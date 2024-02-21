package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataComsumption;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionDTO;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataComsumptionID;
import com.ecoufpel.ecoufpelapp.repositories.DataComsumptionRepository;
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
    private DataComsumptionRepository sensorDataComsumptionRepository;

    public ResponseEntity insertData(DataConsumptionDTO data, UUID key) {
        if (checkAPIService.checkAPIKey(key)) {
            var sensorData = new DataComsumption(new DataComsumptionID(data.classroom_id(), data.date_time()), data.consumption());
            sensorDataComsumptionRepository.save(sensorData);
            return ResponseEntity.status(HttpStatus.OK).body("Data inserted");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("API Key not valid");
    }

    @PostMapping("/insert")
    public ResponseEntity insertDataWithParams(@RequestParam("room_id") String room_id, @RequestParam("time") String time, @RequestParam("consumption") double consumption, @RequestParam("API-Key") String key) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME));
        UUID key_uuid = UUID.fromString(key);
        return this.insertData(new DataConsumptionDTO(room_id, timestamp, consumption), key_uuid);
    }

}
