package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.LogEffortDto;
import com.alikuslu.housefit.demo.model.PointsLog;
import com.alikuslu.housefit.demo.service.PointsLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointsLogController {

    private final PointsLogService pointsLogService;

    public PointsLogController(PointsLogService pointsLogService) {
        this.pointsLogService = pointsLogService;
    }

    @PostMapping("/log")
    public ResponseEntity<PointsLog> logEffort(@RequestBody LogEffortDto logEffortDto) {
        PointsLog pointsLog = pointsLogService.logEffort(logEffortDto);
        return ResponseEntity.ok(pointsLog);
    }

    @GetMapping("/my-logs")
    public ResponseEntity<List<PointsLog>> getMyLogs() {
        List<PointsLog> logs = pointsLogService.getTrainerLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/admin/trainer/{trainerId}/logs")
    public ResponseEntity<List<PointsLog>> getTrainerLogs(@PathVariable Long trainerId) {
        List<PointsLog> logs = pointsLogService.getTrainerLogsForAdmin(trainerId);
        return ResponseEntity.ok(logs);
    }
}
