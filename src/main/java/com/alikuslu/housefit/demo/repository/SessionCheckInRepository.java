package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.CheckInStatus;
import com.alikuslu.housefit.demo.model.SessionCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessionCheckInRepository extends JpaRepository<SessionCheckIn, Long> {

    List<SessionCheckIn> findByUserIdAndStatus(Long userId, CheckInStatus status);

    @Query("SELECT ci FROM SessionCheckIn ci " +
            "WHERE ci.session.id = :sessionId " +
            "AND ci.status = 'PENDING' " +
            "AND ci.session.startTime > CURRENT_TIMESTAMP")
    List<SessionCheckIn> findPendingCheckInsForSession(@Param("sessionId") Long sessionId);

    @Query("SELECT ci FROM SessionCheckIn ci " +
            "WHERE ci.user.id = :userId " +
            "AND ci.status = 'PENDING' " +
            "AND ci.session.startTime > CURRENT_TIMESTAMP")
    List<SessionCheckIn> findActivePendingCheckIns(@Param("userId") Long userId);
}
