package com.ecoufpel.ecoufpelapp.domains.sensor;

import org.springframework.web.socket.TextMessage;

import java.sql.Timestamp;

public record DataConsumptionDTO(String classroomId, Timestamp dateTime, double consumption) {

    public TextMessage toTextMessage() {
        return new TextMessage(
                """
                {
                    "Room ID": %s,
                    "Time": "%s",
                    "Consumption": %s
                }
                """.formatted(classroomId, dateTime, consumption));
    }
}
