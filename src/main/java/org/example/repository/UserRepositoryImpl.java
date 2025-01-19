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
        long userId = resultSet.getLong("user_id");
        User user = new User(UUID.nameUUIDFromBytes(Long.toString(userId).getBytes()), name, password);
        return Optional.of(user);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error saving user", e);
    }
    return Optional.empty();
  }

  public Optional<User> findByName(String name) {
    String sql = "SELECT * FROM users WHERE name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        User user = new User(
            UUID.nameUUIDFromBytes(Long.toString(resultSet.getLong("user_id")).getBytes()),
            resultSet.getString("name"),
            resultSet.getString("password")
        );
        return Optional.of(user);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error finding user by name", e);
    }
    return Optional.empty();
  }

  public boolean exists(String name) {
    String sql = "SELECT * FROM users WHERE name = ?";
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
