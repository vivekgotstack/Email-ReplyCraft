package com.email.writer.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

            System.out.println("Email content empty!");

            return Mono.error(
                    new EmptyEmailException("Email content cannot be empty"));
        }

        String prompt = buildPrompt(emailRequest);

        System.out.println("Prompt Length: " + prompt.length());

        Map<String, Object> body = Map.of(
                "contents", new Object[] {
                        Map.of(
                                "parts", new Object[] {
                                        Map.of("text", prompt)
                                })
                });

        System.out.println("Sending request to Gemini...");

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", geminiApiKey)
                        .build())
                .bodyValue(body)
                .retrieve()

                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {

                                    System.out.println("========= GEMINI ERROR =========");
                                    System.out.println(errorBody);
                                    System.out.println("================================");

                                    return Mono.error(
                                            new RuntimeException(
                                                    "Gemini API Error: " + errorBody));
                                }))

                .bodyToMono(GeminiResponse.class)

                .doOnNext(response -> {
                    System.out.println("========= GEMINI SUCCESS RESPONSE =========");
                    System.out.println(response);
                    System.out.println("===========================================");
                })

                .map(response -> {

                    if (response == null) {
                        throw new RuntimeException("Gemini response is null");
                    }

                    if (response.candidates() == null
                            || response.candidates().length == 0) {

                        throw new RuntimeException(
                                "No candidates returned from Gemini");
                    }

                    if (response.candidates()[0].content() == null) {
                        throw new RuntimeException(
                                "Candidate content is null");
                    }

                    if (response.candidates()[0].content().parts() == null
                            || response.candidates()[0].content().parts().length == 0) {

                        throw new RuntimeException(
                                "Candidate parts are null/empty");
                    }

                    String text =
                            response.candidates()[0]
                                    .content()
                                    .parts()[0]
                                    .text();

                    System.out.println("========= GENERATED RESPONSE =========");
                    System.out.println(text);
                    System.out.println("======================================");

                    return text;
                })

                .doOnError(error -> {
                    System.out.println("========= FULL ERROR =========");
                    error.printStackTrace();
                    System.out.println("==============================");
                })

                .timeout(Duration.ofSeconds(60));
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