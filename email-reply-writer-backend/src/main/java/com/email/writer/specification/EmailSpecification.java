package com.email.writer.specification;

import com.email.writer.entity.Email;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EmailSpecification {

    public static Specification<Email> hasTone(String tone) {
        return (root, query, cb) -> {
            if (tone == null || tone.isBlank()) return null;
            return cb.equal(cb.lower(root.get("tone")), tone.toLowerCase());
        };
    }

    public static Specification<Email> createdAfter(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) return null;
            return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
        };
    }

    public static Specification<Email> createdBefore(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) return null;
            return cb.lessThanOrEqualTo(root.get("createdAt"), to);
        };
    }
}