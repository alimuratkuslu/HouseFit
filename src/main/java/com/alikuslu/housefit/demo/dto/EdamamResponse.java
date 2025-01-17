package com.alikuslu.housefit.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class EdamamResponse {
    private List<ParsedFood> parsed;
}
