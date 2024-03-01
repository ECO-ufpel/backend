package com.ecoufpel.ecoufpelapp.domains.websocket;

import org.springframework.boot.json.JsonParserFactory;

import java.util.Optional;

public record SubscriptionToRoomMessageDTO(SubscriptionToRoomMessageType type, String room_id) {
    static public Optional<SubscriptionToRoomMessageDTO> fromJson(String json) {
        var parser = JsonParserFactory.getJsonParser();
        var map = parser.parseMap(json);

        if (!map.containsKey("type") || !map.containsKey("room_id")) {
            return Optional.empty();
        }
        var message = new SubscriptionToRoomMessageDTO(
                SubscriptionToRoomMessageType.valueOf(map.get("type").toString().toUpperCase()),
                map.get("room_id").toString());
        return Optional.of(message);
    }
}
