package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.MealType;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class MealDto {
    private MealType type;
    private LocalTime time;
    private List<MealFoodDto> foods;
}
