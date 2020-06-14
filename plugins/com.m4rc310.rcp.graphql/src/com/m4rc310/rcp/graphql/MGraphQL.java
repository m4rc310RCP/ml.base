package com.m4rc310.rcp.graphql;

//package com.m4rc310.rcp.graphql;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Singleton;

import org.apache.http.impl.client.HttpClients;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.json.simple.JSONObject;
//import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Creatable
@Singleton
public class MGraphQL {

//	private final String uri = "https://mls-com.herokuapp.com/graphql";
	private final String uri = "http://localhost:8080/graphql";

	public JSONObject query(String query) throws JsonParseException, JsonMappingException, IOException, ParseException {

		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
				HttpClients.createDefault());

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/graphql");

		ResponseEntity<String> response = restTemplate.postForEntity(uri, new HttpEntity<>(query, headers),
				String.class);

		JSONParser parser = new JSONParser();
		JSONObject jsono = (JSONObject) parser.parse(response.getBody());

//		if (jsono.containsKey("errors")) {
//			return (JSONObject) jsono.get("errors");
//		}

		return (JSONObject) jsono.get("data");
	}

	public <T> T query(String query, Class<T> type)
			throws JsonParseException, JsonMappingException, IOException, ParseException {

		JSONObject res = query(query);

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(res.toJSONString(), type);
	}

	public <T> T queryInFile(String pluginId, String path, Class<T> type)
			throws JsonParseException, JsonMappingException, IOException, ParseException {

		try {
			URL bundle = Platform.getBundle(pluginId).getResource(path);
			path = FileLocator.toFileURL(bundle).getFile();
			String query = new String(Files.readAllBytes(Paths.get(path)));
			return query(query, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String toGraph(Object value) {
		try {
//			String json = "{\"alias\":\"MLS\",\"nome\":\"Marcelo Lopes\",\"senha\":null,\"senhaTemporaria\":null,\"atribuicao\":{\"id\":1,\"desenvolvedor\":true,\"administrador\":true},\"id\":1}\n" + 
//					"";
			String json = toJson(value);
			json = json.trim();

//			org.json.JSONObject jsono = new org.json.JSONObject(json);

//			Iterator<?> itr = jsono.keys();
//			while (itr.hasNext()) {
//				Object key = (Object) itr.next();
//				
//				String skey = String.format("\"%s\":", key);
//				String nkey = String.format("%s:", key);
//				
//				json = json.replace(skey, nkey);
//				
//			}

			Pattern p = Pattern.compile("\"\\w+\":");
			Matcher m = p.matcher(json);

			while (m.find()) {
				String okey = m.group();
				String nkey = okey.substring(1, okey.length() - 2);
				nkey = String.format("%s:", nkey);

				json = json.replace(okey, nkey);
			}

//		    json = json.replace(",", " ");

			return json;

		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}

	}

	public static void main(String[] args) {

		System.out.println(new MGraphQL().toGraph(null));

	}

//	private static Collection<Field> getAllFields(Class<?> type) {
//
//		Collection<Field> ret = new ArrayList<>();
//		while (type != Object.class) {
//			ret.addAll(Arrays.asList(type.getDeclaredFields()));
//			type = type.getSuperclass();
//		}
//		return ret;
//	}

	public static String toJson(Object value) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

}
