package com.alikuslu.housefit.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@Entity
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MealType type;

    private LocalTime time;

    @ElementCollection
    @CollectionTable(name = "meal_items", joinColumns = @JoinColumn(name = "meal_id"))
    private List<MealFood> items;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_plan_id")
    private DietPlan dietPlan;
}
