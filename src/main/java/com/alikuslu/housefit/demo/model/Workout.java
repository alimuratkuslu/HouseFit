package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dayOfWeek;

    @ManyToOne
    private TrainingPlan trainingPlan;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workout")
    private List<Exercise> exercises;
}
