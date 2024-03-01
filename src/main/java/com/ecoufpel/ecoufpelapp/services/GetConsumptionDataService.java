package com.ecoufpel.ecoufpelapp.services;


import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionDTO;
import com.ecoufpel.ecoufpelapp.websocket.WebSocketEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecoufpel.ecoufpelapp.repositories.DataConsumptionRepository;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class GetConsumptionDataService {

    public static final int VERIFY_DATABASE_RATE = 5000;

    private final WebSocketEventListener webSocketEventListener;

    private final DataConsumptionRepository dataConsumptionRepository;
    private Timestamp lastTimeStampChecked;
    @Autowired
    public GetConsumptionDataService(WebSocketEventListener webSocketEventListener, DataConsumptionRepository dataConsumptionRepository) {
        this.webSocketEventListener = webSocketEventListener;
        this.dataConsumptionRepository = dataConsumptionRepository;
    }

    public void initialize() {
        lastTimeStampChecked = new Timestamp(System.currentTimeMillis());
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendConsumptionDataToWS();
            }
        };
        timer.scheduleAtFixedRate(task, 0, VERIFY_DATABASE_RATE);
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
