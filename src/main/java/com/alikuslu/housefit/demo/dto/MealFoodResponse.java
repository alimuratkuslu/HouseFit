package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.MealFood;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class MealFoodResponse {
    private String foodId;
    private String label;
    private Double portion;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fats;

    public static MealFoodResponse fromEntity(MealFood food) {
        return new MealFoodResponse(
                food.getFoodId(),
                food.getLabel(),
                food.getPortion(),
                food.getCalories(),
                food.getProtein(),
                food.getCarbs(),
                food.getFats()
        );
    }
}
