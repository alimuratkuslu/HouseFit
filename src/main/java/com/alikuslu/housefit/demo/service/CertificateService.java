package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.CertificateRequest;
import com.alikuslu.housefit.demo.model.Certificate;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import com.alikuslu.housefit.demo.repository.CertificateRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;

    public CertificateService(CertificateRepository certificateRepository, UserRepository userRepository) {
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Certificate addCertificate(CertificateRequest request) throws IOException {
        Optional<User> optionalUser = userRepository.findByUsername(getLoggedInUsername());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = optionalUser.get();
        if (user.getUserType() != UserType.TRAINER) {
            throw new AccessDeniedException("Only trainers can add certificates");
        }

        Certificate certificate = new Certificate();
        certificate.setName(request.getName());
        certificate.setIssuingOrganization(request.getIssuingOrganization());
        certificate.setIssueDate(request.getIssueDate());
        certificate.setExpirationDate(request.getExpirationDate());
        certificate.setCredentialId(request.getCredentialId());
        certificate.setVerificationUrl(request.getVerificationUrl());
        certificate.setCertificateFile(request.getCertificateFile().getBytes());
        certificate.setTrainer(user);

        return certificateRepository.save(certificate);
    }

    public List<Certificate> getCertificatesForTrainer() {
        Optional<User> optionalUser = userRepository.findByUsername(getLoggedInUsername());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User trainer = optionalUser.get();
        return certificateRepository.findByTrainer(trainer);
    }

    @Transactional
    public Certificate updateCertificate(Long certificateId, CertificateRequest request) throws IOException {
        Optional<User> optionalUser = userRepository.findByUsername(getLoggedInUsername());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = optionalUser.get();
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        if (!certificate.getTrainer().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only update your own certificates");
        }

        certificate.setName(request.getName());
        certificate.setIssuingOrganization(request.getIssuingOrganization());
        certificate.setIssueDate(request.getIssueDate());
        certificate.setExpirationDate(request.getExpirationDate());
        certificate.setCredentialId(request.getCredentialId());
        certificate.setVerificationUrl(request.getVerificationUrl());

        if (request.getCertificateFile() != null && !request.getCertificateFile().isEmpty()) {
            certificate.setCertificateFile(request.getCertificateFile().getBytes());
        }

        return certificateRepository.save(certificate);
    }

    @Transactional
    public void deleteCertificate(Long certificateId) {
        Optional<User> optionalUser = userRepository.findByUsername(getLoggedInUsername());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = optionalUser.get();
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        if (!certificate.getTrainer().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only delete your own certificates");
        }

        certificateRepository.delete(certificate);
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
