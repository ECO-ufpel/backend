package com.ecoufpel.ecoufpelapp.services;

import com.ecoufpel.ecoufpelapp.domains.sensor.*;
import com.ecoufpel.ecoufpelapp.repositories.DataComsumptionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Flow;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class InsertDataConsuptionService implements Flow.Publisher<DataConsumptionDTO> {
    private final static List<Flow.Subscriber<? super DataConsumptionDTO>> subscribers = new ArrayList<>();

    @Autowired
    private CheckAPIService checkAPIService;
    @Autowired
    private DataComsumptionRepository sensorDataRepository;

    public ResponseEntity insertData(DataConsumptionDTO data, UUID key) {
        if (checkAPIService.checkAPIKey(key)) {
            var sensorData = new DataComsumption(new DataComsumptionID(data.classroom_id(), data.date_time()), data.consumption());
            subscribers.forEach(subscriber -> subscriber.onNext(data));
            sensorDataRepository.save(sensorData);
            return ResponseEntity.status(HttpStatus.OK).body("Data inserted");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("API Key not valid");
    }

    @Override
    public void subscribe(Flow.Subscriber<? super DataConsumptionDTO> subscriber) {
        subscribers.add(subscriber);
    }
}
