package com.m4rc310.rcp.graphql.websocket;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chen0 on 10/12/2017.
 */

public class TopicHandler {
    private String topic;
    private Set<StompMessageListener> listeners = new HashSet<>();
    public TopicHandler(String topic) {
        this.topic = topic;
    }

    public TopicHandler() {

    }

    public String getTopic() {
        return topic;
    }

    public void addListener(StompMessageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(StompMessageListener listener) {
        listeners.remove(listener);
    }

    public void onMessage(StompMessage message) {
        for(StompMessageListener listener : listeners){
            listener.onMessage(message);
        }

    }
}
