package org.example.parser;

import java.util.List;

public record Article(
    String name,
    String description,
    String date,
    String link
) {}
