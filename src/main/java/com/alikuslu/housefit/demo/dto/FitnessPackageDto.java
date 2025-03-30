package com.alikuslu.housefit.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitnessPackageDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Integer totalSessions;
    private String features;
    private Boolean active;
    private Long createdById;
    private String createdByName;
}
