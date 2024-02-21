package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.sensor.Data;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataDTO;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataID;
import com.ecoufpel.ecoufpelapp.repositories.DataRepository;
import com.ecoufpel.ecoufpelapp.services.CheckAPIService;
import com.ecoufpel.ecoufpelapp.services.InsertDataService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.UUID;
import java.util.concurrent.Flow;

@RestController
@RequestMapping("sensor")
public class SensorDataController {
    @Autowired
    InsertDataService insertDataService;

    @PostMapping("/insert")
    public ResponseEntity insertDataWithParams(@RequestParam("room_id") BigInteger room_id, @RequestParam("time") String time, @RequestParam("consumption") double consumption, @RequestParam("API-Key") String key) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME));
        UUID key_uuid = UUID.fromString(key);
        return insertDataService.insertData(new DataDTO(room_id, timestamp, consumption), key_uuid);
    }

}
