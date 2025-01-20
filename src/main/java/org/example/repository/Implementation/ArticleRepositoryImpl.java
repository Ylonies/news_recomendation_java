package org.example.repository.Implementation;

import org.example.entity.Article;
import org.example.repository.interfaces.ArticleRepository;
import org.example.utils.DataSourceConfig;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArticleRepositoryImpl implements ArticleRepository {

  private final DataSource dataSource = DataSourceConfig.getDataSource();

  @Override
  public List<Article> getNewArticles(UUID userId) {
    String userLastRequestQuery = "SELECT time FROM user_time WHERE user_id = ?";
    String newArticlesQuery = "SELECT * FROM articles WHERE creation_time > ?";

    List<Article> newArticles = new ArrayList<>();

    try (Connection connection = dataSource.getConnection()) {
      Timestamp lastRequestTime = null;
      try (PreparedStatement statement = connection.prepareStatement(userLastRequestQuery)) {
        statement.setObject(1, userId);
        try (ResultSet resultSet = statement.executeQuery()) {
          if (resultSet.next()) {
            lastRequestTime = resultSet.getTimestamp("time");
          }
        }
      }

      if (lastRequestTime != null) {
        try (PreparedStatement statement = connection.prepareStatement(newArticlesQuery)) {
          statement.setTimestamp(1, lastRequestTime);
          try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
              UUID articleId = (UUID) resultSet.getObject("article_id");
              String articleName = resultSet.getString("name");
              String articleDescription = resultSet.getString("description");
              String articleDate = resultSet.getString("date");
              String articleLink = resultSet.getString("link");
              Article article = new Article(articleId, articleName, articleDescription, articleDate, articleLink);
              newArticles.add(article);
            }
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching new articles for user", e);
    }

    return newArticles;
  }

  @Override
  public void saveArticle(Article article) {
    if (article == null) {
      throw new IllegalArgumentException("Article cannot be null");
    }
    String insertArticle = "INSERT INTO articles (article_id, name, description, date, link) VALUES (?, ?, ?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(insertArticle)) {
      statement.setObject(1, article.id());
      statement.setString(2, article.name());
      statement.setString(3, article.description());
      statement.setString(4, article.date());
      statement.setString(5, article.link());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error saving article", e);
    }
  }

  @Override
  public void updateUserLastRequestTime(UUID userId) {
    String updateTimeQuery = "INSERT INTO user_time (user_id, time) VALUES (?, ?) ON CONFLICT (user_id) DO UPDATE SET time = EXCLUDED.time";

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(updateTimeQuery)) {
      statement.setObject(1, userId);
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error updating last request time for user", e);
    }
  }

  public static void main(String[] args) {
    // Создаем экземпляр репозитория
    ArticleRepositoryImpl repository = new ArticleRepositoryImpl();
    UserRepositoryImpl userRepository = new UserRepositoryImpl();
    userRepository.save("Test", "password");
    UUID userId = userRepository.findByName("Test").get().getId();


    Article article = new Article(UUID.randomUUID(), "Article Name", "Description of article", "2025-01-20", "https://example.com");
    repository.saveArticle(article);
    System.out.println("Article saved successfully.");

    repository.updateUserLastRequestTime(userId);
    System.out.println("User's last request time updated.");

    try {
      Thread.sleep(1000);
      Article article1 = new Article(UUID.randomUUID(), "Article Name", "Description of article", "2025-01-20", "https://example.com");
      repository.saveArticle(article1);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    repository.getNewArticles(userId).forEach(articleItem -> {
      System.out.println("New article: " + articleItem.name());
    });

    repository.updateUserLastRequestTime(userId);
    System.out.println("User's last request time updated again.");
  }
}
