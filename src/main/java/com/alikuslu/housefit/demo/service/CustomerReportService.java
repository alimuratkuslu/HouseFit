package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.model.CustomerReport;
import com.alikuslu.housefit.demo.repository.CustomerReportRepository;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CustomerReportService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;
    private final CustomerReportRepository reportRepository;

    @Autowired
    public CustomerReportService(AmazonS3 s3Client, CustomerReportRepository reportRepository) {
        this.s3Client = s3Client;
        this.reportRepository = reportRepository;
    }

    public CustomerReport uploadReport(MultipartFile file, Long customerId, Long trainerId, String title, String description) {
        try {
            String fileKey = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            s3Client.putObject(new PutObjectRequest(
                    bucketName,
                    fileKey,
                    file.getInputStream(),
                    metadata));

            CustomerReport report = new CustomerReport();
            report.setFileName(file.getOriginalFilename());
            report.setFileKey(fileKey);
            report.setUploadDate(LocalDateTime.now());
            report.setCustomerId(customerId);
            report.setTrainerId(trainerId);
            report.setTitle(title);
            report.setDescription(description);
            report.setContentType(file.getContentType());
            report.setFileSize(file.getSize());

            return reportRepository.save(report);
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public byte[] downloadReport(Long reportId) {
        CustomerReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        try {
            S3Object s3Object = s3Client.getObject(bucketName, report.getFileKey());
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("Error downloading file: {}", e.getMessage());
            throw new RuntimeException("Failed to download file", e);
        }
    }

    public Optional<CustomerReport> getReportById(Long reportId) {
        return reportRepository.findById(reportId);
    }

    public boolean isReportOwnedByCustomer(Long reportId, Long customerId) {
        return reportRepository.findById(reportId)
                .map(report -> report.getCustomerId().equals(customerId))
                .orElse(false);
    }

    public boolean isReportUploadedByTrainer(Long reportId, Long trainerId) {
        return reportRepository.findById(reportId)
                .map(report -> report.getTrainerId().equals(trainerId))
                .orElse(false);
    }

    public void deleteReport(Long reportId) {
        CustomerReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        s3Client.deleteObject(bucketName, report.getFileKey());

        reportRepository.delete(report);
    }

    public List<CustomerReport> getReportsByCustomerId(Long customerId) {
        return reportRepository.findByCustomerId(customerId);
    }

    public List<CustomerReport> getReportsByTrainerId(Long trainerId) {
        return reportRepository.findByTrainerId(trainerId);
    }

    public String generatePresignedUrl(Long reportId, int expirationMinutes) {
        CustomerReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * expirationMinutes;
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, report.getFileKey())
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}
