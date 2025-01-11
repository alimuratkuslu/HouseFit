package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.PointsLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointsLogRepository extends JpaRepository<PointsLog, Long> {
    List<PointsLog> findByTrainerId(Long trainerId);
}
