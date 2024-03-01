package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.sensor.DailyAverageConsumption;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumption;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataConsumptionRepository extends JpaRepository<DataConsumption, DataConsumptionID> {
    Optional<List<DataConsumption>> findById_DateTimeBetween(Timestamp startTimestamp, Timestamp endTimestamp);
    @Query("""
        SELECT new com.ecoufpel.ecoufpelapp.domains.sensor.DailyAverageConsumption(
            MIN(c.id.dateTime) AS date, AVG(c.consumption) AS avgConsumption)
        FROM DataConsumption c
        WHERE c.id.classroomId = :classroomId AND c.id.dateTime BETWEEN :start AND :end
        GROUP BY DATE(c.id.dateTime)
    """)
    List<DailyAverageConsumption> getDailyAverageConsumption(
            @Param("classroomId") String classroomId,
            @Param("start") Timestamp start,
            @Param("end") Timestamp end);
}
