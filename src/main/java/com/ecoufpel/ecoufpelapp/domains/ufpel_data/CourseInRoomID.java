package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CourseInRoomID implements Serializable {
    private Integer interval;
    private String classroomId;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
