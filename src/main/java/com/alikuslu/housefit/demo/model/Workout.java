package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Data
@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int dayOfWeek;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_plan_id", nullable = false)
    private TrainingPlan trainingPlan;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();
}
