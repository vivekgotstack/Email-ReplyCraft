package com.email.writer.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();

        String path = request.getRequestURI();
        String method = request.getMethod();

        log.info("Incoming Request -> {} {}", method, path);

        try {
            filterChain.doFilter(request, response);
        } finally {

            long timeTaken = System.currentTimeMillis() - start;

            log.info("Completed -> {} {} | Status: {} | Time: {} ms",
                    method,
                    path,
                    response.getStatus(),
                    timeTaken);
        }
    }
}