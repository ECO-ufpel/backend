package com.ecoufpel.ecoufpelapp.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.HashMap;

@Service
public class WebSocketHandler extends TextWebSocketHandler {
//    HashMap<Principal, WebSocketSession> sessions = new HashMap<>(); // user_cpf, session

    private WebSocketEventListener webSocketEventListener = new WebSocketEventListener();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        session.sendMessage(new TextMessage("Polo!"));
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var principal = session.getPrincipal();

        if (principal == null) {
            session.close();
            System.out.println("User not authenticated");
            return;
        }

        webSocketEventListener.register_user(principal, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        webSocketEventListener.remove_user(session.getPrincipal(), session);
    }

}
