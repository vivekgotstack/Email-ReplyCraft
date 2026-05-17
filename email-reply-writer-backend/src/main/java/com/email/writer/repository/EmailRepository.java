package com.email.writer.repository;

import com.email.writer.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailRepository extends JpaRepository<Email, Long>,
        JpaSpecificationExecutor<Email> {
}