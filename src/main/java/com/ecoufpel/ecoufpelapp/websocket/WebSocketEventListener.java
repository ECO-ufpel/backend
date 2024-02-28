package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionDTO;
import com.ecoufpel.ecoufpelapp.domains.ufpel_data.Classrooms;
import com.ecoufpel.ecoufpelapp.domains.user.User;
import com.ecoufpel.ecoufpelapp.repositories.*;
import com.ecoufpel.ecoufpelapp.services.InsertDataConsuptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;

@Service
public class WebSocketEventListener implements Flow.Subscriber<DataConsumptionDTO> {
    private final HashMap<String, List<WebSocketSession>> usersConnected = new HashMap<>();

    private final InsertDataConsuptionService insertDataService;

    private final ClassroomsRepository classroomsRepository;

    @Autowired
    public WebSocketEventListener(InsertDataConsuptionService insertDataService, ClassroomsRepository classroomsRepository) {
        this.insertDataService = insertDataService;
        this.classroomsRepository = classroomsRepository;
        insertDataService.subscribe(this);
    }

    public void register_user(User user, WebSocketSession socket) {
        var room_id = searchClassroomRepository(user.getCpf()).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Name: " + user.getName() + " added to list");

        var list = usersConnected.get(room_id);

        if (list == null) {
            list = new java.util.ArrayList<>();
            list.add(socket);
            usersConnected.put(room_id, list);
        } else {
            list.add(socket);
        }
    }

    public void remove_user(User user, WebSocketSession socket) {
        var room_id = searchClassroomRepository(user.getCpf()).orElseThrow(() -> new RuntimeException("User not found"));

        var list = usersConnected.get(room_id);

        if (list != null) {
            list.remove(socket);
        }
        else {
            usersConnected.remove(room_id, socket);
        }
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(DataConsumptionDTO item) {
        List<WebSocketSession> room_id_list = usersConnected.get(item.classroom_id());

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

    private Optional<String> searchClassroomRepository(String cpf) {
        var classroom = classroomsRepository.findByUserCpf(cpf);
        return classroom.map(Classrooms::getId);
    }
}
