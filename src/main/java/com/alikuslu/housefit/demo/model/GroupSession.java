package com.alikuslu.housefit.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class GroupSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @ManyToMany
    @JoinTable(
            name = "group_session_participants",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private Set<SessionCheckIn> checkIns = new HashSet<>();

    private boolean checkinsProcessed = false;

    private Integer participantLimit;
    private Integer currentParticipants = 0;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.UPCOMING;

    @Version
    private Long version;
}
