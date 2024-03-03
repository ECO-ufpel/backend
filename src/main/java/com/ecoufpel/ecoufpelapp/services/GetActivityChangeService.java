package com.ecoufpel.ecoufpelapp.services;

import com.ecoufpel.ecoufpelapp.domains.ufpel_data.CourseChangeDTO;
import com.ecoufpel.ecoufpelapp.repositories.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

@Service
public class GetActivityChangeService {
    private final HashMap<String, Pair<List<WebSocketSession>, Timer>> usersConnected = new HashMap<>();
    private final CoursesRepository coursesRepository;
    @Autowired
    public GetActivityChangeService(CoursesRepository coursesRepository){
        this.coursesRepository = coursesRepository;
    }

    public class MyTask extends TimerTask {
        final private String cpf;
        public MyTask(String cpf) {
            this.cpf = cpf;
        }
        @Override
        public void run() {
            var pair = usersConnected.get(this.cpf);
            if (pair == null) {
                return;
            }

            // Send the current activity to the user
            var currentActivity = coursesRepository.findCurrentCourseByUserCpf(this.cpf);
            var message = new CourseChangeDTO(null, null, null, null);
            if (currentActivity.isPresent()) {
                message = currentActivity.get();
            }
            CourseChangeDTO finalMessage = message;
            pair.getFirst().forEach(socket -> {
                try {
                    socket.sendMessage(finalMessage.toTextMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // Set a timer for the next activity
            var timeInterval = coursesRepository.findTimeIntervalsByUserCpf(this.cpf);
            if (timeInterval.isPresent()) {
                var endTime = timeInterval.get().getEndTime();
                var now = new Time(System.currentTimeMillis());
                var milis_end = endTime.getHours() * 60 * 60 * 1000 + endTime.getMinutes() * 60 * 1000 + endTime.getSeconds() * 1000;
                var milis_now = now.getHours() * 60 * 60 * 1000 + now.getMinutes() * 60 * 1000 + now.getSeconds() * 1000;
                var milis_delay = milis_end - milis_now + 1000;
                pair.getSecond().schedule(new MyTask(this.cpf), milis_delay);
            }
            else {
                var next_time_interval = coursesRepository.findNextTimeIntervalsByUserCpf(this.cpf);
                if (next_time_interval.isPresent()) {
                    var startTime = next_time_interval.get().getStartTime();
                    var now = new Time(System.currentTimeMillis());
                    var milis_start = startTime.getHours() * 60 * 60 * 1000 + startTime.getMinutes() * 60 * 1000 + startTime.getSeconds() * 1000;
                    var milis_now = now.getHours() * 60 * 60 * 1000 + now.getMinutes() * 60 * 1000 + now.getSeconds() * 1000;
                    var milis_delay = milis_start - milis_now + 1000;
                    pair.getSecond().schedule(new MyTask(this.cpf), milis_delay);
                }
                // TODO: if there is no time interval it may because is on the end of the day, in that case the localtime on the query must be midnight
                // to get the first on the day, or simply return all time intervals and order and get the first one
            }
        }
    }

    public void register_user(String cpf, WebSocketSession socket) {
        if (usersConnected.containsKey(cpf)) {
            var pair = usersConnected.get(cpf);
            var list = pair.getFirst();
            list.add(socket);
            var timer = pair.getSecond();
            timer.cancel();
            timer.schedule(new MyTask(cpf), 0);
        }
        else {
            var list = new ArrayList<WebSocketSession>();
            list.add(socket);
            var timer = new Timer();
            usersConnected.put(cpf, Pair.of(list, timer));
            timer.schedule(new MyTask(cpf), 0);
        }
    }

    public void remove_user(WebSocketSession socket) {
        usersConnected.forEach((cpf, pair) -> {
            var list = pair.getFirst();
            list.remove(socket);
        });
    }
}
