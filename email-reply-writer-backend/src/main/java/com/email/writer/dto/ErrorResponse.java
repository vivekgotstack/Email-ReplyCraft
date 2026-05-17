package com.email.writer.dto;

import java.time.Instant;

public class ErrorResponse {

    private final boolean success = false;

    private final String errorCode;

    private final String message;

    private final Instant timestamp = Instant.now();

    public ErrorResponse(
            String errorCode,
            String message) {

        this.errorCode = errorCode;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}