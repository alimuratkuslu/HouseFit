package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.SetAvailabilityDto;
import com.alikuslu.housefit.demo.model.Availability;
import com.alikuslu.housefit.demo.service.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<Availability> setAvailability(@RequestBody SetAvailabilityDto setAvailabilityDto) {
        Availability availability = availabilityService.setAvailability(setAvailabilityDto);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<Availability>> getAvailability(@PathVariable Long trainerId) {
        List<Availability> availabilities = availabilityService.getAvailability(trainerId);
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/trainer/{trainerId}/date/{date}")
    public ResponseEntity<List<Availability>> getAvailabilityForDate(
            @PathVariable Long trainerId,
            @PathVariable String date) {
        LocalDate requestedDate = LocalDate.parse(date);
        List<Availability> availabilities = availabilityService.getAvailabilityForDate(trainerId, requestedDate);
        return ResponseEntity.ok(availabilities);
    }
}
