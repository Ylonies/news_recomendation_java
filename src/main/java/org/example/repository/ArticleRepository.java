package org.example.repository;

import org.example.entity.Article;

import java.util.List;
import java.util.UUID;

public interface ArticleRepository {
    public List<Article> getLastArticles(UUID userId);
}
