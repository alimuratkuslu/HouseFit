package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
@Entity
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    private LocalDate startDate;
    private LocalDate endDate;
    private String intensity;
    private String goal;

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workout> workouts = new ArrayList<>();
}
