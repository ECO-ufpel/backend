package com.ecoufpel.ecoufpelapp.domains.sensor;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classroom_energy_consumption", schema = "sensor_data")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataComsumption {
    @EmbeddedId
    private DataComsumptionID id;
    private double consumption;
}
