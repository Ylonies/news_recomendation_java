package org.example.repository;

import org.example.entity.Catalog;
import org.example.repository.Implementation.CatalogRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CatalogRepositoryImplTest {
  private DataSource dataSource;
  private CatalogRepositoryImpl catalogRepository;

  @BeforeEach
  void setUp() {
    dataSource = mock(DataSource.class);
    catalogRepository = new CatalogRepositoryImpl();
  }

  @Test
  void testGetBasicCatalogs() throws SQLException {
    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);

    UUID catalogId = UUID.randomUUID(); // Создаем UUID
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getObject("catalog_id")).thenReturn(catalogId); // Используем UUID
    when(resultSet.getString("name")).thenReturn("Catalog 1");

    List<Catalog> catalogs = catalogRepository.getBasicCatalogs();

    assertEquals(1, catalogs.size());
    assertEquals("Catalog 1", catalogs.get(0).getName()); // исправляем getFirst на get(0)

    verify(dataSource).getConnection();
    verify(connection).prepareStatement(anyString());
    verify(statement).executeQuery();
  }

  @Test
  void testExistsByName() throws SQLException {
    UUID userId = UUID.randomUUID();
    String catalogName = "Catalog 1";

    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);

    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getString("catalog_id")).thenReturn(UUID.randomUUID().toString());
    when(resultSet.getString("name")).thenReturn(catalogName);

    boolean exists = catalogRepository.existsByName(userId, catalogName);

    assertTrue(exists);

    verify(dataSource).getConnection();
    verify(connection).prepareStatement(anyString());
    verify(statement).executeQuery();
  }


  @Test
  void testGetUserCatalogs() throws SQLException {
    UUID userId = UUID.randomUUID();

    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);

    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getString("catalog_id")).thenReturn(UUID.randomUUID().toString());
    when(resultSet.getString("name")).thenReturn("User Catalog 1");

    List<Catalog> catalogs = catalogRepository.getUserCatalogs(userId);

    assertEquals(1, catalogs.size());
    assertEquals("User Catalog 1", catalogs.get(0).getName());

    verify(dataSource).getConnection();
    verify(connection).prepareStatement(anyString());
    verify(statement).executeQuery();
  }

  @Test
  void testGetByName() throws SQLException {
    UUID userId = UUID.randomUUID();
    String catalogName = "Catalog 1";

    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);

    when(resultSet.next()).thenReturn(true);
    when(resultSet.getString("catalog_id")).thenReturn(UUID.randomUUID().toString());
    when(resultSet.getString("name")).thenReturn(catalogName);

    Catalog catalog = catalogRepository.getByName(userId, catalogName);

    assertNotNull(catalog);
    assertEquals(catalogName, catalog.getName());

    verify(dataSource).getConnection();
    verify(connection).prepareStatement(anyString());
    verify(statement).executeQuery();
  }
}
