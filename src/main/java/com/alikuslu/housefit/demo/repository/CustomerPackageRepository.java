package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.CustomerPackage;
import com.alikuslu.housefit.demo.model.PackageStatus;
import com.alikuslu.housefit.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerPackageRepository extends JpaRepository<CustomerPackage, Long> {
    List<CustomerPackage> findByCustomer(User customer);
    List<CustomerPackage> findByCustomerAndStatus(User customer, PackageStatus status);
    List<CustomerPackage> findByAssignedBy(User trainer);
}
