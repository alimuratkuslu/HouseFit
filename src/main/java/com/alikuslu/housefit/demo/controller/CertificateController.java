package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.CertificateRequest;
import com.alikuslu.housefit.demo.model.Certificate;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.service.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/certificate")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping
    public ResponseEntity<Certificate> addCertificate(@ModelAttribute CertificateRequest request) throws IOException {
        return ResponseEntity.ok(certificateService.addCertificate(request));
    }

    @GetMapping("/my-certificates")
    public ResponseEntity<List<Certificate>> getMyCertificates() {
        return ResponseEntity.ok(certificateService.getCertificatesForTrainer());
    }

    @PutMapping("/{certificateId}")
    public ResponseEntity<Certificate> updateCertificate(
            @PathVariable Long certificateId,
            @RequestBody CertificateRequest request
    ) throws IOException {
        return ResponseEntity.ok(certificateService.updateCertificate(certificateId, request));
    }

    @DeleteMapping("/{certificateId}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long certificateId) {
        certificateService.deleteCertificate(certificateId);
        return ResponseEntity.noContent().build();
    }
}
