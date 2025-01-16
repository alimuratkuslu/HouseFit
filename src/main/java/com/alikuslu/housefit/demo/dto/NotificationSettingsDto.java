package com.alikuslu.housefit.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationSettingsDto {
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private String reminderTime;
}
