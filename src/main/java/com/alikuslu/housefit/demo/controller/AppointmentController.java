package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.RequestAppointmentDto;
import com.alikuslu.housefit.demo.model.Appointment;
import com.alikuslu.housefit.demo.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<Appointment>> getAllAppointmentsOfSessionUser() {
        return ResponseEntity.ok(appointmentService.getAppointmentsOfSessionUser());
    }

    @GetMapping("/trainer-appointments")
    public ResponseEntity<List<Appointment>> getTrainerAppointments() {
        List<Appointment> appointments = appointmentService.getTrainerAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{appointmentId}/accept")
    public ResponseEntity<Appointment> acceptAppointment(@PathVariable Long appointmentId) {
        Appointment updatedAppointment = appointmentService.acceptAppointment(appointmentId);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PutMapping("/{appointmentId}/decline")
    public ResponseEntity<Appointment> declineAppointment(@PathVariable Long appointmentId) {
        Appointment updatedAppointment = appointmentService.declineAppointment(appointmentId);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PostMapping
    public ResponseEntity<Appointment> requestAppointment(@RequestBody RequestAppointmentDto requestAppointmentDto) {
        Appointment appointmentRequest = appointmentService.requestAppointment(requestAppointmentDto);
        return ResponseEntity.ok(appointmentRequest);
    }
}
