package com.email.writer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.email.writer.dto.EmailRequestDTO;
import com.email.writer.dto.SuccessResponse;
import com.email.writer.service.EmailGeneratorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/email")
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;

    public EmailGeneratorController(EmailGeneratorService emailGeneratorService) {
        this.emailGeneratorService = emailGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<SuccessResponse<String>> generateEmail(@Valid @RequestBody EmailRequestDTO dto) {
        String response = emailGeneratorService.generateRequest(dto);
        return ResponseEntity.ok(new SuccessResponse<>("Response generated",response));
    }
}