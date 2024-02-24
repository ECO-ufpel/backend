package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumption;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataConsumptionRepository extends JpaRepository<DataConsumption, DataConsumptionID> {
}
