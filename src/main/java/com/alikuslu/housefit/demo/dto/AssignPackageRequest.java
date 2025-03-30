package com.alikuslu.housefit.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignPackageRequest {
    private Long customerId;
    private Long packageId;
    private LocalDate startDate;
    private String notes;
}
