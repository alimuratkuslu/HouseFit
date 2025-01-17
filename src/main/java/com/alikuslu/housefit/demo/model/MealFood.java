package com.alikuslu.housefit.demo.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class MealFood {
    private String foodId;
    private String label;
    private Double portion;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fats;
}
