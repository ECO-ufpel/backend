package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @SubscribeMapping("/data")
    public String subscribeData() {
        return "Connected";
    }

    @MessageMapping("/data")
    @SendTo("/topic/data")
//    @SendToUser("/queue/data")
    public void sendData(@Payload String user_cpf) throws Exception {

        template.convertAndSend("/topic/data", new DataConsumptionMessage(new DataDTO(BigInteger.valueOf(2), Timestamp.valueOf(LocalDateTime.now()), 3.14)));
//        return new DataConsumptionMessage(new DataDTO(BigInteger.valueOf(2), Timestamp.valueOf(LocalDateTime.now()), 3.14));
    }

}
