package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.sensor.Data;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.sql.Time;

public interface DataRepository extends JpaRepository<Data, DataID> {
}
