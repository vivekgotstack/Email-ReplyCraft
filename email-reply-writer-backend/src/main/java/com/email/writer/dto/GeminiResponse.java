package com.email.writer.dto;

public record GeminiResponse(
    Candidate[] candidates
) {
    public record Candidate(Content content) {}
    public record Content(Part[] parts) {}
    public record Part(String text) {}
}


// Read it like this 👇

// “GeminiResponse has candidates”
// “Each candidate has content”
// “Content has parts”
// “Each part has text”

// That’s it. Nothing more.
// This code is just a mirror of the JSON shape.