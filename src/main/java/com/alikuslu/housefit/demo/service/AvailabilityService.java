package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.SetAvailabilityDto;
import com.alikuslu.housefit.demo.model.Availability;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.repository.AvailabilityRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final UserService userService;

    public AvailabilityService(AvailabilityRepository availabilityRepository, UserService userService) {
        this.availabilityRepository = availabilityRepository;
        this.userService = userService;
    }

    public Availability setAvailability(SetAvailabilityDto setAvailabilityDto) {
        LocalDateTime startTime = LocalDateTime.parse(setAvailabilityDto.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(setAvailabilityDto.getEndTime());

        String trainerUsername = getLoggedInUsername();
        User trainer = userService.findByUsername(trainerUsername);

        Availability availability = Availability.builder()
                .trainer(trainer)
                .startTime(startTime)
                .endTime(endTime)
                .isBooked(false)
                .build();

        return availabilityRepository.save(availability);
    }

    public List<Availability> getAvailability(Long trainerId) {
        return availabilityRepository.findByTrainerId(trainerId);
    }

    public List<Availability> getAvailabilityForDate(Long trainerId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return availabilityRepository.findByTrainerIdAndStartTimeBetweenAndIsBookedFalse(
                trainerId,
                startOfDay,
                endOfDay
        );
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
