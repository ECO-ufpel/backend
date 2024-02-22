package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionDTO;
import com.ecoufpel.ecoufpelapp.domains.ufpel_data.Classrooms;
import com.ecoufpel.ecoufpelapp.domains.user.User;
import com.ecoufpel.ecoufpelapp.repositories.UserRepository;
import com.ecoufpel.ecoufpelapp.services.InsertDataConsuptionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;

@Service
@Transactional
public class WebSocketEventListener implements Flow.Subscriber<DataConsumptionDTO> {
    private HashMap<String, List<WebSocketSession>> users_connected = new HashMap<>();

    @Autowired
    private InsertDataConsuptionService insertDataService = new InsertDataConsuptionService();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @PersistenceContext(name = "ufpel_data")
    private EntityManager entityManager;

    public WebSocketEventListener() {
        insertDataService.subscribe(this);
    }

    @Transactional
    public void register_user(Principal principal, WebSocketSession socket) {

        Optional<User> user_option = (Optional<User>) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        if (user_option.isEmpty()) {
            return;
        }

        var user = user_option.get();

        TypedQuery<Classrooms> query = entityManager.createQuery("""
                SELECT classroom_id FROM ufpel_data.classrooms AS ca
                JOIN ufpel_data.course_in_room AS cir ON cir.classroom_id = ca.id
                JOIN ufpel_data.time_intervals AS interval ON interval.id = cir.interval
                JOIN ufpel_data.user_in_course AS uic ON uic.course_id = cir.course_id
                WHERE user_cpf = :cpf AND
                LOCALTIME(0) BETWEEN start_time AND end_time
                """,  Classrooms.class).setParameter("cpf", user.getCpf());

        List<Classrooms> resultList = query.getResultList();

        var room_id = resultList.getFirst().getId();

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
    public void onNext(DataConsumptionDTO item) {
        List<WebSocketSession> room_id_list = users_connected.get(item.classroom_id().toString());

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
