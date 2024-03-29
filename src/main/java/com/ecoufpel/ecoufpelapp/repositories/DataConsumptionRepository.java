package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.sensor.DailyAverageConsumption;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumption;
import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumptionID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataConsumptionRepository extends JpaRepository<DataConsumption, DataConsumptionID> {
    Optional<List<DataConsumption>> findById_DateTimeBetween(Timestamp startTimestamp, Timestamp endTimestamp);
    @Query("""
        SELECT new com.ecoufpel.ecoufpelapp.domains.sensor.DailyAverageConsumption(
            MIN(c.aggregationDate) AS date, AVG(c.avgConsumption) AS avgConsumption)
        FROM ClassroomDataAggregation c
        WHERE c.classroomId = :classroomId AND c.aggregationDate BETWEEN :start AND :end
        GROUP BY DATE(c.aggregationDate)
    """)
    List<DailyAverageConsumption> getDailyAverageConsumption(
            @Param("classroomId") String classroomId,
            @Param("start") Timestamp start,
            @Param("end") Timestamp end);

    @Query("""
        SELECT c 
        FROM DataConsumption c
        WHERE c.id.classroomId = :classroomId AND c.id.dateTime BETWEEN :startDateTime  AND :endDateTime
    """)
    List<DataConsumption> getLastHourConsumptionByRoomId(
            @Param("classroomId") String classroomId,
            @Param("startDateTime") Timestamp startDateTime,
            @Param("endDateTime") Timestamp endDateTime);
}
