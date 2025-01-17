package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.Meal;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DietPlanDto {
    private Long customerId;
    private Long trainerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Meal> meals;
}
