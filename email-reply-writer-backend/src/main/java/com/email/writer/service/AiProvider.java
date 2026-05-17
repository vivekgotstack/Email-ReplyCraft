package com.email.writer.service;

import com.email.writer.dto.EmailRequestDTO;

import reactor.core.publisher.Mono;

public interface AiProvider {

    Mono<String> generateReply(EmailRequestDTO request);
}