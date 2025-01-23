package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    List<TrainingPlan> findByCustomerId(Long customerId);
    List<TrainingPlan> findByTrainerId(Long trainerId);
}
