package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {

    List<WorkoutLog> findByUserOrderByDateDesc(User user);

    @Query("SELECT wl FROM WorkoutLog wl WHERE wl.user = ?1 AND wl.date BETWEEN ?2 AND ?3")
    List<WorkoutLog> findByUserAndDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
}
