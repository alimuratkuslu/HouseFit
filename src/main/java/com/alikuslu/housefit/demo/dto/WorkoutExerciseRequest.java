package com.alikuslu.housefit.demo.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class WorkoutExerciseRequest {
    @NotNull
    private Long exerciseId;
    private int sets;
    private int repetitions;
    private Integer targetWeight;
    private int restSeconds;
    private String notes;
}
