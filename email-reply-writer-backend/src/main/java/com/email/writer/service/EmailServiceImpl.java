package com.email.writer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.email.writer.dto.EmailRequestDTO;
import com.email.writer.exception.AiServiceException;
import com.email.writer.exception.EmptyEmailException;
import com.email.writer.security.SecurityUtil;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final AiProvider aiProvider;
    private final AiUsageService aiUsageService;
    private final AiHistoryService aiHistoryService;

    public EmailServiceImpl(
            AiProvider aiProvider,
            AiUsageService aiUsageService,
            AiHistoryService aiHistoryService) {
        this.aiProvider = aiProvider;
        this.aiUsageService = aiUsageService;
        this.aiHistoryService = aiHistoryService;
    }

    @Override
    @Transactional
    public String generateReply(EmailRequestDTO request) {

        if (request.getEmailContent() == null
                || request.getEmailContent().isBlank()) {

            throw new EmptyEmailException(
                    "Email content cannot be empty");
        }

        String email = SecurityUtil.getCurrentUserEmail();

        aiUsageService.enforceDailyLimit(email);

        try {

            String response = aiProvider
                    .generateReply(request).block();

            aiUsageService.logSuccess(
                    email,
                    "openrouter/free",
                    "OK");

            aiHistoryService.save(
                    email,
                    request.getEmailContent(),
                    response,
                    request.getTone(),
                    "openrouter/free");

            log.info(
                    "Email reply generated successfully for user: {}",
                    email);

            return response;

        } catch (Exception e) {

            aiUsageService.logFailure(
                    email,
                    "openrouter/free",
                    e.getMessage());

            log.error(
                    "Email reply generation failed for user: {}",
                    email,
                    e);

            throw new AiServiceException(
                    "Failed to generate email reply");
        }
    }
}