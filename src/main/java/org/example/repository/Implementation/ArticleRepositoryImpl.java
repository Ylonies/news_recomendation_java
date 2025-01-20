package org.example.repository.Implementation;

import org.example.entity.Article;
import org.example.entity.Catalog;
import org.example.entity.Website;
import org.example.repository.interfaces.ArticleRepository;
import org.example.utils.DataSourceConfig;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ArticleRepositoryImpl implements ArticleRepository {

  private final DataSource dataSource = DataSourceConfig.getDataSource();

  @Override
  public List<Article> getNewArticles(UUID userId, List<Catalog> catalogs, List<Website> websites) {
    List<Article> newArticles = new ArrayList<>();

    try (Connection connection = dataSource.getConnection()) {
      Timestamp lastRequestTime = getUserLastRequestTime(userId);

      if (lastRequestTime != null) {
        // 1. Создание временной таблицы
        String createTempTableQuery = "CREATE TEMPORARY TABLE temp_articles AS " +
                "SELECT a.* FROM articles a " +
                "WHERE a.creation_time > ?";

        String dropTempTableQuery = "DROP TABLE IF EXISTS temp_articles";
        try (PreparedStatement dropTempTableStmt = connection.prepareStatement(dropTempTableQuery)) {
          dropTempTableStmt.executeUpdate();
        }

        try (PreparedStatement createTempTableStmt = connection.prepareStatement(createTempTableQuery)) {
          createTempTableStmt.setTimestamp(1, lastRequestTime);
          createTempTableStmt.executeUpdate();
        }

        // 2. Поиск по категориям
        String newArticlesQuery = "SELECT ta.* FROM temp_articles ta " +
                "JOIN article_category ac ON ta.article_id = ac.article_id " +
                "WHERE ac.catalog_id IN (" +
                catalogs.stream().map(c -> "?").collect(Collectors.joining(", ")) + ") " +
                "AND ac.website_id IN (" +
                websites.stream().map(w -> "?").collect(Collectors.joining(", ")) + ")";

        try (PreparedStatement statement = connection.prepareStatement(newArticlesQuery)) {
          // Устанавливаем значения для catalogIds
          int index = 1;
          for (Catalog catalog : catalogs) {
            statement.setObject(index++, catalog.getId());
          }
          // Устанавливаем значения для websiteIds
          for (Website website : websites) {
            statement.setObject(index++, website.getId());
          }

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
    String insertArticleCategory = "INSERT INTO article_category (article_id, catalog_id, website_id) VALUES (?, ?, ?)";

    try (Connection connection = dataSource.getConnection()) {
      // Вставка статьи в таблицу articles
      try (PreparedStatement statement = connection.prepareStatement(insertArticle)) {
        statement.setObject(1, article.id());
        statement.setString(2, article.name());
        statement.setString(3, article.description());
        statement.setString(4, article.date());
        statement.setString(5, article.link());
        statement.executeUpdate();
      }

      // Вставка в таблицу article_category
      try (PreparedStatement statement = connection.prepareStatement(insertArticleCategory)) {
        statement.setObject(1, article.id()); // Используем тот же article_id
        statement.setObject(2, article.catalogId()); // Предполагается, что у Article есть метод catalogId()
        statement.setObject(3, article.websiteId()); // Предполагается, что у Article есть метод websiteId()
        statement.executeUpdate();
      }
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

  public Timestamp getUserLastRequestTime(UUID userId) {
    String query = "SELECT time FROM user_time WHERE user_id = ?";
    Timestamp lastRequestTime = null;

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setObject(1, userId);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          lastRequestTime = resultSet.getTimestamp("time");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching last request time for user", e);
    }
    return lastRequestTime;
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

//    repository.getNewArticles(userId).forEach(articleItem -> {
//      System.out.println("New article: " + articleItem.name());
//    });
//
//    repository.updateUserLastRequestTime(userId);
//    System.out.println("User's last request time updated again.");
//  }
  }
}
