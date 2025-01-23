package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.TrainingPlanDto;
import com.alikuslu.housefit.demo.model.TrainingPlan;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.repository.TrainingPlanRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final UserRepository userRepository;

    public TrainingPlanService(TrainingPlanRepository trainingPlanRepository, UserRepository userRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TrainingPlan createTrainingPlan(TrainingPlanDto dto) {
        User customer = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        User trainer = userRepository.findById(dto.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setCustomer(customer);
        trainingPlan.setTrainer(trainer);
        trainingPlan.setStartDate(dto.getStartDate());
        trainingPlan.setEndDate(dto.getEndDate());
        // Map workouts and exercises
        // Add mapping logic here

        return trainingPlanRepository.save(trainingPlan);
    }

    public TrainingPlan updateTrainingPlan(Long id, TrainingPlanDto dto) {
        TrainingPlan existingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training plan not found"));

        // Update the plan details
        // Add update logic here

        return trainingPlanRepository.save(existingPlan);
    }

    public void deleteTrainingPlan(Long id) {
        trainingPlanRepository.deleteById(id);
    }

    public List<TrainingPlan> getCustomerTrainingPlans(Long customerId) {
        return trainingPlanRepository.findByCustomerId(customerId);
    }

    public List<TrainingPlan> getTrainerTrainingPlans(Long trainerId) {
        return trainingPlanRepository.findByTrainerId(trainerId);
    }
}
