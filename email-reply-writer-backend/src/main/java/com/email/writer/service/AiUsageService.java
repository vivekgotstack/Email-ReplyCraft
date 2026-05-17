package com.email.writer.service;

import com.email.writer.entity.AiUsageLog;
import com.email.writer.exception.RateLimitExceededException;
import com.email.writer.repository.AiUsageLogRepository;
import com.email.writer.config.RateLimitProperties;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AiUsageService {

    private final AiUsageLogRepository repository;
    private final RateLimitProperties rateLimitProperties;

    public AiUsageService(
            AiUsageLogRepository repository,
            RateLimitProperties rateLimitProperties) {

        this.repository = repository;
        this.rateLimitProperties = rateLimitProperties;
    }

    public void enforceDailyLimit(String email) {

        LocalDateTime startOfDay = LocalDateTime.now()
                .toLocalDate()
                .atStartOfDay();

        long usage = repository.countByEmailAndRequestedAtAfter(
                email,
                startOfDay
        );

        if (usage >= rateLimitProperties.getDailyLimit()) {
            throw new RateLimitExceededException(
                    "Daily AI limit exceeded: " + rateLimitProperties.getDailyLimit()
            );
        }
    }

    public void logSuccess(String email, String model, String metadata) {
        AiUsageLog log = new AiUsageLog();
        log.setEmail(email);
        log.setModel(model);
        log.setSuccess(true);
        log.setMetadata(metadata);
        repository.save(log);
    }

    public void logFailure(String email, String model, String error) {
        AiUsageLog log = new AiUsageLog();
        log.setEmail(email);
        log.setModel(model);
        log.setSuccess(false);
        log.setMetadata(error);
        repository.save(log);
    }
}