package com.alikuslu.housefit.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupSessionRequest {
    private String name;
    private String description;
    private String photoUrl;
    private Long trainerId;
    private Integer participantLimit;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
