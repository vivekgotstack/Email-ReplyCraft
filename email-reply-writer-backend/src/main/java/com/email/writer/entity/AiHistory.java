package com.email.writer.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "ai_history")
public class AiHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String response;

    private String tone;

    private String model;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public void setModel(String model) {
        this.model = model;
    }
}