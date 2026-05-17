package com.email.writer.service;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.email.writer.config.AiProperties;
import com.email.writer.dto.EmailRequestDTO;
import com.email.writer.dto.OpenRouterResponse;
import com.email.writer.exception.AiProviderException;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
public class OpenRouterProvider implements AiProvider {

    private static final Logger log = LoggerFactory.getLogger(OpenRouterProvider.class);

    private final WebClient webClient;
    private final AiProperties aiProperties;

    public OpenRouterProvider(
            WebClient webClient,
            AiProperties aiProperties) {

        this.webClient = webClient;
        this.aiProperties = aiProperties;
    }

    @Override
    public Mono<String> generateReply(EmailRequestDTO request) {

        String prompt = buildPrompt(request);

        Map<String, Object> body = Map.of(
                "model", "openrouter/free",
                "messages", new Object[] {
                        Map.of(
                                "role", "user",
                                "content", prompt)
                });

        return webClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + aiProperties.getApiKey())
                .header(
                        "HTTP-Referer",
                        "https://email-replycraft.onrender.com")
                .header(
                        "X-Title",
                        "Email Reply Craft")
                .bodyValue(body)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError()
                                || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {

                                    log.error(
                                            "AI API error: {}",
                                            errorBody);

                                    return Mono.error(
                                            new AiProviderException(
                                                    "AI provider request failed"));
                                }))
                .bodyToMono(OpenRouterResponse.class)
                .timeout(Duration.ofSeconds(
                        aiProperties.getTimeoutSeconds()))
                .retryWhen(
                        Retry.backoff(
                                aiProperties.getRetryCount(),
                                Duration.ofSeconds(2))
                                .doBeforeRetry(r -> log.warn(
                                        "AI retry: {}",
                                        r.totalRetries() + 1)))
                .map(this::extractContent)
                .doOnSuccess(res -> log.info("AI generation successful"))
                .doOnError(err -> log.error("AI generation failed", err));
    }

    private String extractContent(OpenRouterResponse response) {

        try {

            if (response == null
                    || response.getChoices() == null
                    || response.getChoices().isEmpty()
                    || response.getChoices().get(0).getMessage() == null) {

                throw new AiProviderException(
                        "Empty AI response received");
            }

            String content = response
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

            if (content == null || content.isBlank()) {

                throw new AiProviderException(
                        "AI returned blank response");
            }

            return content.trim();

        } catch (Exception e) {

            log.error("Failed to extract AI content", e);

            throw new AiProviderException(
                    "Failed to parse AI response",
                    e);
        }
    }

    private String buildPrompt(EmailRequestDTO emailRequest) {

        String tone = emailRequest.getTone() == null
                ? null
                : emailRequest.getTone()
                        .trim()
                        .toLowerCase();

        StringBuilder prompt = new StringBuilder(550);

        prompt.append("""
                You are an assistant that writes email replies with provided tone.

                    STRICT RULES:
                    - Write ONLY the email body.
                    - Do NOT explain your reasoning.
                    - Write a detailed and complete reply.
                    - Keep the response according to the tone selected.
                    - Response should usually be 120-200 words unless the email is very short.
                    - Add proper greeting and closing lines.

                ORIGINAL EMAIL:
                ---
                """);

        prompt.append(emailRequest.getEmailContent());

        prompt.append("""
                ---

                WRITE THE REPLY BELOW.
                """);

        if (tone != null && !tone.isBlank()) {

            prompt.append("\nTone: ")
                    .append(tone);
        }

        return prompt.toString();
    }
}