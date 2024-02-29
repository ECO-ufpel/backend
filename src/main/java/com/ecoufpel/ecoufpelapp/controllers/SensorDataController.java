package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionDTO;
import com.ecoufpel.ecoufpelapp.services.InsertDataConsuptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("sensor")
public class SensorDataController {
    private final InsertDataConsuptionService insertDataService;

    @Autowired
    public SensorDataController(InsertDataConsuptionService insertDataService) {
        this.insertDataService = insertDataService;
    }

    @PostMapping("/insert")
    public ResponseEntity insertDataWithParams(@RequestParam("room_id") String room_id, @RequestParam("time") String time, @RequestParam("consumption") double consumption, @RequestParam("API-Key") String key) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME));
        UUID key_uuid = UUID.fromString(key);
        return insertDataService.insertData(new DataConsumptionDTO(room_id, timestamp, consumption), key_uuid);
    }

    @GetMapping("/data/history")
    public ResponseEntity getHistoryData(@RequestParam("room_id") String room_id, @RequestParam("start_time") String start_time, @RequestParam("end_time") String end_time, @RequestParam("API-Key") String key) {
        Timestamp start = Timestamp.valueOf(LocalDateTime.parse(start_time, DateTimeFormatter.ISO_DATE_TIME));
        Timestamp end = Timestamp.valueOf(LocalDateTime.parse(end_time, DateTimeFormatter.ISO_DATE_TIME));
        UUID key_uuid = UUID.fromString(key);
        return insertDataService.getHistoryData(room_id, start, end, key_uuid);
    }

}
