package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.RequestAppointmentDto;
import com.alikuslu.housefit.demo.model.*;
import com.alikuslu.housefit.demo.repository.AppointmentRepository;
import com.alikuslu.housefit.demo.repository.AvailabilityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AvailabilityService availabilityService;
    private final UserService userService;
    private final AvailabilityRepository availabilityRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, AvailabilityService availabilityService, UserService userService, AvailabilityRepository availabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityService = availabilityService;
        this.userService = userService;
        this.availabilityRepository = availabilityRepository;
    }

    public Appointment acceptAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.ACCEPTED);
        return appointmentRepository.save(appointment);
    }

    public Appointment declineAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Availability availability = appointment.getAvailability();
        if (availability != null) {
            availability.setIsBooked(false);
            availabilityRepository.save(availability);
        }

        appointment.setStatus(AppointmentStatus.DECLINED);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getTrainerAppointments() {
        String trainerUsername = getLoggedInUsername();
        User trainer = userService.findByUsername(trainerUsername);
        return appointmentRepository.findByTrainerOrderByAppointmentTimeDesc(trainer);
    }

    public List<Appointment> getTodaysSessions() {
        String username = getLoggedInUsername();
        User user = userService.findByUsername(username);

        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        return appointmentRepository.findTodaysAcceptedSessionsOrderByTime(
                user,
                startOfDay,
                endOfDay,
                AppointmentStatus.ACCEPTED
        );
    }

    public Appointment getNextUpcomingSession() {
        String username = getLoggedInUsername();
        User user = userService.findByUsername(username);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        return appointmentRepository.findNextUpcomingSession(
                user,
                now,
                endOfDay,
                AppointmentStatus.ACCEPTED
        );
    }

    public Appointment completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        String username = getLoggedInUsername();
        User user = userService.findByUsername(username);

        if (appointment.getStatus() != AppointmentStatus.ACCEPTED) {
            throw new RuntimeException("Only accepted appointments can be marked as completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getTrainerPendingAppointments() {
        String trainerUsername = getLoggedInUsername();
        User trainer = userService.findByUsername(trainerUsername);
        return appointmentRepository.findByTrainerAndStatusOrderByAppointmentTimeDesc(trainer, AppointmentStatus.PENDING);
    }

    public List<Appointment> getAppointmentsOfSessionUser() {
        String username = getLoggedInUsername();
        User customer = userService.findByUsername(username);

        return appointmentRepository.findByCustomerIdOrderByAppointmentTimeDesc(customer.getId());
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment requestAppointment(RequestAppointmentDto requestAppointmentDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime appointmentTime = LocalDateTime.parse(requestAppointmentDto.getDate(), formatter);

        String customerUsername = getLoggedInUsername();
        User customer = userService.findByUsername(customerUsername);
        User trainer = userService.findById(requestAppointmentDto.getTrainerId());

        List<Availability> availabilities = availabilityService.getAvailability(requestAppointmentDto.getTrainerId());
        Optional<Availability> matchingAvailability = availabilities.stream()
                .filter(a -> !a.getIsBooked() &&
                        !appointmentTime.isBefore(a.getStartTime()) &&
                        !appointmentTime.isAfter(a.getEndTime()))
                .findFirst();

        if (matchingAvailability.isEmpty()) {
            throw new RuntimeException("Trainer is not available at the requested time");
        }

        Availability availability = matchingAvailability.get();
        availability.setIsBooked(true);
        availabilityRepository.save(availability);

        Appointment appointmentRequest = Appointment.builder()
                .customer(customer)
                .trainer(trainer)
                .appointmentTime(appointmentTime)
                .message(requestAppointmentDto.getMessage())
                .status(AppointmentStatus.PENDING)
                .availability(availability)
                .build();

        return appointmentRepository.save(appointmentRequest);
    }

    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
