package com.ecoufpel.ecoufpelapp.domains.websocket;

import org.springframework.web.socket.TextMessage;

public record ExpectedConsumptionMessageDTO(String roomId, Double expectedConsumption, Double stdDev) {
    public TextMessage toTextMessage() {
        return new TextMessage(String.format("""
                {
                "type": "expected_consumption",
                "average": "%s",
                "std_dev": "%s",
                "room_id": "%s"
                }
                """, expectedConsumption, stdDev, roomId));
    }
}
