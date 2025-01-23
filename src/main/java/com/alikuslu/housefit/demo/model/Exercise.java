package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer sets;
    private Integer repetitions;
    private String notes;

    @ManyToOne
    private Workout workout;
}
