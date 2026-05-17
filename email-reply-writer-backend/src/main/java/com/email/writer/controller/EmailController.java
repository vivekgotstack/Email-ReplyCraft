package com.email.writer.controller;

import com.email.writer.dto.EmailRequestDTO;
import com.email.writer.dto.SuccessResponse;
import com.email.writer.service.EmailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("alive");
    }

    @PostMapping("/generate")
    public ResponseEntity<SuccessResponse<String>> generate(
            @RequestBody EmailRequestDTO dto) {

        String response = emailService.generateReply(dto);

        return ResponseEntity.ok(
                new SuccessResponse<>("Generated", response));
    }
}