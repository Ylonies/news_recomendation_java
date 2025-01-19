package org.example.repository;

import org.example.entity.Website;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebsiteRepositoryImpl implements WebsiteRepository {
  private final DataSource dataSource;

  public WebsiteRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<Website> getBasicWebsites() {
    String sql = "SELECT * FROM websites";
    List<Website> websites = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        UUID websiteId = UUID.fromString(resultSet.getString("website_id"));
        String name = resultSet.getString("url");
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
    String sql = "SELECT w.website_id, w.name " +
        "FROM websites w " +
        "JOIN user_website uw ON uw.website_id = w.website_id " +
        "WHERE uw.user_id = ?";
    List<Website> websites = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, userId.toString());
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          UUID websiteId = UUID.fromString(resultSet.getString("catalog_id"));
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
      statement.setString(1, userId.toString());
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
    String sql = "SELECT * FROM websites w" +
        "WHERE w.name = ?";
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
    String sql = "SELECT w.website_id, w.name " +
        "FROM websites w " +
        "JOIN user_website uw ON uw.website_id = w.website_id " +
        "WHERE uw.user_id = ? AND w.name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, userId.toString());
      statement.setString(2, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          UUID websiteId = UUID.fromString(resultSet.getString("website_id"));
          String websiteName = resultSet.getString("name");
          return new Website(websiteId, websiteName, null, userId);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error finding website by name", e);
    }
    return null;
  }

  @Override
  public Website addByName(UUID userId, String name) {
    String sql = "INSERT INTO websites (name) VALUES (?) RETURNING website_id";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          UUID websiteId = UUID.fromString(resultSet.getString("website_id"));
          String userCatalogSql = "INSERT INTO user_website (user_id, website_id) VALUES (?, ?)";
          try (PreparedStatement userWebsiteStatement = connection.prepareStatement(userCatalogSql)) {
            userWebsiteStatement.setString(1, userId.toString());
            userWebsiteStatement.setString(2, websiteId.toString());
            userWebsiteStatement.executeUpdate();
          }
          return new Website(websiteId, name, null, userId);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error saving website", e);
    }
    return null;
  }

  @Override
  public void deleteByName(UUID userId, String name) {
    String sql = "DELETE FROM websites w USING user_website uw " +
        "WHERE w.website_id = uw.website_id " +
        "AND uw.user_id = ? AND w.name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, userId.toString());
      statement.setString(2, name);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error deleting website", e);
    }
  }
}
