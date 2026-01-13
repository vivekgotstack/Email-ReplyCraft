package com.email.writer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class EmailRequestDTO {

    @NotBlank(message = "Email content must not be empty")
    @Size(max = 5000, message = "Email content is too long")
    private String emailContent;

    @Size(max = 30, message = "Tone value is too long")
    private String tone;
}
