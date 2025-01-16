package com.alikuslu.housefit.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateProfileDto {
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
}
