package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.UpdateTrainerTargetsRequest;
import com.alikuslu.housefit.demo.model.Appointment;
import com.alikuslu.housefit.demo.model.AppointmentStatus;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import com.alikuslu.housefit.demo.repository.AppointmentRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public AdminService(UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public User updateTrainerTargets(UpdateTrainerTargetsRequest request) {
        User trainer = userRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        if (trainer.getUserType() != UserType.TRAINER) {
            throw new IllegalArgumentException("User is not a trainer");
        }

        trainer.setTargetSessions(request.getTargetSessions());
        trainer.setTargetPoints(request.getTargetPoints());
        return userRepository.save(trainer);
    }

    public Map<String, Object> getTrainerMonthlyProgress(Long trainerId) {
        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().withDayOfMonth(1).plusMonths(1).with(LocalTime.MIN);

        List<Appointment> monthlySessions = appointmentRepository.findByTrainerAndAppointmentTimeBetweenAndStatus(
                trainer, start, end, AppointmentStatus.COMPLETED
        );

        return Map.of(
                "targetSessions", trainer.getTargetSessions(),
                "completedSessions", monthlySessions.size(),
                "targetPoints", trainer.getTargetPoints()
        );
    }
}
