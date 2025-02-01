package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.SessionCheckInResponse;
import com.alikuslu.housefit.demo.model.CheckInStatus;
import com.alikuslu.housefit.demo.model.SessionCheckIn;
import com.alikuslu.housefit.demo.repository.SessionCheckInRepository;
import com.alikuslu.housefit.demo.service.SessionCheckInService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session-checkins")
public class SessionCheckInController {

    private final SessionCheckInService checkInService;
    private final SessionCheckInRepository checkInRepository;

    public SessionCheckInController(SessionCheckInService checkInService, SessionCheckInRepository checkInRepository) {
        this.checkInService = checkInService;
        this.checkInRepository = checkInRepository;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<SessionCheckInResponse>> getPendingCheckIns(@RequestParam Long userId) {
        List<SessionCheckIn> checkIns = checkInRepository.findActivePendingCheckIns(userId);
        return ResponseEntity.ok(checkIns.stream()
                .map(this::convertToResponse)
                .toList());
    }

    @PutMapping("/{checkInId}")
    public ResponseEntity<SessionCheckInResponse> updateCheckInStatus(
            @PathVariable Long checkInId,
            @RequestParam CheckInStatus status
    ) {
        checkInService.processCheckInResponse(checkInId, status);
        SessionCheckIn updated = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new RuntimeException("Check-in not found"));
        return ResponseEntity.ok(convertToResponse(updated));
    }

    private SessionCheckInResponse convertToResponse(SessionCheckIn checkIn) {
        return new SessionCheckInResponse(
                checkIn.getId(),
                checkIn.getSession().getId(),
                checkIn.getSession().getName(),
                checkIn.getSession().getStartTime(),
                checkIn.getStatus(),
                checkIn.getRespondedAt()
        );
    }
}
