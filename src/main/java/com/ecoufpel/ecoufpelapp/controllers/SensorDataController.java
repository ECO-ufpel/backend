package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionDTO;
import com.ecoufpel.ecoufpelapp.services.InsertDataConsuptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("sensor")
public class SensorDataController {
    @Autowired
    InsertDataConsuptionService insertDataService;

    @PostMapping("/insert")
    public ResponseEntity insertDataWithParams(@RequestParam("room_id") String room_id, @RequestParam("time") String time, @RequestParam("consumption") double consumption, @RequestParam("API-Key") String key) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME));
        UUID key_uuid = UUID.fromString(key);
        return insertDataService.insertData(new DataConsumptionDTO(room_id, timestamp, consumption), key_uuid);
    }

}
