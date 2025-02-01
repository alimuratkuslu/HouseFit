package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.Workout;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class WorkoutResponse {
    private Long id;
    private String name;
    private int dayOfWeek;
    private String description;
    private List<WorkoutExerciseResponse> exercises;

    public static WorkoutResponse fromEntity(Workout workout) {
        return new WorkoutResponse(
                workout.getId(),
                workout.getName(),
                workout.getDayOfWeek(),
                workout.getDescription(),
                workout.getWorkoutExercises().stream()
                        .map(WorkoutExerciseResponse::fromEntity)
                        .toList()
        );
    }
}
