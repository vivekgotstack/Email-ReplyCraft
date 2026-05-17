package com.email.writer.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "ai_usage_logs")
public class AiUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String model;

    private LocalDateTime requestedAt = LocalDateTime.now();

    private boolean success;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

}