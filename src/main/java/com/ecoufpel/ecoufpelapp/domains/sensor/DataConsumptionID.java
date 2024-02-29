package com.ecoufpel.ecoufpelapp.domains.sensor;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DataConsumptionID implements Serializable {
    private String classroomId;
    private Timestamp dateTime;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
