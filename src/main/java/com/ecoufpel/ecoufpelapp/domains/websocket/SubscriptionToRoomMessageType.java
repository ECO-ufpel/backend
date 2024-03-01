package com.ecoufpel.ecoufpelapp.domains.websocket;

public enum SubscriptionToRoomMessageType {
    SUBSCRIBE("subscribe"),
    UNSUBSCRIBE("unsubscribe");

    private final String type;

    SubscriptionToRoomMessageType(String type) { this.type = type; }

    public String getType() {
        return type;
    }
}
