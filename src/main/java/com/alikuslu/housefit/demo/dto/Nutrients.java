package com.alikuslu.housefit.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Nutrients {
    @JsonProperty("ENERC_KCAL")
    private Double calories;

    @JsonProperty("PROCNT")
    private Double protein;

    @JsonProperty("FAT")
    private Double fat;

    @JsonProperty("CHOCDF")
    private Double carbs;

    @JsonProperty("FIBTG")
    private Double fiber;
}
