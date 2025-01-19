package org.example.repository;

import org.example.entity.Article;

import java.util.List;
import java.util.UUID;

public class MockArticleRepository implements  ArticleRepository{

    @Override
    public List<Article> getLastArticles(UUID userId) {
        return List.of();
    }
}
