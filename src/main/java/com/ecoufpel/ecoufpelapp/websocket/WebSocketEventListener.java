package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.controllers.SensorDataController;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataDTO;
import com.ecoufpel.ecoufpelapp.domains.user.User;
import com.ecoufpel.ecoufpelapp.repositories.UserRepository;
import com.ecoufpel.ecoufpelapp.services.InsertDataService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;

@Service
public class WebSocketEventListener implements Flow.Subscriber<DataDTO> {
    private HashMap<String, List<WebSocketSession>> users_connected = new HashMap<>();

    @Autowired
    private InsertDataService insertDataService = new InsertDataService();

    @Autowired
    private UserRepository userRepository;

    public WebSocketEventListener() {
        insertDataService.subscribe(this);
    }

    public void register_user(Principal principal, WebSocketSession socket) {

        Optional<User> user_option = (Optional<User>) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        if (user_option.isEmpty()) {
            return;
        }

        var user = user_option.get();

        // Make a query to get the room ID
        // For now we will use the user's CPF as the room ID
        var room_id = user.getCpf();

        System.out.println("Name: " + user.getName() + " added to list");

        var list = users_connected.get(room_id);

        if (list == null) {
            list = new java.util.ArrayList<>();
            list.add(socket);
            users_connected.put(room_id, list);
        } else {
            list.add(socket);
        }
    }

    public void remove_user(Principal principal, WebSocketSession socket) {
        User user = (User) principal;
        if (user == null) {
            return;
        }

        // Make a query to get the room ID
        // For now we will use the user's CPF as the room ID
        var room_id = user.getCpf();

        var list = users_connected.get(room_id);

        if (list != null) {
            list.remove(socket);
        }
        else {
            users_connected.remove(room_id, socket);
        }
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(DataDTO item) {
        List<WebSocketSession> room_id_list = users_connected.get(item.room_id().toString());

        if (room_id_list == null) {
            return;
        }

        room_id_list.forEach((socket) -> {
            try {
                socket.sendMessage(item.toTextMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("ERROR: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Completed");
    }

}
