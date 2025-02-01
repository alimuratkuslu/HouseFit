package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.DietPlanRequest;
import com.alikuslu.housefit.demo.dto.DietPlanResponse;
import com.alikuslu.housefit.demo.service.DietPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diet")
public class DietPlanController {

    private final DietPlanService dietPlanService;

    public DietPlanController(DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    @PostMapping
    public ResponseEntity<DietPlanResponse> createDietPlan(@RequestBody DietPlanRequest request) {
        DietPlanResponse response = dietPlanService.createDietPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<DietPlanResponse> getDietPlan(@PathVariable Long planId) {
        return ResponseEntity.ok(dietPlanService.getDietPlan(planId));
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<List<DietPlanResponse>> getCustomerDietPlans(@PathVariable Long customerId) {
        return ResponseEntity.ok(dietPlanService.getCustomerDietPlans(customerId));
    }
}
