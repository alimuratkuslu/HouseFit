package com.alikuslu.housefit.demo.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {

    private final String field;

    public AuthenticationException(String message, String field) {
        super(message);
        this.field = field;
    }

}
