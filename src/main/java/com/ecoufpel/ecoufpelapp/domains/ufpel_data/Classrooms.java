package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "classrooms", schema = "ufpel_data")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Classrooms
{
    @Id
    private String id;

}
