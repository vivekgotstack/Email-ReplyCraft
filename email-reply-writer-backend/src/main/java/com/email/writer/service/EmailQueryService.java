package com.email.writer.service;

import com.email.writer.entity.Email;
import com.email.writer.repository.EmailRepository;
import com.email.writer.specification.EmailSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailQueryService {

    private final EmailRepository emailRepository;

    public EmailQueryService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public Page<Email> getEmails(
            String tone,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable) {

        Specification<Email> spec =
                Specification.where(EmailSpecification.hasTone(tone))
                        .and(EmailSpecification.createdAfter(from))
                        .and(EmailSpecification.createdBefore(to));

        return emailRepository.findAll(spec, pageable);
    }
}