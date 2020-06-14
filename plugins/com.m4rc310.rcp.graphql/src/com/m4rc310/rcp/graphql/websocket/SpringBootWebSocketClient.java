package com.m4rc310.rcp.graphql.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by chen0 on 9/12/2017.
 */

@Singleton
@Creatable
public final class SpringBootWebSocketClient extends WebSocketListener {

	private Map<String, TopicHandler> topics = new HashMap<>();
	private CloseHandler closeHandler;
	private String id = "sub-001";
	private WebSocket webSocket;

	private String address;

	public SpringBootWebSocketClient() {

	}

	public SpringBootWebSocketClient(String id) {
		this.id = id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public TopicHandler subscribe(String topic) {
		
		TopicHandler handler = new TopicHandler(topic);
		topics.put(topic, handler);
		if (webSocket != null) {
			sendSubscribeMessage(webSocket, topic);
		}
		return handler;
	}

	public void unSubscribe(String topic) {
		topics.remove(topic);
	}

	public TopicHandler getTopicHandler(String topic) {
		if (topics.containsKey(topic)) {
			return topics.get(topic);
		}
		return null;
	}

	public void update() {
		if (this.address != null) {
			connect(address);
		}
	}

	public void connect(String address) {

		this.address = address;

		OkHttpClient client = new OkHttpClient.Builder()
				.pingInterval(30, TimeUnit.SECONDS)
				.readTimeout(0, TimeUnit.MILLISECONDS).build();

		Request request = new Request.Builder().url(address).build();
		client.newWebSocket(request, this);
		
		

		// Trigger shutdown of the dispatcher's executor so this process can exit
		// cleanly.
		client.dispatcher().executorService().shutdown();
	}

	@Override
	public void onOpen(WebSocket webSocket, Response response) {

		/*
		 * webSocket.send("Hello..."); webSocket.send("...World!");
		 * webSocket.send(ByteString.decodeHex("deadbeef"));
		 */

		this.webSocket = webSocket;

		sendConnectMessage(webSocket);

		for (String topic : topics.keySet()) {
			sendSubscribeMessage(webSocket, topic);
		}

		closeHandler = new CloseHandler(webSocket);
	}

	private void sendConnectMessage(WebSocket webSocket) {
		StompMessage message = new StompMessage("CONNECT");
		message.put("accept-version", "1.1");
		message.put("heart-beat", "10000,10000");
		webSocket.send(StompMessageSerializer.serialize(message));
	}

	private void sendSubscribeMessage(WebSocket webSocket, String topic) {
		StompMessage message = new StompMessage("SUBSCRIBE");
		message.put("id", id);
		message.put("destination", topic);
		webSocket.send(StompMessageSerializer.serialize(message));
	}

	public void disconnect() {
		if (webSocket != null) {
			closeHandler.close();
			webSocket = null;
			closeHandler = null;
		}
	}

	public boolean isConnected() {
		return closeHandler != null;
	}

	@Override
	public void onMessage(WebSocket webSocket, String text) {
		// System.out.println("MESSAGE: " + text);
		StompMessage message = StompMessageSerializer.deserialize(text);
		String topic = message.getHeader("destination");
		if (topics.containsKey(topic)) {
			topics.get(topic).onMessage(message);
		}
	}

	@Override
	public void onMessage(WebSocket webSocket, ByteString bytes) {
		System.out.println("MESSAGE: " + bytes.hex());
	}

	@Override
	public void onClosing(WebSocket webSocket, int code, String reason) {
		webSocket.close(1000, null);
		System.out.println("CLOSE: " + code + " " + reason);
	}

	@Override
	public void onFailure(WebSocket webSocket, Throwable t, Response response) {
		if (address != null) {
			connect(address);
		}
	}

	public static void main(String... args) {
		SpringBootWebSocketClient client = new SpringBootWebSocketClient();
		TopicHandler handler = client.subscribe("/topics/event");

		handler.addListener(new StompMessageListener() {
			@Override
			public void onMessage(StompMessage message) {
				System.out.println(message.getHeader("destination") + ": " + message.getContent());

			}
		});

//        client.connect("ws://localhost:8080/my-ws/websocket");
		client.connect("ws://m4rc310.herokuapp.com/ws/websocket");
//        client.connect("ws://m4rc310.herokuapp.com/gs-guide-websocket/websocket");

	}
}