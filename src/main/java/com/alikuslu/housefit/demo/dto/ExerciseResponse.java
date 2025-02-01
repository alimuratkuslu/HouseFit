package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ExerciseResponse {
    private Long id;
    private String name;
    private String muscleGroup;
    private String equipment;
    private String description;
    private String videoUrl;

    public static ExerciseResponse fromEntity(Exercise exercise) {
        return new ExerciseResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getMuscleGroup(),
                exercise.getEquipment(),
                exercise.getDescription(),
                exercise.getVideoUrl()
        );
    }
}
