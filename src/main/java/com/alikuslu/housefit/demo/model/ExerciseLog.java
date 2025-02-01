package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exercise exercise;

    @ManyToOne
    private WorkoutLog workoutLog;

    private Integer setNumber;
    private Integer reps;
    private Double weight;
}