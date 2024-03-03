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

    public static final long VERIFY_DATABASE_RATE_MAX = 5000;
    public static final long VERIFY_DATABASE_RATE_MIN = 1000;

    private final WebSocketEventListener webSocketEventListener;

    private final DataConsumptionRepository dataConsumptionRepository;
    private Timestamp lastTimeStampChecked;
    private final Timer timer;
    private final Random random;

    @Autowired
    public GetConsumptionDataService(WebSocketEventListener webSocketEventListener, DataConsumptionRepository dataConsumptionRepository) {
        this.webSocketEventListener = webSocketEventListener;
        this.dataConsumptionRepository = dataConsumptionRepository;
        this.timer = new Timer();
        this.random = new Random();
    }

    public void initialize() {
        lastTimeStampChecked = new Timestamp(System.currentTimeMillis());
        this.timer.schedule(new GetConsumptionTask(), 0);
    }

    public class GetConsumptionTask extends TimerTask {
        @Override
        public void run() {
            sendConsumptionDataToWS();
            timer.schedule(new GetConsumptionTask(), random.nextLong(VERIFY_DATABASE_RATE_MIN, VERIFY_DATABASE_RATE_MAX));
        }
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
