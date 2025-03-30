package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer_packages")
public class CustomerPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private FitnessPackage fitnessPackage;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer remainingSessions;

    @Enumerated(EnumType.STRING)
    private PackageStatus status;

    private String notes;
}
