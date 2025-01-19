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
        return resultSet.next();  // Если строка существует, вернется true
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
    return null;
  }

  public void addUserCatalog(UUID userId, String catalogName) {
    // Сначала проверим, существует ли такой каталог в таблице catalogs
    String checkCatalogSql = "SELECT catalog_id FROM catalogs WHERE name = ?";
    UUID catalogId = null;
    try (Connection connection = dataSource.getConnection();
         PreparedStatement checkCatalogStatement = connection.prepareStatement(checkCatalogSql)) {
      checkCatalogStatement.setString(1, catalogName);
      try (ResultSet resultSet = checkCatalogStatement.executeQuery()) {
        if (resultSet.next()) {
          // Если каталог существует, получаем его ID
          catalogId = (UUID) resultSet.getObject("catalog_id");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error checking if catalog exists", e);
    }

    // Если каталог не существует, создаем новый каталог в таблице catalogs
    if (catalogId == null) {
      String insertCatalogSql = "INSERT INTO catalogs (name) VALUES (?) RETURNING catalog_id";
      try (Connection connection = dataSource.getConnection();
           PreparedStatement insertCatalogStatement = connection.prepareStatement(insertCatalogSql)) {
        insertCatalogStatement.setString(1, catalogName);
        try (ResultSet resultSet = insertCatalogStatement.executeQuery()) {
          if (resultSet.next()) {
            catalogId = (UUID) resultSet.getObject("catalog_id");
          }
        }
      } catch (SQLException e) {
        throw new RuntimeException("Error inserting new catalog", e);
      }
    }

    // Теперь добавляем этот каталог в таблицу user_catalog для данного пользователя
    String insertUserCatalogSql = "INSERT INTO user_catalog (user_id, catalog_id) VALUES (?, ?)";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement insertUserCatalogStatement = connection.prepareStatement(insertUserCatalogSql)) {
      insertUserCatalogStatement.setObject(1, userId, Types.OTHER);
      insertUserCatalogStatement.setObject(2, catalogId, Types.OTHER);
      insertUserCatalogStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error adding catalog to user", e);
    }
  }

  public void addToUser(UUID userId, UUID catalogId) {
    // Проверим, существует ли каталог в таблице catalogs
    String checkCatalogSql = "SELECT 1 FROM catalogs WHERE catalog_id = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement checkCatalogStatement = connection.prepareStatement(checkCatalogSql)) {
      checkCatalogStatement.setObject(1, catalogId, Types.OTHER);
      try (ResultSet resultSet = checkCatalogStatement.executeQuery()) {
        if (!resultSet.next()) {
          throw new IllegalArgumentException("Catalog with ID " + catalogId + " does not exist.");
        }

        // Если каталог существует, добавляем его в user_catalog
        String insertUserCatalogSql = "INSERT INTO user_catalog (user_id, catalog_id) VALUES (?, ?)";
        try (PreparedStatement insertUserCatalogStatement = connection.prepareStatement(insertUserCatalogSql)) {
          insertUserCatalogStatement.setObject(1, userId, Types.OTHER);
          insertUserCatalogStatement.setObject(2, catalogId, Types.OTHER);
          insertUserCatalogStatement.executeUpdate();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error adding existing catalog to user", e);
    }
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

  public static void main(String[] args) {
    CatalogRepositoryImpl repository = new CatalogRepositoryImpl();
    UUID userId = (UUID.fromString("276c22e1-43be-4e9c-9516-736fa350c711"));

    // Добавляем каталог для пользователя
    String catalogName = "имя каталога";
    repository.addUserCatalog(userId, catalogName);
    System.out.println("Catalog added for user: " + catalogName);

    // Получаем каталоги пользователя
    List<Catalog> userCatalogs = repository.getUserCatalogs(userId);
    System.out.println("User catalogs:");
    userCatalogs.forEach(catalog -> System.out.println("Catalog name: " + catalog.getName()));

    // Проверяем, существует ли каталог по имени для пользователя
    boolean exists = repository.existsByName(userId, catalogName);
    System.out.println("Catalog exists by name '" + catalogName + "': " + exists);

    // Получаем базовые каталоги
    List<Catalog> basicCatalogs = repository.getBasicCatalogs();
    System.out.println("Basic catalogs:");
    basicCatalogs.forEach(catalog -> System.out.println("Catalog name: " + catalog.getName()));

    // Получаем каталог по имени для пользователя
    Catalog catalogByName = repository.getByName(userId, catalogName);
    if (catalogByName != null) {
      System.out.println("Catalog found by name '" + catalogName + "': " + catalogByName.getName());
    } else {
      System.out.println("Catalog not found by name '" + catalogName + "'.");
    }

    // Удаляем каталог по имени для пользователя
    repository.deleteByName(userId, catalogName);
    System.out.println("Catalog '" + catalogName + "' deleted for user.");
  }

}
