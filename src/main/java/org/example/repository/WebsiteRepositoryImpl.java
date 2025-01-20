package org.example.repository;

import org.example.entity.Website;
import org.example.utils.DataSourceConfig;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebsiteRepositoryImpl implements WebsiteRepository {
  private final DataSource dataSource = DataSourceConfig.getDataSource();

  @Override
  public List<Website> getBasicWebsites() {
    String sql = "SELECT website_id, name, url FROM websites WHERE is_basic = true";
    List<Website> websites = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        UUID websiteId = (UUID) resultSet.getObject("website_id");
        String name = resultSet.getString("name");
        String url = resultSet.getString("url");
        websites.add(new Website(websiteId, name, url, null));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching basic websites", e);
    }
    return websites;
  }

  @Override
  public List<Website> getUserWebsites(UUID userId) {
    String sql = "SELECT w.website_id, w.name, w.url " +
        "FROM websites w " +
        "JOIN user_website uw ON uw.website_id = w.website_id " +
        "WHERE uw.user_id = ?";
    List<Website> websites = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, userId);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          UUID websiteId = (UUID) resultSet.getObject("website_id");
          String name = resultSet.getString("name");
          String url = resultSet.getString("url");
          websites.add(new Website(websiteId, name, url, userId));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching websites for user", e);
    }
    return websites;
  }

  @Override
  public boolean addedByName(UUID userId, String name) {
    String sql = "SELECT 1 FROM websites w " +
        "JOIN user_website uw ON uw.website_id = w.website_id " +
        "WHERE uw.user_id = ? AND w.name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, userId);
      statement.setString(2, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error checking if website exists", e);
    }
  }

  @Override
  public boolean existsByName(String name) {
    String sql = "SELECT * FROM websites w WHERE w.name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error checking if basic catalog exists", e);
    }
  }

  @Override
  public Website getByName(UUID userId, String name) {
    String sql = "SELECT w.website_id, w.name, w.url " +
        "FROM websites w " +
        "JOIN user_website uw ON uw.website_id = w.website_id " +
        "WHERE uw.user_id = ? AND w.name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, userId);
      statement.setString(2, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          UUID websiteId = (UUID) resultSet.getObject("website_id");
          String websiteName = resultSet.getString("name");
          String url = resultSet.getString("url");
          return new Website(websiteId, websiteName, url, userId);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error finding website by name", e);
    }
    return null;
  }

  @Override
  public Website addToUser(UUID userId, String name) {
    if (!existsByName(name)) {
      throw new RuntimeException("Website does not exist");
    }
    String sql = "SELECT website_id, name, url FROM websites WHERE name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          UUID websiteId = (UUID) resultSet.getObject("website_id");
          String url = resultSet.getString("url");
          String userWebsiteSql = "INSERT INTO user_website (user_id, website_id, url) VALUES (?, ?, ?)";
          try (PreparedStatement userWebsiteStatement = connection.prepareStatement(userWebsiteSql)) {
            userWebsiteStatement.setObject(1, userId);
            userWebsiteStatement.setObject(2, websiteId);
            userWebsiteStatement.setString(3, url);
            userWebsiteStatement.executeUpdate();
          }
          return new Website(websiteId, name, url, userId);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error adding website to user", e);
    }
    return null;
  }

  @Override
  public Website addUserWebsite(UUID userId, String name, String url) {
    String checkWebsiteSql = "SELECT website_id FROM websites WHERE name = ? AND url = ?";
    String insertUserWebsiteSql = "INSERT INTO user_website (user_id, website_id, url, name) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement checkStatement = connection.prepareStatement(checkWebsiteSql)) {
        checkStatement.setString(1, name);
        checkStatement.setString(2, url);
        try (ResultSet resultSet = checkStatement.executeQuery()) {
          if (resultSet.next()) {
            UUID websiteId = (UUID) resultSet.getObject("website_id");

            try (PreparedStatement userWebsiteStatement = connection.prepareStatement(insertUserWebsiteSql)) {
              userWebsiteStatement.setObject(1, websiteId);
              userWebsiteStatement.setObject(2, userId);
              userWebsiteStatement.setString(3, url);
              userWebsiteStatement.setString(4, name);
              userWebsiteStatement.executeUpdate();
            }

            return new Website(websiteId, name, url, userId);
          } else {
            UUID newWebsiteId = UUID.randomUUID();
            try (PreparedStatement userWebsiteStatement = connection.prepareStatement(insertUserWebsiteSql)) {
              userWebsiteStatement.setObject(1, newWebsiteId);
              userWebsiteStatement.setObject(2, userId);
              userWebsiteStatement.setString(3, url);
              userWebsiteStatement.setString(4, name);
              userWebsiteStatement.executeUpdate();
            }

            return new Website(newWebsiteId, name, url, userId);
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error saving website for user", e);
    }
  }

  @Override
  public void deleteByName(UUID userId, String name) {
    String sql = "DELETE FROM websites w USING user_website uw " +
        "WHERE w.website_id = uw.website_id " +
        "AND uw.user_id = ? AND w.name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, userId);
      statement.setString(2, name);
      statement.executeUpdate();
    } catch (SQLException e)      {
      throw new RuntimeException("Error deleting website", e);
    }
  }
}
