package com.ecoufpel.ecoufpelapp.services;


import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionDTO;
import com.ecoufpel.ecoufpelapp.websocket.WebSocketEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecoufpel.ecoufpelapp.repositories.DataConsumptionRepository;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class GetConsumptionDataService {

    public static final int MIN_INTERVAL_VALUE = 3000;
    public static final int MAX_INTERVAL_VALUE = 10000;

    @Autowired
    private WebSocketEventListener webSocketEventListener;

    @Autowired
    DataConsumptionRepository dataConsumptionRepository;
    private Timestamp lastTimeStampChecked;
    @Autowired
    public GetConsumptionDataService(WebSocketEventListener webSocketEventListener) {
        this.webSocketEventListener = webSocketEventListener;
    }

    public void initialize() {
        lastTimeStampChecked = new Timestamp(System.currentTimeMillis());
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendConsumptionDataToWS();
                int newInterval = generateRandomInterval(MIN_INTERVAL_VALUE, MAX_INTERVAL_VALUE);
                timer.schedule(this, newInterval);
            }
        };

        timer.schedule(task, 0);
    }

    private static int generateRandomInterval(int min, int max) {
        Random random = new Random();
        return random.nextInt(min - max + 1) + min;
    }

    private void sendConsumptionDataToWS() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        dataConsumptionRepository
            .findById_DateTimeBetween(lastTimeStampChecked, now)
            .ifPresent(dataConsumptionList -> {
                dataConsumptionList.forEach(dataConsumptionEntry -> {
                    webSocketEventListener.onNext(new DataConsumptionDTO(
                            dataConsumptionEntry.getId().getClassroomId(),
                            dataConsumptionEntry.getId().getDateTime(),
                            dataConsumptionEntry.getConsumption()
                            )
                    );
                });
            });
        lastTimeStampChecked = now;
    }
}
