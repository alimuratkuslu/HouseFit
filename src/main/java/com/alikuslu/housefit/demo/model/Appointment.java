package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    private User customer;

    @ManyToOne
    private User trainer;

    private LocalDateTime appointmentTime;

    private String message;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @OneToOne
    @JoinColumn(name = "availability_id")
    private Availability availability;
}
