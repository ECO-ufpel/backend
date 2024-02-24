package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.ecoufpel.ecoufpelapp.domains.user.User;

import java.util.ArrayList;
import java.util.List;

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
