package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fitness_packages")
public class FitnessPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    private Integer duration;

    private Integer totalSessions;

    private String features;

    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
