package com.alikuslu.housefit.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutRequest {
    private String name;
    private int dayOfWeek;
    private String description;
    private List<WorkoutExerciseRequest> exercises;
}
