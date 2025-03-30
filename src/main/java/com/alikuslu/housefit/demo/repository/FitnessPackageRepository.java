package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.FitnessPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FitnessPackageRepository extends JpaRepository<FitnessPackage, Long> {
    List<FitnessPackage> findByActiveTrue();
    List<FitnessPackage> findByCreatedById(Long trainerId);
}
