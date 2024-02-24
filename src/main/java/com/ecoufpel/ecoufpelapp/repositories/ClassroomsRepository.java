package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.ufpel_data.Classrooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomsRepository extends JpaRepository<Classrooms, String> {
    Optional<Classrooms> findById(String id);

    @Query("""
    SELECT ca FROM Classrooms AS ca
    JOIN CourseInRoom AS cir ON cir.id.classroomId = ca.id
    JOIN TimeIntervals AS interval ON interval.id = cir.id.interval
    JOIN UserInCourse AS uic ON uic.id.courseId = cir.courseId
    WHERE uic.id.userCpf = ?1
    AND LOCALTIME() BETWEEN interval.startTime AND interval.endTime""")
    Optional<Classrooms> findByUserCpf(String userCpf);
}
