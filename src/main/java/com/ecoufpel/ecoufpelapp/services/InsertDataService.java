package com.ecoufpel.ecoufpelapp.services;

import com.ecoufpel.ecoufpelapp.domains.sensor.Data;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataDTO;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataID;
import com.ecoufpel.ecoufpelapp.repositories.DataRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Flow;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class InsertDataService implements Flow.Publisher<DataDTO> {
    private final static List<Flow.Subscriber<? super DataDTO>> subscribers = new ArrayList<>();

    @Autowired
    private CheckAPIService checkAPIService;
    @Autowired
    private DataRepository sensorDataRepository;

    public ResponseEntity insertData(DataDTO data, UUID key) {
        if (checkAPIService.checkAPIKey(key)) {
            var sensorData = new Data(new DataID(data.room_id(), data.time()), data.consumption());
            subscribers.forEach(subscriber -> subscriber.onNext(data));
            sensorDataRepository.save(sensorData);
            return ResponseEntity.status(HttpStatus.OK).body("Data inserted");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("API Key not valid");
    }

    @Override
    public void subscribe(Flow.Subscriber<? super DataDTO> subscriber) {
        subscribers.add(subscriber);
    }
}
