package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.DietPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {

    @EntityGraph(attributePaths = {"meals"})
    List<DietPlan> findByCustomerId(Long customerId);

    @Query("SELECT dp FROM DietPlan dp " +
            "LEFT JOIN FETCH dp.meals m " +
            "WHERE dp.id = :id")
    Optional<DietPlan> findFullPlanById(@Param("id") Long id);
}
