package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.ExerciseLog;
import com.alikuslu.housefit.demo.model.Workout;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutLogRequest {
    private Workout workout;
    private List<ExerciseLog> exerciseLogs;
}
