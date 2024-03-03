package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.ufpel_data.Classrooms;
import com.ecoufpel.ecoufpelapp.domains.ufpel_data.CourseChangeDTO;
import com.ecoufpel.ecoufpelapp.domains.ufpel_data.Courses;
import com.ecoufpel.ecoufpelapp.domains.ufpel_data.TimeIntervals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, String> {

@Query("""
    SELECT ti FROM TimeIntervals AS ti
    JOIN CourseInRoom AS cir ON cir.id.interval = ti.id
    JOIN Classrooms AS class ON cir.id.classroomId = class.id
    JOIN UserInCourse AS uic ON uic.id.courseId = cir.courseId
    WHERE uic.id.userCpf = ?1
    AND LOCALTIME() BETWEEN ti.startTime AND ti.endTime""")
    public Optional<TimeIntervals> findTimeIntervalsByUserCpf(String userCpf);

@Query("""
    SELECT new com.ecoufpel.ecoufpelapp.domains.ufpel_data.CourseChangeDTO(
            cour.title AS currentActivity, class.id AS classroomId, cour.id AS courseId, ?1 AS userCpf)
    FROM Classrooms as class
    JOIN CourseInRoom AS cir ON cir.id.classroomId = class.id
    JOIN Courses AS cour ON cir.courseId = cour.id
    JOIN TimeIntervals AS ti ON cir.id.interval = ti.id
    JOIN UserInCourse AS uic ON uic.id.courseId = cir.courseId
    WHERE uic.id.userCpf = ?1
    AND LOCALTIME() BETWEEN ti.startTime AND ti.endTime""")
    public Optional<CourseChangeDTO> findCurrentCourseByUserCpf(String userCpf);

@Query("""
    SELECT ti FROM TimeIntervals AS ti
    JOIN CourseInRoom AS cir ON cir.id.interval = ti.id
    JOIN Classrooms AS class ON cir.id.classroomId = class.id
    JOIN UserInCourse AS uic ON uic.id.courseId = cir.courseId
    WHERE uic.id.userCpf = ?1
    AND ti.startTime > LOCALTIME()
    ORDER BY ti.startTime ASC 
    LIMIT 1""")
    public Optional<TimeIntervals> findNextTimeIntervalsByUserCpf(String userCpf);
}
