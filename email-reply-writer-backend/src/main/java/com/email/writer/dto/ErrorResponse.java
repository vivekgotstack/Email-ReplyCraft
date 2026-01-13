package com.email.writer.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class ErrorResponse {
    private boolean success = false;
    private String errorCode;
    private String message;
    private Instant timeStamp = Instant.now();

    public ErrorResponse(String errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }
}