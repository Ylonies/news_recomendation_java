package org.example.repository;

import org.example.entity.Catalog;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CatalogRepositoryImpl implements CatalogRepository {
  private final DataSource dataSource;

  public CatalogRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<Catalog> getBasicCatalogs() {
    String sql = "SELECT catalog_id, name FROM catalogs";
    List<Catalog> catalogs = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        UUID catalogId = (UUID) resultSet.getObject("catalog_id");
        String name = resultSet.getString("name");
        catalogs.add(new Catalog(catalogId, name, null));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching basic catalogs", e);
    }
    return catalogs;
  }

  @Override
  public List<Catalog> getUserCatalogs(UUID userId) {
    String sql = "SELECT c.catalog_id, c.name " +
        "FROM catalogs c " +
        "JOIN user_catalog uc ON uc.catalog_id = c.catalog_id " +
        "WHERE uc.user_id = ?";
    List<Catalog> catalogs = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, userId);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          UUID catalogId = (UUID) resultSet.getObject("catalog_id");
          String name = resultSet.getString("name");
          catalogs.add(new Catalog(catalogId, name, userId));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching catalogs for user", e);
    }
    return catalogs;
  }

  @Override
  public boolean existsByName(UUID userId, String name) {
    String sql = "SELECT 1 FROM catalogs c " +
        "JOIN user_catalog uc ON uc.catalog_id = c.catalog_id " +
        "WHERE uc.user_id = ? AND c.name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, userId);
      statement.setString(2, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error checking if catalog exists", e);
    }
  }

  @Override
  public Catalog getByName(UUID userId, String name) {
    String sql = "SELECT c.catalog_id, c.name " +
        "FROM catalogs c " +
        "JOIN user_catalog uc ON uc.catalog_id = c.catalog_id " +
        "WHERE uc.user_id = ? AND c.name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, userId);
      statement.setString(2, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          UUID catalogId = (UUID) resultSet.getObject("catalog_id");
          String catalogName = resultSet.getString("name");
          return new Catalog(catalogId, catalogName, userId);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error finding catalog by name", e);
    }
    return null;
  }

  @Override
  public Catalog addByName(UUID userId, String name) {
    String sql = "INSERT INTO catalogs (name) VALUES (?) RETURNING catalog_id";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          UUID catalogId = (UUID) resultSet.getObject("catalog_id");
          String userCatalogSql = "INSERT INTO user_catalog (user_id, catalog_id) VALUES (?, ?)";
          try (PreparedStatement userCatalogStatement = connection.prepareStatement(userCatalogSql)) {
            userCatalogStatement.setObject(1, userId);
            userCatalogStatement.setObject(2, catalogId);
            userCatalogStatement.executeUpdate();
          }
          return new Catalog(catalogId, name, userId);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error saving catalog", e);
    }
    return null;
  }

  @Override
  public void deleteByName(UUID userId, String name) {
    String sql = "DELETE FROM catalogs c USING user_catalog uc " +
        "WHERE c.catalog_id = uc.catalog_id " +
        "AND uc.user_id = ? AND c.name = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, userId);
      statement.setString(2, name);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error deleting catalog", e);
    }
  }
}
