package com.email.writer.controller;

import com.email.writer.dto.PageResponse;
import com.email.writer.dto.AiHistoryResponse;
import com.email.writer.service.AiHistoryService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class AiHistoryController {

    private final AiHistoryService historyService;

    public AiHistoryController(AiHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public PageResponse<AiHistoryResponse> getHistory(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return historyService.getHistory(email, page, size);
    }
}