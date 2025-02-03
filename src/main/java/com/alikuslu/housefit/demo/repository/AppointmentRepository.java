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

    List<Appointment> findByCustomerAndAppointmentTimeBetween(User customer, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByTrainerAndAppointmentTimeBetween(User trainer, LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM Appointment a WHERE " +
            "(a.trainer = :user OR a.customer = :user) AND " +
            "a.appointmentTime BETWEEN :start AND :end " +
            "ORDER BY a.appointmentTime ASC")
    List<Appointment> findTodaysAppointmentsForUser(@Param("user") User user,
                                                    @Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);
}
