package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String muscleGroup;
    private String equipment;
    private String description;
    private String videoUrl;
}
