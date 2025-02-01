package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.GroupSession;
import com.alikuslu.housefit.demo.model.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupSessionRepository extends JpaRepository<GroupSession, Long> {

    @EntityGraph(attributePaths = {"trainer", "participants"})
    Page<GroupSession> findByStatus(SessionStatus status, Pageable pageable);

    @Query("SELECT gs FROM GroupSession gs " +
            "WHERE gs.startTime BETWEEN :start AND :end " +
            "ORDER BY gs.startTime ASC")
    List<GroupSession> findBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT gs FROM GroupSession gs " +
            "WHERE gs.startTime BETWEEN :start AND :end " +
            "AND gs.checkinsProcessed = false")
    List<GroupSession> findByStartTimeBetweenAndCheckinsProcessedFalse(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT gs FROM GroupSession gs " +
            "JOIN gs.participants p " +
            "WHERE p.id = :userId")
    Page<GroupSession> findByParticipantId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT gs FROM GroupSession gs " +
            "WHERE gs.trainer.id = :trainerId")
    Page<GroupSession> findByTrainerId(@Param("trainerId") Long trainerId, Pageable pageable);
}
