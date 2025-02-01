package com.alikuslu.housefit.demo.dto;

import lombok.Data;

@Data
public class MealFoodRequest {
    private String foodId;
    private String label;
    private Double portion;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fats;
}
