package com.alikuslu.housefit.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String username;
    private String name;
    private String surname;
    private String phoneNumber;
    private String password;
    private String referralCode;
}
