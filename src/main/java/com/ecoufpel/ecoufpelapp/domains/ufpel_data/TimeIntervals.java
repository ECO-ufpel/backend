package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Table(name = "time_intervals", schema = "ufpel_data")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeIntervals {
    @Id
    private Integer id;
    private Time startTime;
    private Time endTime;
}
