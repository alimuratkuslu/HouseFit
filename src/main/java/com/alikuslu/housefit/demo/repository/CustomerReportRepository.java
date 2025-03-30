package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.CustomerReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerReportRepository extends JpaRepository<CustomerReport, Long> {

    List<CustomerReport> findByCustomerId(Long customerId);
    List<CustomerReport> findByTrainerId(Long trainerId);
    List<CustomerReport> findByCustomerIdAndTrainerId(Long customerId, Long trainerId);
    Optional<CustomerReport> findByFileKey(String fileKey);
}
