package com.alikuslu.housefit.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAppointmentDto {

    private Long trainerId;
    //TODO: Expecting date in ISO format (e.g., "2025-01-06T10:00:00")
    private String date;
    private String message = "";

    public String getMessage() {
        return message != null ? message : "";
    }
}
