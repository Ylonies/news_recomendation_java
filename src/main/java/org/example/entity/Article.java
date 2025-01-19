package org.example.entity;

import java.util.UUID;

public record Article(UUID id, String name, String description, String date, String link) {
}