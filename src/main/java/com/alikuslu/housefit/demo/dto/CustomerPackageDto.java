package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPackageDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long packageId;
    private String packageName;
    private Long assignedById;
    private String assignedByName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer remainingSessions;
    private PackageStatus status;
    private String notes;
}
