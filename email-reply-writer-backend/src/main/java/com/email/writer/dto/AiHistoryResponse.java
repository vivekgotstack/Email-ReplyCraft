package com.email.writer.dto;

import java.time.LocalDateTime;

public record AiHistoryResponse(
        Long id,
        String prompt,
        String response,
        String tone,
        String model,
        LocalDateTime createdAt
) {}