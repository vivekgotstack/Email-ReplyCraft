package com.email.writer.service;

import com.email.writer.dto.EmailRequestDTO;

public interface EmailService {

    String generateReply(EmailRequestDTO request);
}