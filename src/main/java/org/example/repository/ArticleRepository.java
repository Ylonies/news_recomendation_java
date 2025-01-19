package org.example.repository;

import org.example.entity.Article;

import java.util.List;
import java.util.UUID;

public interface ArticleRepository {
    List<Article> getNewArticles(UUID userId);
    void saveArticle(Article article);
    void updateUserLastRequestTime(UUID userId);
}
