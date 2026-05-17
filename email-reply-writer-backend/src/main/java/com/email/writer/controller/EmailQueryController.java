package com.email.writer.controller;

import com.email.writer.entity.Email;
import com.email.writer.service.EmailQueryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/emails")
public class EmailQueryController {

    private final EmailQueryService emailQueryService;

    public EmailQueryController(EmailQueryService emailQueryService) {
        this.emailQueryService = emailQueryService;
    }

    @GetMapping
    public Page<Email> getEmails(
            @RequestParam(required = false) String tone,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {

        return emailQueryService.getEmails(tone, from, to, pageable);
    }
}