package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    @Query("SELECT w FROM Workout w " +
            "LEFT JOIN FETCH w.workoutExercises we " +
            "LEFT JOIN FETCH we.exercise " +
            "WHERE w.trainingPlan.id = :planId")
    List<Workout> findByTrainingPlanIdWithExercises(@Param("planId") Long planId);

    @Query("SELECT w FROM Workout w " +
            "LEFT JOIN FETCH w.workoutExercises we " +
            "LEFT JOIN FETCH we.exercise " +
            "WHERE w.trainingPlan.id = :planId")
    List<Workout> findWorkoutsWithExercisesByPlanId(@Param("planId") Long planId);
}
