package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.TrainingPlan;
import com.alikuslu.housefit.demo.model.Workout;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {

    @EntityGraph(attributePaths = {"workouts"})
    Optional<TrainingPlan> findByCustomerId(Long customerId);

    @Query("SELECT tp FROM TrainingPlan tp " +
            "JOIN FETCH tp.workouts w " +
            "JOIN FETCH w.workoutExercises we " +
            "WHERE tp.id = :id")
    Optional<TrainingPlan> findFullPlanById(@Param("id") Long id);

    List<TrainingPlan> findByTrainerId(Long trainerId);
}
