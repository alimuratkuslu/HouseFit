package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.GymAccess;
import com.alikuslu.housefit.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GymAccessRepository extends JpaRepository<GymAccess, Long> {

    List<GymAccess> findByUserOrderByTimestampDesc(User user);

    Optional<GymAccess> findFirstByUserOrderByTimestampDesc(User user);

    List<GymAccess> findByUserAndTimestampBetweenOrderByTimestampDesc(
            User user,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
}
