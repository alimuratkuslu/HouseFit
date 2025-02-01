package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.DietPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietPlanResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<MealResponse> meals;

    public static DietPlanResponse fromEntity(DietPlan plan) {
        return new DietPlanResponse(
                plan.getId(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.getMeals().stream()
                        .map(MealResponse::fromEntity)
                        .toList()
        );
    }
}
