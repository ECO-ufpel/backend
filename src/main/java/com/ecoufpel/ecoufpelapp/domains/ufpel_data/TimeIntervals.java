package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.sql.Timestamp;

@Table(name = "time_intervals", schema = "ufpel_data")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeIntervals {
    @Id
    private Integer id;
    private Timestamp startTime;
    private Timestamp endTime;
}
