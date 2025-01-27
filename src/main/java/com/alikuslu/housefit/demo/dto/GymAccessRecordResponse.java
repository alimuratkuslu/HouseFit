package com.alikuslu.housefit.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GymAccessRecordResponse {
    private Long id;
    private Long userId;
    private String accessType;
    private LocalDateTime timestamp;
    private Long locationId;
}
