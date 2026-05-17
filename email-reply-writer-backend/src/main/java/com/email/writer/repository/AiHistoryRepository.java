package com.email.writer.repository;

import com.email.writer.entity.AiHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiHistoryRepository extends JpaRepository<AiHistory, Long> {

    Page<AiHistory> findByEmail(String email, Pageable pageable);
}