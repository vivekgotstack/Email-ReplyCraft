package com.email.writer.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.email.writer.dto.EmailRequestDTO;
import com.email.writer.dto.GeminiResponse;
import com.email.writer.exception.EmptyEmailException;

import reactor.core.publisher.Mono;

@Service
public class EmailGeneratorService {

        private final WebClient webClient;

        public EmailGeneratorService(
                        @Value("${gemini.api.url}") String geminiApiUrl,
                        @Value("${gemini.api.key}") String geminiApiKey) {

                this.webClient = WebClient.builder()
                                .baseUrl(geminiApiUrl)
                                .defaultHeader("x-goog-api-key", geminiApiKey)
                                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .build();
        }

        public Mono<String> generateRequest(EmailRequestDTO emailRequest) {

                if (emailRequest.getEmailContent() == null || emailRequest.getEmailContent().isBlank()) {
                        return Mono.error(new EmptyEmailException("Email content cannot be empty"));
                }

                String prompt = buildPrompt(emailRequest);

                Map<String, Object> body = Map.of(
                                "contents", new Object[] {
                                                Map.of(
                                                                "parts", new Object[] {
                                                                                Map.of("text", prompt)
                                                                })
                                });

                return webClient.post()
                                .bodyValue(body)
                                .retrieve()
                                .onStatus(
                                                status -> status.isError(),
                                                response -> response.bodyToMono(String.class)
                                                                .flatMap(err -> Mono.error(
                                                                                new RuntimeException(
                                                                                                "Gemini API error: "
                                                                                                                + err))))
                                .bodyToMono(GeminiResponse.class)
                                .map(res -> res.candidates()[0].content().parts()[0].text())
                                .timeout(Duration.ofSeconds(20));
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
                                - Do NOT invent real company names, people, or dates.

                                PLACEHOLDER RULES:
                                - Use placeholders wrapped in double curly braces.
                                - If a company name is needed, use {{ORGANIZATION_NAME}}
                                - If a sender name is needed, use {{SENDER_NAME}}
                                - If a team/department is needed, use {{TEAM_NAME}}
                                - If a project/product is needed, use {{PROJECT_NAME}}
                                - If a date or time is needed, use {{DATE}}
                                - For any other specific detail, create a placeholder using {{UPPER_SNAKE_CASE}}

                                SIGN-OFF REQUIREMENT:
                                - The reply MUST end with a natural email closing.
                                - Use ONE of the following formats only:
                                  • Regards, {{SENDER_NAME}}
                                  • Best regards, {{TEAM_NAME}}
                                  • Sincerely, {{ORGANIZATION_NAME}}
                                """);

                if (tone != null && !tone.isBlank()) {
                        prompt.append("""

                                        TONE REQUIREMENT:
                                        - The reply MUST be written in a %s tone.
                                        - The sign-off MUST match the same tone.
                                        """.formatted(tone));
                }

                prompt.append("""

                                ORIGINAL EMAIL:
                                ---
                                """);

                prompt.append(emailRequest.getEmailContent());

                prompt.append("""
                                ---

                                WRITE THE REPLY BELOW.
                                END WITH A SIGN-OFF USING PLACEHOLDERS:
                                """);

                return prompt.toString();
        }
}
