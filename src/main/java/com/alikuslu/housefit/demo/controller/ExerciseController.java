package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.model.Exercise;
import com.alikuslu.housefit.demo.repository.ExerciseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;

    public ExerciseController(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        return ResponseEntity.ok(exerciseRepository.findAll());
    }
}
