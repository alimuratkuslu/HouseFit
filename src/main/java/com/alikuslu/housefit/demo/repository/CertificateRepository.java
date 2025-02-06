package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.Certificate;
import com.alikuslu.housefit.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByTrainer(User trainer);
}
