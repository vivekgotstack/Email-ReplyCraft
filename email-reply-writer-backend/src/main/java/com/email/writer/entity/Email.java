package com.email.writer.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
@Getter
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emailContent;

    private String tone;

    @Column(columnDefinition = "TEXT")
    private String generatedReply;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public void setGeneratedReply(String generatedReply) {
        this.generatedReply = generatedReply;
    }

}