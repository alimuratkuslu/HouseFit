package com.alikuslu.housefit.demo.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.util.List;

@Data
public class TrainingPlanRequest {
    @NotNull
    private Long customerId;

    @NotNull
    private Long trainerId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String intensity;

    private String goal;

    private List<WorkoutRequest> workouts;
}
