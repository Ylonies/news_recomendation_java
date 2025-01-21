package org.example.repository.interfaces;

import org.example.entity.Article;
import org.example.entity.Catalog;
import org.example.entity.Website;

import java.util.List;
import java.util.UUID;

public interface ArticleRepository {
    List<Article> getNewArticles(UUID userId, List<Catalog> catalogs, List<Website> websites);
    void saveArticle(Article article);
    void updateUserLastRequestTime(UUID userId);
    void saveArticleCategory(UUID articleId, UUID catalogId, UUID websiteId);
}
