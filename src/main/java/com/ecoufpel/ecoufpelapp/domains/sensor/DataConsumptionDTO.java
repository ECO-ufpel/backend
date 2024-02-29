package com.ecoufpel.ecoufpelapp.domains.sensor;

import org.springframework.web.socket.TextMessage;

import java.math.BigInteger;
import java.sql.Timestamp;

public record DataConsumptionDTO(String classroom_id, Timestamp dateTime, double consumption) {

    public TextMessage toTextMessage() {
        return new TextMessage(
                """
                {
                    "Room ID": %s,
                    "Time": "%s",
                    "Consumption": %s
                }
                """.formatted(classroom_id, dateTime, consumption));
    }
}
