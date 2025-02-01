package com.alikuslu.housefit.demo.dto;

import lombok.Data;

@Data
public class NewPasswordRequest {
    private String email;
    private String newPassword;
}
