package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.domains.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

@Service
public class WebSocketHandler extends TextWebSocketHandler {
    private final WebSocketEventListener webSocketEventListener;

    @Autowired
    public WebSocketHandler(WebSocketEventListener webSocketEventListener) {
        this.webSocketEventListener = webSocketEventListener;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        session.sendMessage(new TextMessage("Polo!"));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = (User) session.getAttributes().get("user");
        if (user == null){
            session.close();
            System.out.println("User not authenticated");
            return;
        }

        webSocketEventListener.register_user(user, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        User user = (User) session.getAttributes().get("user");
        if (user == null) {
            return;
        }
        webSocketEventListener.remove_user(user, session);
    }

}
