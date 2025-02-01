package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.Appointment;
import com.alikuslu.housefit.demo.model.AppointmentStatus;
import com.alikuslu.housefit.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCustomerIdOrderByAppointmentTimeDesc(Long customerId);
    List<Appointment> findByTrainerOrderByAppointmentTimeDesc(User trainer);

    List<Appointment> findByTrainerAndStatusOrderByAppointmentTimeDesc(User trainer, AppointmentStatus status);
}
