package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
