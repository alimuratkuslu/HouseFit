package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class WorkoutLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Workout workout;

    private LocalDateTime date;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workoutLog")
    private List<ExerciseLog> exerciseLogs;
}
