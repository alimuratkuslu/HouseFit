package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.Appointment;
import com.alikuslu.housefit.demo.model.AppointmentStatus;
import com.alikuslu.housefit.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCustomerIdOrderByAppointmentTimeDesc(Long customerId);
    List<Appointment> findByTrainerOrderByAppointmentTimeDesc(User trainer);

    List<Appointment> findByTrainerAndStatusOrderByAppointmentTimeDesc(User trainer, AppointmentStatus status);

    List<Appointment> findByTrainerAndAppointmentTimeBetweenAndStatus(
            User trainer,
            LocalDateTime appointmentTimeStart,
            LocalDateTime appointmentTimeEnd,
            AppointmentStatus status
    );

    @Query("SELECT a FROM Appointment a WHERE " +
            "(a.trainer = :user OR a.customer = :user) AND " +
            "a.appointmentTime BETWEEN :startTime AND :endTime AND " +
            "a.status = :status " +
            "ORDER BY a.appointmentTime ASC")
    List<Appointment> findTodaysAcceptedSessionsOrderByTime(
            @Param("user") User user,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") AppointmentStatus status
    );

    @Query(value = "SELECT * FROM appointments a " +
            "WHERE (a.trainer_id = :#{#user.id} OR a.customer_id = :#{#user.id}) " +
            "AND a.appointment_time BETWEEN :currentTime AND :endTime " +
            "AND a.status = :#{#status.name()} " +
            "ORDER BY a.appointment_time ASC LIMIT 1", nativeQuery = true)
    Appointment findNextUpcomingSession(
            @Param("user") User user,
            @Param("currentTime") LocalDateTime currentTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") AppointmentStatus status
    );
}
