package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.LogEffortDto;
import com.alikuslu.housefit.demo.model.PointsLog;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.repository.PointsLogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PointsLogService {

    private final PointsLogRepository pointsLogRepository;
    private final UserService userService;

    public PointsLogService(PointsLogRepository pointsLogRepository, UserService userService) {
        this.pointsLogRepository = pointsLogRepository;
        this.userService = userService;
    }

    public static final Map<String, Double> POINTS_MAP = new HashMap<>();

    static {
        POINTS_MAP.put("Private lesson", 0.5);
        POINTS_MAP.put("Measuring a customer for the first time", 3.0);
        POINTS_MAP.put("Measuring a customer again", 5.0);
        POINTS_MAP.put("Showing a fitness program", 3.0);
        POINTS_MAP.put("Renewing a customer’s fitness program", 5.0);
        POINTS_MAP.put("Training session at the gym", 4.0);
        POINTS_MAP.put("Posting on Instagram", 10.0);
        POINTS_MAP.put("Posting Instagram story", 5.0);
        POINTS_MAP.put("Having a customer comment on Google Maps", 10.0);
        POINTS_MAP.put("Signing a reference member to the gym", 10.0);
        POINTS_MAP.put("Winning the weekly quiz", 5.0);
    }

    public PointsLog logEffort(LogEffortDto logEffortDto) {
        Double points = POINTS_MAP.get(logEffortDto.getEffortType());
        if (points == null) {
            throw new RuntimeException("Invalid effort type");
        }

        String trainerUsername = getLoggedInUsername();
        User trainer = userService.findByUsername(trainerUsername);

        PointsLog pointsLog = PointsLog.builder()
                .trainer(trainer)
                .effortType(logEffortDto.getEffortType())
                .points(points)
                .dateLogged(LocalDate.now())
                .build();

        pointsLogRepository.save(pointsLog);

        trainer.setPoints(trainer.getPoints() + points);
        userService.saveUser(trainer);

        return pointsLog;
    }

    public List<PointsLog> getTrainerLogs() {
        String trainerUsername = getLoggedInUsername();
        User trainer = userService.findByUsername(trainerUsername);
        return pointsLogRepository.findByTrainerId(trainer.getId());
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
