package com.alikuslu.housefit.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutDto {
    private String name;
    private String dayOfWeek;
    private List<ExerciseDto> exercises;
}
