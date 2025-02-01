package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.TrainingPlanRequest;
import com.alikuslu.housefit.demo.dto.TrainingPlanResponse;
import com.alikuslu.housefit.demo.model.TrainingPlan;
import com.alikuslu.housefit.demo.service.TrainingPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    @PostMapping
    public ResponseEntity<TrainingPlanResponse> createOrUpdatePlan(@RequestBody TrainingPlanRequest request) {
        TrainingPlanResponse response = trainingPlanService.createOrUpdatePlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<TrainingPlanResponse> getCustomerPlan(@PathVariable Long customerId) {
        TrainingPlanResponse response = trainingPlanService.getCustomerTrainingPlan(customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<TrainingPlanResponse> getPlanById(@PathVariable Long planId) {
        TrainingPlanResponse response = trainingPlanService.getTrainingPlanById(planId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<TrainingPlan>> getTrainerTrainingPlans(@PathVariable Long trainerId) {
        return ResponseEntity.ok(trainingPlanService.getTrainerTrainingPlans(trainerId));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long planId) {
        trainingPlanService.deleteTrainingPlan(planId);
        return ResponseEntity.noContent().build();
    }
}
