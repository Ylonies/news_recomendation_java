package org.example.repository;

import org.example.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl {
  private final DataSource dataSource;

  public UserRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Optional<User> save(String name, String password) {
    String sql = "INSERT INTO users (name, password) VALUES (?, ?) RETURNING user_id";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      statement.setString(2, password);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        UUID userId = (UUID) resultSet.getObject("user_id");
        User user = new User(userId, name, password);
        return Optional.of(user);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error saving user", e);
    }
    return Optional.empty();
  }

  private User getById(UUID userId) throws SQLException {
    String sql = "SELECT user_id, name, password FROM users WHERE user_id = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, userId);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          UUID extractedUserId = (UUID) resultSet.getObject("user_id");
          String name = resultSet.getString("name");
          String password = resultSet.getString("password");
          return new User(extractedUserId, name, password);
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new SQLException("Error fetching user by ID", e);
    }
  }

  public Optional<User> findByName(String name) {
    String sql = "SELECT user_id, name, password FROM users WHERE name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        UUID userId = (UUID) resultSet.getObject("user_id");
        String password = resultSet.getString("password");
        User user = new User(userId, name, password);
        return Optional.of(user);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error finding user by name", e);
    }
    return Optional.empty();
  }

  public boolean exists(String name) {
    String sql = "SELECT 1 FROM users WHERE name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      ResultSet resultSet = statement.executeQuery();
      return resultSet.next();
    } catch (SQLException e) {
      throw new RuntimeException("Error checking if user exists", e);
    }
  }
}
