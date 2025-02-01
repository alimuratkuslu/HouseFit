package com.alikuslu.housefit.demo.dto;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private String email;
    private String code;
}
