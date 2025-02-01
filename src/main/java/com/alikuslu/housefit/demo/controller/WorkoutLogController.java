package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.WorkoutLogRequest;
import com.alikuslu.housefit.demo.model.Exercise;
import com.alikuslu.housefit.demo.model.ExerciseLog;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.WorkoutLog;
import com.alikuslu.housefit.demo.service.WorkoutLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workout-logs")
public class WorkoutLogController {

    private final WorkoutLogService workoutLogService;

    public WorkoutLogController(WorkoutLogService workoutLogService) {
        this.workoutLogService = workoutLogService;
    }

    @PostMapping
    public ResponseEntity<WorkoutLog> logWorkout(@RequestBody WorkoutLogRequest request, @RequestAttribute User currentUser) {
        WorkoutLog log = workoutLogService.logWorkout(
                currentUser,
                request.getWorkout(),
                request.getExerciseLogs()
        );
        return ResponseEntity.ok(log);
    }

    @GetMapping("/history")
    public ResponseEntity<List<WorkoutLog>> getWorkoutHistory(@RequestAttribute User currentUser) {
        return ResponseEntity.ok(workoutLogService.getUserWorkoutHistory(currentUser));
    }

    @GetMapping("/history/date-range")
    public ResponseEntity<List<WorkoutLog>> getWorkoutHistoryBetweenDates(
            @RequestAttribute User currentUser,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(
                workoutLogService.getUserWorkoutHistoryBetweenDates(currentUser, startDate, endDate)
        );
    }

    @GetMapping("/progress/{exerciseId}")
    public ResponseEntity<Map<String, List<ExerciseLog>>> getExerciseProgress(
            @RequestAttribute User currentUser,
            @PathVariable Long exerciseId) {
        Exercise exercise = new Exercise();
        exercise.setId(exerciseId);
        return ResponseEntity.ok(
                workoutLogService.getExerciseProgressionData(currentUser, exercise)
        );
    }
}
