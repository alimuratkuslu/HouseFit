package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    private int sets;
    private int repetitions;
    private Integer targetWeight;
    private int restSeconds;
    private String notes;
}
