package com.alikuslu.housefit.demo.dto;

import lombok.Data;

@Data
public class Food {
    private String foodId;
    private String label;
    private Nutrients nutrients;
}
