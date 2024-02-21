package com.ecoufpel.ecoufpelapp.domains.sensor;

import org.springframework.web.socket.TextMessage;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public record DataDTO(BigInteger room_id, Timestamp time, double consumption) {
    public TextMessage toTextMessage() {
        return new TextMessage(
                """
                {
                    "Room ID": %s,
                    "Time": "%s",
                    "Consumption": %s
                }
                """.formatted(room_id, time, consumption));
    }
}
