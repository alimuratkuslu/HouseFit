package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.AssignPackageRequest;
import com.alikuslu.housefit.demo.dto.CustomerPackageDto;
import com.alikuslu.housefit.demo.service.CustomerPackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-package")
public class CustomerPackageController {

    private final CustomerPackageService customerPackageService;

    public CustomerPackageController(CustomerPackageService customerPackageService) {
        this.customerPackageService = customerPackageService;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerPackageDto>> getCustomerPackages(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerPackageService.getCustomerPackages(customerId));
    }

    @GetMapping("/customer/{customerId}/active")
    public ResponseEntity<List<CustomerPackageDto>> getActiveCustomerPackages(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerPackageService.getActiveCustomerPackages(customerId));
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<CustomerPackageDto>> getTrainerAssignedPackages(@PathVariable Long trainerId) {
        return ResponseEntity.ok(customerPackageService.getTrainerAssignedPackages(trainerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerPackageDto> getCustomerPackageById(@PathVariable Long id) {
        return ResponseEntity.ok(customerPackageService.getCustomerPackageById(id));
    }

    @PostMapping("/assign")
    public ResponseEntity<CustomerPackageDto> assignPackage(
            @RequestBody AssignPackageRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(customerPackageService.assignPackage(request, userDetails.getUsername()));
    }

    @PostMapping("/{id}/complete-session")
    public ResponseEntity<CustomerPackageDto> completeSession(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(customerPackageService.completeSession(id, userDetails.getUsername()));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelPackage(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        customerPackageService.cancelPackage(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
