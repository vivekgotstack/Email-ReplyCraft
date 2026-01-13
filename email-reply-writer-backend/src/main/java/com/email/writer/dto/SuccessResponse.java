package com.email.writer.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class SuccessResponse<T> {
    private boolean success = true;
    private String message;
    private T data;
    private Instant timeStamp = Instant.now();

    public SuccessResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}