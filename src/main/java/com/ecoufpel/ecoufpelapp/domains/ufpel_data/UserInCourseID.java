package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserInCourseID implements Serializable {
    private String userCpf;
    private String courseId;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

