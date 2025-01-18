package org.example.parser;

public record Article(
    String name,
    String description,
    String date,
    String link
) {}