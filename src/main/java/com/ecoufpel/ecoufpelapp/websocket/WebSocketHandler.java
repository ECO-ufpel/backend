package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.domains.websocket.SubscriptionToRoomMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class WebSocketHandler extends TextWebSocketHandler {
    private final WebSocketEventListener webSocketEventListener;

    @Autowired
    public WebSocketHandler(WebSocketEventListener webSocketEventListener) {
        this.webSocketEventListener = webSocketEventListener;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var optionalSubscribeMessage = SubscriptionToRoomMessageDTO.fromJson(message.getPayload());

        if (optionalSubscribeMessage.isEmpty()) {
            System.out.println("Invalid message");
            return;
        }

        var subscribeMessage = optionalSubscribeMessage.get();
        System.out.println("Received message: " + message.getPayload());

        switch (subscribeMessage.type()) {
            case SUBSCRIBE -> subscribeToRoom(session, subscribeMessage);
            case UNSUBSCRIBE -> unsubscribeFromRoom(session, subscribeMessage);
        }
    }

    public void subscribeToRoom(WebSocketSession session, SubscriptionToRoomMessageDTO message) {
        webSocketEventListener.register_user(message.room_id(), session);
    }

    public void unsubscribeFromRoom(WebSocketSession session, SubscriptionToRoomMessageDTO message) {
        webSocketEventListener.remove_user(message.room_id(), session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        User user = (User) session.getAttributes().get("user");
//        if (user == null){
//            session.close();
//            System.out.println("User not authenticated");
//            return;
//        }
//
//        webSocketEventListener.register_user(user, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        webSocketEventListener.remove_user(session);
    }

}
