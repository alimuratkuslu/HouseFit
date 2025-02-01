package com.alikuslu.housefit.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DietPlanRequest {
    private Long customerId;
    private Long trainerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<MealRequest> meals;
}
