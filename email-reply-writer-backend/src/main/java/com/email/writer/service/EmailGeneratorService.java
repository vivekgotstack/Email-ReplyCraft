package com.email.writer.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.email.writer.dto.EmailRequestDTO;
import com.email.writer.exception.EmptyEmailException;

import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmailGeneratorService {

        private final WebClient webClient;
        private final String geminiApiKey;

        public EmailGeneratorService(
                        @Value("${gemini.api.url}") String geminiApiUrl,
                        @Value("${gemini.api.key}") String geminiApiKey) {

                this.geminiApiKey = geminiApiKey;

                System.out.println("========= GEMINI CONFIG =========");
                System.out.println("Gemini URL: " + geminiApiUrl);
                System.out.println("API KEY EXISTS: " +
                                (geminiApiKey != null && !geminiApiKey.isBlank()));
                System.out.println("=================================");

                this.webClient = WebClient.builder()
                                .baseUrl(geminiApiUrl)
                                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .build();
        }

        public Mono<String> generateRequest(EmailRequestDTO emailRequest) {

                System.out.println("========= REQUEST START =========");

                if (emailRequest.getEmailContent() == null
                                || emailRequest.getEmailContent().isBlank()) {

                        return Mono.error(
                                        new EmptyEmailException("Email content cannot be empty"));
                }

                String prompt = buildPrompt(emailRequest);

                Map<String, Object> body = Map.of(
                                "model", "google/gemini-2.0-flash-exp:free",

                                "messages", new Object[] {
                                                Map.of(
                                                                "role", "user",
                                                                "content", prompt)
                                });

                return webClient.post()

                                .header("Authorization", "Bearer " + geminiApiKey)

                                .header("HTTP-Referer",
                                                "https://email-replycraft.onrender.com")

                                .header("X-Title",
                                                "Email Reply Craft")

                                .bodyValue(body)

                                .retrieve()

                                .onStatus(
                                                status -> status.isError(),
                                                response -> response.bodyToMono(String.class)
                                                                .flatMap(errorBody -> {

                                                                        System.out.println(errorBody);

                                                                        return Mono.error(
                                                                                        new RuntimeException(
                                                                                                        errorBody));
                                                                }))

                                .bodyToMono(String.class)

                                .map(response -> {

                                        try {

                                                ObjectMapper mapper = new ObjectMapper();

                                                JsonNode json = mapper.readTree(response);

                                                return json
                                                                .get("choices")
                                                                .get(0)
                                                                .get("message")
                                                                .get("content")
                                                                .asText();

                                        } catch (Exception e) {
                                                throw new RuntimeException(e);
                                        }
                                });
        }

        private String buildPrompt(EmailRequestDTO emailRequest) {

                String tone = emailRequest.getTone() == null
                                ? null
                                : emailRequest.getTone().trim().toLowerCase();

                StringBuilder prompt = new StringBuilder(550);

                prompt.append("""
                                You are an assistant that writes email replies.

                                STRICT RULES:
                                - Do NOT include a subject line.
                                - Write ONLY the email body.
                                - Do NOT explain your reasoning.

                                ORIGINAL EMAIL:
                                ---
                                """);

                prompt.append(emailRequest.getEmailContent());

                prompt.append("""
                                ---

                                WRITE THE REPLY BELOW.
                                """);

                if (tone != null && !tone.isBlank()) {
                        prompt.append("\nTone: ").append(tone);
                }

                return prompt.toString();
        }
}