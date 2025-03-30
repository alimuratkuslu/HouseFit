package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer_reports")
public class CustomerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileKey;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    private String title;
    private String description;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long trainerId;

    @Column(nullable = false)
    private String contentType;

    private Long fileSize;
}
