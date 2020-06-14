package com.m4rc310.rcp.graphql.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by chen0 on 10/12/2017.
 */

public class StompMessage {
    private Map<String, String> headers = new HashMap<>();
    private String body="";
    private String command;

    public StompMessage(String command) {
        this.command = command;
    }

    public StompMessage() {

    }

    public String getHeader(String name){
        return headers.get(name);
    }

    public void put(String name, String value){
        headers.put(name, value);
    }
    public void setContent(String body){
        this.body = body;
    }
    
    public <T> T getContent(Class<T> type) {
    	try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(body, type);
		} catch (Exception e) {
			return null;
		}
    }
    
    public <T> List<T> getContentList(Class<T> type) {
    	try {
    		JSONParser parser = new JSONParser();
    		JSONArray array = (JSONArray) parser.parse(body);
    		
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
    		return null;
    	}
    }
    

	public String getContent() {
        return body;
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
    public String getCommand() {
        return command;
    }
}
