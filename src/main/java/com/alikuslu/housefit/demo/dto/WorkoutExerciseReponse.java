package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.WorkoutExercise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class WorkoutExerciseResponse {
    private Long id;
    private ExerciseResponse exercise;
    private int sets;
    private int repetitions;
    private Integer targetWeight;
    private int restSeconds;
    private String notes;

    public static WorkoutExerciseResponse fromEntity(WorkoutExercise we) {
        return new WorkoutExerciseResponse(
                we.getId(),
                ExerciseResponse.fromEntity(we.getExercise()),
                we.getSets(),
                we.getRepetitions(),
                we.getTargetWeight(),
                we.getRestSeconds(),
                we.getNotes()
        );
    }
}
