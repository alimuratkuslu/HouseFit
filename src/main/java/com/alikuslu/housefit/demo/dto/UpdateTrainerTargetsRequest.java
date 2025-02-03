package com.alikuslu.housefit.demo.dto;

import lombok.Data;

import java.time.YearMonth;

@Data
public class UpdateTrainerTargetsRequest {
    private Long trainerId;
    private Integer targetSessions;
    private Double targetPoints;
    private YearMonth targetMonth;
}
