package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.CreatePackageRequest;
import com.alikuslu.housefit.demo.dto.FitnessPackageDto;
import com.alikuslu.housefit.demo.service.FitnessPackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/package")
public class FitnessPackageController {

    private final FitnessPackageService fitnessPackageService;

    public FitnessPackageController(FitnessPackageService fitnessPackageService) {
        this.fitnessPackageService = fitnessPackageService;
    }

    @GetMapping
    public ResponseEntity<List<FitnessPackageDto>> getAllActivePackages() {
        return ResponseEntity.ok(fitnessPackageService.getAllActivePackages());
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<FitnessPackageDto>> getPackagesByTrainer(@PathVariable Long trainerId) {
        return ResponseEntity.ok(fitnessPackageService.getPackagesByTrainer(trainerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FitnessPackageDto> getPackageById(@PathVariable Long id) {
        return ResponseEntity.ok(fitnessPackageService.getPackageById(id));
    }

    @PostMapping
    public ResponseEntity<FitnessPackageDto> createPackage(
            @RequestBody CreatePackageRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fitnessPackageService.createPackage(request, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FitnessPackageDto> updatePackage(
            @PathVariable Long id,
            @RequestBody CreatePackageRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fitnessPackageService.updatePackage(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivatePackage(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        fitnessPackageService.deactivatePackage(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
