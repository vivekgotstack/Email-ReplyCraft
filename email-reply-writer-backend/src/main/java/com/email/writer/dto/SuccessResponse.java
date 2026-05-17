package com.email.writer.dto;

import java.time.Instant;

public class SuccessResponse<T> {

    private final boolean success = true;

    private final String message;

    private final T data;

    private final Instant timestamp = Instant.now();

    public SuccessResponse(
            String message,
            T data) {

        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}