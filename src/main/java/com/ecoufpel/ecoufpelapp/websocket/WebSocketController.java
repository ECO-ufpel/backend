package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
public class WebSocketController {

    @MessageMapping("/data")
    @SendTo("/topic/data")
    public DataConsumptionMessage sendData(String user_cpf) {
        return new DataConsumptionMessage(new DataDTO(BigInteger.valueOf(2), Timestamp.valueOf(LocalDateTime.now()), 3.14));
    }

}
