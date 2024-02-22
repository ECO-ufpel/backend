package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "course_in_room", schema = "ufpel_data")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInRoom {
    @EmbeddedId
    private CourseInRoomID id;
    private String courseId;
}
