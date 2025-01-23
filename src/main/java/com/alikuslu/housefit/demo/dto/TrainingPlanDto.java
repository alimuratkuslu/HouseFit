package com.alikuslu.housefit.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TrainingPlanDto {
    private Long customerId;
    private Long trainerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<WorkoutDto> workouts;
}
