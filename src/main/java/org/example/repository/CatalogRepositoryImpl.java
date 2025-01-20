package org.example.repository;

import org.example.entity.Catalog;
import org.example.utils.DataSourceConfig;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CatalogRepositoryImpl implements CatalogRepository {
  private final DataSource dataSource = DataSourceConfig.getDataSource();

  @Override
  public List<Catalog> getBasicCatalogs() {
    String sql = "SELECT catalog_id, name FROM catalogs WHERE is_basic = true";
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
  public void deleteByName(UUID userId, String name) {
    if (!existsByName(userId, name)) {
      throw new RuntimeException("Catalog does not exist for the user");
    }

    String deleteUserCatalogSql = "DELETE FROM user_catalog WHERE user_id = ? AND catalog_id = (SELECT catalog_id FROM catalogs WHERE name = ?)";

    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement deleteUserCatalogStatement = connection.prepareStatement(deleteUserCatalogSql)) {
        deleteUserCatalogStatement.setObject(1, userId);
        deleteUserCatalogStatement.setString(2, name);
        deleteUserCatalogStatement.executeUpdate();
      }

      String deleteCatalogSql = "DELETE FROM catalogs WHERE catalog_id = (SELECT catalog_id FROM catalogs WHERE name = ?)";
      try (PreparedStatement deleteCatalogStatement = connection.prepareStatement(deleteCatalogSql)) {
        deleteCatalogStatement.setString(1, name);
        deleteCatalogStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error deleting catalog", e);
    }
  }


  @Override
  public Catalog addToUser(UUID userId, String name) {
    String checkCatalogSql = "SELECT catalog_id, name FROM catalogs WHERE name = ? AND is_basic = true";
    String insertUserCatalogSql = "INSERT INTO user_catalog (user_id, catalog_id, name) VALUES (?, ?, ?)";

    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement checkStatement = connection.prepareStatement(checkCatalogSql)) {
        checkStatement.setString(1, name);
        try (ResultSet resultSet = checkStatement.executeQuery()) {
          if (resultSet.next()) {
            UUID catalogId = (UUID) resultSet.getObject("catalog_id");

            try (PreparedStatement userWebsiteStatement = connection.prepareStatement(insertUserCatalogSql)) {
              userWebsiteStatement.setObject(1, userId);
              userWebsiteStatement.setObject(2, catalogId);
              userWebsiteStatement.setString(3, name);
              userWebsiteStatement.executeUpdate();
            }
            return new Catalog(catalogId, name, userId);
          } else {
            throw new RuntimeException("Catalog does not exist or is not basic (is_basic = true).");
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error saving catalog for user", e);
    }
  }

  @Override
  public Catalog addUserCatalog(UUID userId, String name) {
    String checkCatalogSql = "SELECT catalog_id, name FROM catalogs WHERE name = ?";

    String insertUserCatalogSql = "INSERT INTO user_catalog (user_id, catalog_id, name) VALUES (?, ?, ?)";

    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement checkStatement = connection.prepareStatement(checkCatalogSql)) {
        checkStatement.setString(1, name);
        try (ResultSet resultSet = checkStatement.executeQuery()) {
          if (resultSet.next()) {
            UUID catalogId = (UUID) resultSet.getObject("catalog_id");
            String catalogName = resultSet.getString("name");

            String checkUserCatalogSql = "SELECT 1 FROM user_catalog WHERE user_id = ? AND catalog_id = ?";
            try (PreparedStatement checkUserCatalogStatement = connection.prepareStatement(checkUserCatalogSql)) {
              checkUserCatalogStatement.setObject(1, userId);
              checkUserCatalogStatement.setObject(2, catalogId);

              try (ResultSet checkResultSet = checkUserCatalogStatement.executeQuery()) {
                if (!checkResultSet.next()) {
                  try (PreparedStatement userCatalogStatement = connection.prepareStatement(insertUserCatalogSql)) {
                    userCatalogStatement.setObject(1, userId);
                    userCatalogStatement.setObject(2, catalogId);
                    userCatalogStatement.setString(3, catalogName);
                    userCatalogStatement.executeUpdate();
                  }
                }
              }
            }

            return new Catalog(catalogId, catalogName, userId);
          } else {
            String insertCatalogSql = "INSERT INTO catalogs (name, is_basic) VALUES (?, ?) RETURNING catalog_id";
            try (PreparedStatement insertCatalogStatement = connection.prepareStatement(insertCatalogSql)) {
              insertCatalogStatement.setString(1, name);
              insertCatalogStatement.setBoolean(2, false);

              try (ResultSet insertResultSet = insertCatalogStatement.executeQuery()) {
                if (insertResultSet.next()) {
                  UUID catalogId = (UUID) insertResultSet.getObject("catalog_id");

                  try (PreparedStatement userCatalogStatement = connection.prepareStatement(insertUserCatalogSql)) {
                    userCatalogStatement.setObject(1, userId);
                    userCatalogStatement.setObject(2, catalogId);
                    userCatalogStatement.setString(3, name);
                    userCatalogStatement.executeUpdate();
                  }

                  return new Catalog(catalogId, name, userId);
                }
              }
            }
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error adding user catalog", e);
    }
    return null;
  }
}
