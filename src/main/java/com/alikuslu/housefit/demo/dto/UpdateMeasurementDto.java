package com.alikuslu.housefit.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateMeasurementDto {
    private Double weight;
    private Double height;
    private Double bodyFat;
    private Double muscleMass;
}
