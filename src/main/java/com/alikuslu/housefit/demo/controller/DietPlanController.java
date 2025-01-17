package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.DietPlanDto;
import com.alikuslu.housefit.demo.model.DietPlan;
import com.alikuslu.housefit.demo.service.DietPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diet")
public class DietPlanController {

    private final DietPlanService dietPlanService;

    public DietPlanController(DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    @PostMapping("/create")
    public ResponseEntity<DietPlan> createDietPlan(@RequestBody DietPlanDto dto) {
        return ResponseEntity.ok(dietPlanService.createDietPlan(dto));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<DietPlan> getDietPlan(@PathVariable Long customerId) {
        return ResponseEntity.ok(dietPlanService.getDietPlanByUserId(customerId));
    }
}
