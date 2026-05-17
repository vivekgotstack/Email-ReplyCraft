package com.email.writer.service;

import com.email.writer.dto.AiHistoryResponse;
import com.email.writer.dto.PageResponse;
import com.email.writer.entity.AiHistory;
import com.email.writer.repository.AiHistoryRepository;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiHistoryService {

    private final AiHistoryRepository repository;

    public AiHistoryService(AiHistoryRepository repository) {
        this.repository = repository;
    }

    public PageResponse<AiHistoryResponse> getHistory(
            String email,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<AiHistory> result = repository.findByEmail(email, pageable);

        List<AiHistoryResponse> content = result.getContent()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    public void save(
            String email,
            String prompt,
            String response,
            String tone,
            String model) {
        AiHistory history = new AiHistory();
        history.setEmail(email);
        history.setPrompt(prompt);
        history.setResponse(response);
        history.setTone(tone);
        history.setModel(model);

        repository.save(history);
    }

    private AiHistoryResponse mapToDto(AiHistory h) {
        return new AiHistoryResponse(
                h.getId(),
                h.getPrompt(),
                h.getResponse(),
                h.getTone(),
                h.getModel(),
                h.getCreatedAt());
    }
}