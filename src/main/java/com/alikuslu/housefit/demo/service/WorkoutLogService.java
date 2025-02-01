package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.model.*;
import com.alikuslu.housefit.demo.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;

    public WorkoutLogService(WorkoutLogRepository workoutLogRepository) {
        this.workoutLogRepository = workoutLogRepository;
    }

    public WorkoutLog logWorkout(User user, Workout workout, List<ExerciseLog> exerciseLogs) {
        WorkoutLog workoutLog = new WorkoutLog();
        workoutLog.setUser(user);
        workoutLog.setWorkout(workout);
        workoutLog.setDate(LocalDateTime.now());
        workoutLog.setExerciseLogs(exerciseLogs);

        exerciseLogs.forEach(log -> log.setWorkoutLog(workoutLog));

        return workoutLogRepository.save(workoutLog);
    }

    public List<WorkoutLog> getUserWorkoutHistory(User user) {
        return workoutLogRepository.findByUserOrderByDateDesc(user);
    }

    public List<WorkoutLog> getUserWorkoutHistoryBetweenDates(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return workoutLogRepository.findByUserAndDateBetween(user, startDate, endDate);
    }

    public Map<String, List<ExerciseLog>> getExerciseProgressionData(User user, Exercise exercise) {
        return getUserWorkoutHistory(user).stream()
                .flatMap(log -> log.getExerciseLogs().stream())
                .filter(log -> log.getExercise().getId().equals(exercise.getId()))
                .collect(Collectors.groupingBy(log -> log.getWorkoutLog().getDate().toLocalDate().toString()));
    }
}
