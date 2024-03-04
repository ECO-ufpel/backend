package com.ecoufpel.ecoufpelapp.domains.sensor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "classroom_data_aggregation", schema = "sensor_data")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDataAggregation {
    @Id
    private String classroomId;
    private Integer avgConsumption;
    private Integer minConsumption;
    private Integer maxConsumption;
    private Integer stdConsumption;
    private Timestamp aggregationDate;
}
