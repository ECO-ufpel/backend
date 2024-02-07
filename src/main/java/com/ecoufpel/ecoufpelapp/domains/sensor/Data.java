package com.ecoufpel.ecoufpelapp.domains.sensor;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigInteger;
import java.sql.Time;

@Entity
@Table(name = "data", schema = "sensors")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    @EmbeddedId
    private DataID id;
    private double consumption;
}
