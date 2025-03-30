package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.model.CustomerReport;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.service.CustomerReportService;
import com.alikuslu.housefit.demo.service.UserService;
import com.amazonaws.auth.policy.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer-report")
public class CustomerReportController {

    private final CustomerReportService reportService;
    private final UserService userService;

    public CustomerReportController(CustomerReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CustomerReport> uploadReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("customerId") Long customerId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @AuthenticationPrincipal UserDetails userDetails) {

        User trainer = userService.findByUsername(userDetails.getUsername());
        CustomerReport report = reportService.uploadReport(file, customerId, trainer.getId(), title, description);
        return ResponseEntity.ok(report);

    }

    @GetMapping("/{reportId}")
    public ResponseEntity<Resource> downloadReport(@PathVariable Long reportId, @AuthenticationPrincipal UserDetails userDetails) {
        if (!userService.canAccessReport(reportId, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CustomerReport report = reportService.getReportById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        String presignedUrl = reportService.generatePresignedUrl(reportId, 10);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(presignedUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerReport>> getReportsByCustomer(
            @PathVariable Long customerId) {
        List<CustomerReport> reports = reportService.getReportsByCustomerId(customerId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<CustomerReport>> getReportsByTrainer(@AuthenticationPrincipal User currentUser) {
        Long trainerId = userService.findById(currentUser.getId()).getId();
        List<CustomerReport> reports = reportService.getReportsByTrainerId(trainerId);
        return ResponseEntity.ok(reports);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId, @AuthenticationPrincipal UserDetails userDetails) {
        if (!userService.canDeleteReport(reportId, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}
