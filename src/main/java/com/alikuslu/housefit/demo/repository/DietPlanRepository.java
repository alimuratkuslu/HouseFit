package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {

    Optional<DietPlan> findByCustomerId(Long customerId);
}
