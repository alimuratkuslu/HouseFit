package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.TrainingPlanRequest;
import com.alikuslu.housefit.demo.dto.TrainingPlanResponse;
import com.alikuslu.housefit.demo.model.*;
import com.alikuslu.housefit.demo.repository.ExerciseRepository;
import com.alikuslu.housefit.demo.repository.TrainingPlanRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import com.alikuslu.housefit.demo.repository.WorkoutRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;

    public TrainingPlanService(TrainingPlanRepository trainingPlanRepository, UserRepository userRepository, ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
    }

    @Transactional
    public TrainingPlanResponse createOrUpdatePlan(TrainingPlanRequest request) {
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User trainer = userRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        TrainingPlan plan = new TrainingPlan();
        plan.setCustomer(customer);
        plan.setTrainer(trainer);
        plan.setStartDate(request.getStartDate());
        plan.setEndDate(request.getEndDate());
        plan.setIntensity(request.getIntensity());
        plan.setGoal(request.getGoal());

        request.getWorkouts().forEach(workoutRequest -> {
            Workout workout = new Workout();
            workout.setName(workoutRequest.getName());
            workout.setDayOfWeek(workoutRequest.getDayOfWeek());
            workout.setDescription(workoutRequest.getDescription());
            workout.setTrainingPlan(plan);

            workoutRequest.getExercises().forEach(exerciseRequest -> {
                Exercise exercise = exerciseRepository.findById(exerciseRequest.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found"));

                WorkoutExercise workoutExercise = new WorkoutExercise();
                workoutExercise.setExercise(exercise);
                workoutExercise.setSets(exerciseRequest.getSets());
                workoutExercise.setRepetitions(exerciseRequest.getRepetitions());
                workoutExercise.setTargetWeight(exerciseRequest.getTargetWeight());
                workoutExercise.setRestSeconds(exerciseRequest.getRestSeconds());
                workoutExercise.setNotes(exerciseRequest.getNotes());
                workoutExercise.setWorkout(workout);

                workout.getWorkoutExercises().add(workoutExercise);
            });

            plan.getWorkouts().add(workout);
        });

        TrainingPlan savedPlan = trainingPlanRepository.save(plan);
        return convertToResponse(savedPlan);
    }

    public TrainingPlanResponse getTrainingPlanById(Long planId) {
        TrainingPlan plan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Training plan not found with ID: " + planId));

        return convertToResponse(plan);
    }

    public TrainingPlanResponse getCustomerTrainingPlan(Long customerId) {
        TrainingPlan plan = trainingPlanRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Training plan not found for customer ID: " + customerId));

        List<Workout> workouts = workoutRepository.findWorkoutsWithExercisesByPlanId(plan.getId());

        return TrainingPlanResponse.fromEntity(plan, workouts);
    }

    public void deleteTrainingPlan(Long id) {
        trainingPlanRepository.deleteById(id);
    }

    public List<TrainingPlan> getTrainerTrainingPlans(Long trainerId) {
        return trainingPlanRepository.findByTrainerId(trainerId);
    }

    private TrainingPlanResponse convertToResponse(TrainingPlan plan) {
        List<Workout> workouts = workoutRepository.findByTrainingPlanIdWithExercises(plan.getId());

        return TrainingPlanResponse.fromEntity(plan, workouts);
    }
}
