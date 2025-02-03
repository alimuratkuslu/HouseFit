package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.UpdateTrainerTargetsRequest;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/targets")
    public ResponseEntity<User> updateTrainerTargets(@RequestBody UpdateTrainerTargetsRequest request) {
        return ResponseEntity.ok(adminService.updateTrainerTargets(request));
    }

    @GetMapping("/progress/{trainerId}")
    public ResponseEntity<Map<String, Object>> getTrainerProgress(@PathVariable Long trainerId) {
        return ResponseEntity.ok(adminService.getTrainerMonthlyProgress(trainerId));
    }
}
