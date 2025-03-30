package com.alikuslu.housefit.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePackageRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Integer totalSessions;
    private String features;
}
