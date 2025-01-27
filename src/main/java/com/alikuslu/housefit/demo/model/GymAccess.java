package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "gym_access")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GymAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccessType accessType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long locationId;

    public enum AccessType {
        ENTRY, EXIT
    }
}
