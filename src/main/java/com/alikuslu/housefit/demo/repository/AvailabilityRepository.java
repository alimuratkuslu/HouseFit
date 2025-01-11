package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByTrainerId(Long trainerId);
    List<Availability> findByTrainerIdAndStartTimeBetweenAndIsBookedFalse(
            Long trainerId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
}
