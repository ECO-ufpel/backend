package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_in_course", schema = "ufpel_data")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UserInCourse {
    @EmbeddedId
    private UserInCourseID id;
}
