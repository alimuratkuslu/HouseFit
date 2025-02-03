package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Builder
@Table(name = "users")
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    private String username;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String referralCode;
    private String referredBy;
    private Double points = 0.0;
    private String avatar;

    private String passwordResetCode;
    private LocalDateTime passwordResetCodeExpiresAt;

    private Double weight;
    private Double height;
    private Double bodyFat;
    private Double muscleMass;

    @Column(name = "target_sessions")
    private Integer targetSessions;

    @Column(name = "target_points")
    private Double targetPoints;

    private Boolean emailNotifications = true;
    private Boolean pushNotifications = true;
    private String reminderTime;

    public void setTargetSessions(Integer targetSessions) {
        if (this.userType == UserType.TRAINER) {
            this.targetSessions = targetSessions;
        }
    }

    public void setTargetPoints(Double targetPoints) {
        if (this.userType == UserType.TRAINER) {
            this.targetPoints = targetPoints;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
