package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.GroupSession;
import com.alikuslu.housefit.demo.model.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupSessionResponse {
    private Long id;
    private String name;
    private String description;
    private String photoUrl;
    private UserResponse trainer;
    private Integer participantLimit;
    private Integer currentParticipants;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SessionStatus status;
    private Set<UserResponse> participants;

    public static GroupSessionResponse fromEntity(GroupSession session) {
        return new GroupSessionResponse(
                session.getId(),
                session.getName(),
                session.getDescription(),
                session.getPhotoUrl(),
                UserResponse.fromEntity(session.getTrainer()),
                session.getParticipantLimit(),
                session.getCurrentParticipants(),
                session.getStartTime(),
                session.getEndTime(),
                session.getStatus(),
                session.getParticipants().stream()
                        .map(UserResponse::fromEntity)
                        .collect(Collectors.toSet())
        );
    }
}
