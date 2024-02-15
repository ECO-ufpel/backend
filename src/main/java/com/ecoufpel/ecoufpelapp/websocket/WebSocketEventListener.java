package com.ecoufpel.ecoufpelapp.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    @EventListener
    public void onApplicationEvent(SessionConnectedEvent event) {
//        webSocketService.addSession(event.getUser());
        System.out.println("Connected: " + event.toString());
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    }

//    public void onApplicationEvent(SessionDisconnectEvent event) {
//        webSocketService.removeSession(event.getUser());
//    }
}
