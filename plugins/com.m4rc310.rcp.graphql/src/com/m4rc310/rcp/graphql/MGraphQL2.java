package com.m4rc310.rcp.graphql;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.http.impl.client.HttpClients;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m4rc310.rcp.graphql.websocket.SpringBootWebSocketClient;
import com.m4rc310.rcp.graphql.websocket.StompMessageListener;
import com.m4rc310.rcp.graphql.websocket.TopicHandler;

@Creatable
@Singleton
public class MGraphQL2 {

	private final String LOCAL_HOST = "localhost:8080";
	private final String SERVER_HOST = "mls-com.herokuapp.com";

	private final String HTTP = "http";
	private final String HTTPS = "https";

//	private final String LOCAL_URI = String.format("%s://%s/graphql", HTTP, LOCAL_HOST);
//	private final String SERVER_URI = String.format("%s://%s/graphql", HTTP, SERVER_HOST);

	private final boolean isLocalHost = true;

	private final String HOST = isLocalHost ? LOCAL_HOST : SERVER_HOST;

	private final String URI = String.format("%s://%s/graphql", isLocalHost ? HTTP : HTTPS, HOST);

	private final String URI_WEB_SOCKET = String.format("ws://%s/ws/websocket", HOST);
	private JSONObject data;
	private JSONArray errors;

	@Inject
	SpringBootWebSocketClient client;

//	@Inject
//	public void asyncQuery(String query, String ok, String err, StompMessageListener listener) {
//		subscribe(ok, listener);
//	}

	public MGraphQL2 query(String query, Object... args) {
		try {
			query = String.format(query, args);

			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					HttpClients.createDefault());

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

			HttpHeaders headers = new HttpHeaders();
			headers.add("content-type", "application/graphql");

			ResponseEntity<String> response = restTemplate.postForEntity(URI, new HttpEntity<>(query, headers),
					String.class);

			JSONParser parser = new JSONParser();
			JSONObject jsono = (JSONObject) parser.parse(response.getBody());
			this.errors = (JSONArray) jsono.get("errors");
			this.data = (JSONObject) jsono.get("data");

			return this;
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	public String toJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	public String toGraph(Object value) {
		try {
			String json = toJson(value);
			json = json.trim();

			Pattern p = Pattern.compile("\"\\w+\":");
			Matcher m = p.matcher(json);

			while (m.find()) {
				String okey = m.group();
				String nkey = okey.substring(1, okey.length() - 2);
				nkey = String.format("%s:", nkey);
				json = json.replace(okey, nkey);
			}

			return json;

		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}

	}
	
	

	public <T> List<T> getDataList(String field, Class<? extends T> type) {
		try {
			JSONArray array = (JSONArray) data.get(field);

			ObjectMapper mapper = new ObjectMapper();

			Iterator<?> iterator = array.iterator();
			List<T> r = new ArrayList<T>();
			while (iterator.hasNext()) {
				JSONObject jsono = (JSONObject) iterator.next();
				T value = mapper.readValue(jsono.toJSONString(), type);
				r.add(type.cast(value));
			}
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			return Arrays.asList();
		}

	}

	public <T> Optional<T> getData(String field, Class<T> type) {
		try {
			JSONObject jsono = (JSONObject) data.get(field);
			ObjectMapper mapper = new ObjectMapper();
			return Optional.ofNullable(mapper.readValue(jsono.toJSONString(), type));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public void subscribe(String topic, StompMessageListener listener) {

		topic = String.format("/topics/%s", topic);

		TopicHandler handler = client.subscribe(topic);
		handler.addListener(listener);

		client.connect(URI_WEB_SOCKET);
	}

	public GraphQLQueryException getError(Throwable e) {
		try {
			if (errors==null || errors.isEmpty()) {

				if (e != null) {
					return new GraphQLQueryException(e.getMessage());
				}
				
				return null;
			}

			JSONObject error = (JSONObject) errors.get(0);
			String message = (String) error.get("message");

			int i = message.indexOf(" : ");
			message = message.substring(i);
			message = message.replace(" : ", "");

			return new GraphQLQueryException(message);
		} catch (Exception er) {
			throw new GraphQLQueryException(er);
		}

	}

	public GraphQLQueryException getError() {
		return getError(null);
	}

}
