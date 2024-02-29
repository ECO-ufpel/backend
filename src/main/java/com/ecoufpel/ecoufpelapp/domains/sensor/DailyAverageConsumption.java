package com.ecoufpel.ecoufpelapp.domains.sensor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyAverageConsumption {
    private String date;
    private Double avgConsumption;

    public DailyAverageConsumption(Timestamp date, double avgConsumption) {
        LocalDateTime localDateTime = date.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        this.date = localDateTime.format(formatter);
        this.avgConsumption = avgConsumption;
    }
}