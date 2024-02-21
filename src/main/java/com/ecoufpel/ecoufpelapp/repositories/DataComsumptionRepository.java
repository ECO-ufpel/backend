package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataComsumption;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataComsumptionID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataComsumptionRepository extends JpaRepository<DataComsumption, DataComsumptionID> {
}
