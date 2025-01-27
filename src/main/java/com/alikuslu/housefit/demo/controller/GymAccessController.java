package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.GymAccessRecordRequest;
import com.alikuslu.housefit.demo.dto.GymAccessRecordResponse;
import com.alikuslu.housefit.demo.model.GymAccess;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.service.GymAccessService;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gym-access")
public class GymAccessController {

    private final GymAccessService gymAccessService;

    public GymAccessController(GymAccessService gymAccessService) {
        this.gymAccessService = gymAccessService;
    }

    @PostMapping("/record")
    public ResponseEntity<GymAccessRecordResponse> recordAccess(
            @RequestBody GymAccessRecordRequest request,
            @AuthenticationPrincipal User currentUser
    ) throws BadRequestException {
        GymAccess record = gymAccessService.recordAccess(request.getQrCode(), currentUser);
        return ResponseEntity.ok(mapToResponse(record));
    }

    @GetMapping("/history")
    public ResponseEntity<List<GymAccessRecordResponse>> getHistory(
            @AuthenticationPrincipal User currentUser
    ) {
        List<GymAccess> history = gymAccessService.getUserAccessHistory(currentUser);
        List<GymAccessRecordResponse> response = history.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/date-range")
    public ResponseEntity<List<GymAccessRecordResponse>> getHistoryBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @AuthenticationPrincipal User currentUser
    ) {
        List<GymAccess> history = gymAccessService.getUserAccessHistoryBetweenDates(
                currentUser,
                startTime,
                endTime
        );
        List<GymAccessRecordResponse> response = history.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private GymAccessRecordResponse mapToResponse(GymAccess record) {
        return GymAccessRecordResponse.builder()
                .id(record.getId())
                .userId(record.getUser().getId())
                .accessType(record.getAccessType().name())
                .timestamp(record.getTimestamp())
                .locationId(record.getLocationId())
                .build();
    }
}
