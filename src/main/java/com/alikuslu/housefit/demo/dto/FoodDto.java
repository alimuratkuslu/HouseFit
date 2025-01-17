package com.alikuslu.housefit.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoodDto {

    private String foodId;
    private String label;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fats;
    private Double fiber;
}
