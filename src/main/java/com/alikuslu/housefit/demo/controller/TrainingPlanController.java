package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.TrainingPlanDto;
import com.alikuslu.housefit.demo.model.TrainingPlan;
import com.alikuslu.housefit.demo.service.TrainingPlanService;
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

    @PostMapping("/create")
    public ResponseEntity<TrainingPlan> createTrainingPlan(@RequestBody TrainingPlanDto dto) {
        return ResponseEntity.ok(trainingPlanService.createTrainingPlan(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingPlan> updateTrainingPlan(@PathVariable Long id, @RequestBody TrainingPlanDto dto) {
        return ResponseEntity.ok(trainingPlanService.updateTrainingPlan(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingPlan(@PathVariable Long id) {
        trainingPlanService.deleteTrainingPlan(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TrainingPlan>> getCustomerTrainingPlans(@PathVariable Long customerId) {
        return ResponseEntity.ok(trainingPlanService.getCustomerTrainingPlans(customerId));
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<TrainingPlan>> getTrainerTrainingPlans(@PathVariable Long trainerId) {
        return ResponseEntity.ok(trainingPlanService.getTrainerTrainingPlans(trainerId));
    }
}
