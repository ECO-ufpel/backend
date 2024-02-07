package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.sensor.Data;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataDTO;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataID;
import com.ecoufpel.ecoufpelapp.repositories.DataRepository;
import com.ecoufpel.ecoufpelapp.services.CheckAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("sensor")
public class SensorDataController {
    @Autowired
    private CheckAPIService checkAPIService;
    @Autowired
    private DataRepository sensorDataRepository;

    public ResponseEntity insertData(DataDTO data, UUID key) {
        if (checkAPIService.checkAPIKey(key)) {
            var sensorData = new Data(new DataID(data.room_id(), data.time()), data.consumption());
            sensorDataRepository.save(sensorData);
            return ResponseEntity.status(HttpStatus.OK).body("Data inserted");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("API Key not valid");
    }

    @PostMapping("/insert")
    public ResponseEntity insertDataWithParams(@RequestParam("room_id") BigInteger room_id, @RequestParam("time") String time, @RequestParam("consumption") double consumption, @RequestParam("API-Key") String key) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME));
        UUID key_uuid = UUID.fromString(key);
        return this.insertData(new DataDTO(room_id, timestamp, consumption), key_uuid);
    }

}
