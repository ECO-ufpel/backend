package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "courses", schema = "ufpel_data")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Courses
{
    @Id
    private String id;
    private String title;
}
