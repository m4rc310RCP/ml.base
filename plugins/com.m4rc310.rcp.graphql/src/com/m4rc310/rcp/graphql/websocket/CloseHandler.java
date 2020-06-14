package com.m4rc310.rcp.graphql.websocket;

import okhttp3.WebSocket;

/**
 * Created by chen0 on 10/12/2017.
 */

public class CloseHandler {
    private final WebSocket webSocket;
    public CloseHandler(WebSocket webSocket){
        this.webSocket = webSocket;
    }

    public void close() {
        webSocket.close(1000, "close websocket");
    }
}
