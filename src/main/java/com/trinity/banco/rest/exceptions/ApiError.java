package com.trinity.banco.rest.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
