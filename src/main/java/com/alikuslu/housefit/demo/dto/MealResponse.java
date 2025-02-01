package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.Meal;
import com.alikuslu.housefit.demo.model.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class MealResponse {
    private Long id;
    private MealType type;
    private LocalTime time;
    private List<MealFoodResponse> items;

    public static MealResponse fromEntity(Meal meal) {
        return new MealResponse(
                meal.getId(),
                meal.getType(),
                meal.getTime(),
                meal.getItems().stream()
                        .map(MealFoodResponse::fromEntity)
                        .toList()
        );
    }
}
