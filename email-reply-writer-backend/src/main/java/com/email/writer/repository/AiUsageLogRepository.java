package com.email.writer.repository;

import com.email.writer.entity.AiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AiUsageLogRepository extends JpaRepository<AiUsageLog, Long> {

    long countByEmailAndRequestedAtAfter(String email, LocalDateTime from);

    List<AiUsageLog> findByEmailOrderByRequestedAtDesc(String email);
}