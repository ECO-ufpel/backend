package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumption;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataConsumptionRepository extends JpaRepository<DataConsumption, DataConsumptionID> {
    Optional<List<DataConsumption>> findById_DateTimeBetween(Timestamp startTimestamp, Timestamp endTimestamp);
}
