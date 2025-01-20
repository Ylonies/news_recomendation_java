package org.example.repository;

import org.example.entity.Article;
import org.example.repository.Implementation.ArticleRepositoryImpl;
import org.example.repository.Implementation.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class ArticleRepositoryTest {

  @Test
  void testSaveArticleAndUpdateUserLastRequestTime() throws InterruptedException {
    ArticleRepositoryImpl repository = new ArticleRepositoryImpl();
    UserRepositoryImpl userRepository = new UserRepositoryImpl();

    userRepository.save("Test", "password");
    UUID userId = userRepository.findByName("Test").get().getId();

    Article article = new Article(UUID.randomUUID(), "Article Name", "Description of article", "2025-01-20", "https://example.com");
    repository.saveArticle(article);
    System.out.println("Article saved successfully.");

    repository.updateUserLastRequestTime(userId);
    System.out.println("User's last request time updated.");

    Thread.sleep(1000);

    Article article1 = new Article(UUID.randomUUID(), "Article Name", "Description of article", "2025-01-20", "https://example.com");
    repository.saveArticle(article1);

    var newArticles = repository.getNewArticles(userId);
    assertNotNull(newArticles);
    assertTrue(newArticles.size() > 0, "Should have new articles");

    newArticles.forEach(articleItem -> {
      System.out.println("New article: " + articleItem.name());
    });

    repository.updateUserLastRequestTime(userId);
    System.out.println("User's last request time updated again.");
  }
}
