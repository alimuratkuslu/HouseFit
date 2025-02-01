package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private UserType userType;
    private String avatar;
    private Double points;
    private Double weight;
    private Double height;
    private Double bodyFat;
    private Double muscleMass;
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private String reminderTime;

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getUserType(),
                user.getAvatar(),
                user.getPoints(),
                user.getWeight(),
                user.getHeight(),
                user.getBodyFat(),
                user.getMuscleMass(),
                user.getEmailNotifications(),
                user.getPushNotifications(),
                user.getReminderTime()
        );
    }
}
