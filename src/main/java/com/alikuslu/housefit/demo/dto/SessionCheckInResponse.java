package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.CheckInStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionCheckInResponse {
    private Long id;
    private Long sessionId;
    private String sessionName;
    private LocalDateTime sessionTime;
    private CheckInStatus status;
    private LocalDateTime respondedAt;
}
