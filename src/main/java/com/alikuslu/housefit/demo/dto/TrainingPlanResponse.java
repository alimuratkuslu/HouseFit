package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.TrainingPlan;
import com.alikuslu.housefit.demo.model.Workout;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingPlanResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String intensity;
    private String goal;
    private List<WorkoutResponse> workouts;

    public static TrainingPlanResponse fromEntity(TrainingPlan plan, List<Workout> workouts) {
        return new TrainingPlanResponse(
                plan.getId(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.getIntensity(),
                plan.getGoal(),
                workouts.stream()
                        .map(WorkoutResponse::fromEntity)
                        .toList()
        );
    }
}
