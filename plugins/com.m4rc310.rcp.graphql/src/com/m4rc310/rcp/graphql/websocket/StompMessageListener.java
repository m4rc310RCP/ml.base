package com.m4rc310.rcp.graphql.websocket;

/**
 * Created by chen0 on 10/12/2017.
 */

public interface StompMessageListener {
    void onMessage(StompMessage message);
//    void onMessage(StompMessage message, Exception e);
}
