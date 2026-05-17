package com.email.writer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AiCacheService {

    private static final Logger log = LoggerFactory.getLogger(AiCacheService.class);

    public String get(String key) {
        return null; // cache disabled
    }

    public void set(String key, String value, long ttlSeconds) {
        // no-op
        log.debug("Cache disabled - skipping SET");
    }
}