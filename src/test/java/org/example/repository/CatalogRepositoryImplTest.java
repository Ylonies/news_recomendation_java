package org.example.repository;

import org.example.entity.Catalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CatalogRepositoryImplTest {
  private DataSource dataSource;
  private CatalogRepositoryImpl catalogRepository;

  @BeforeEach
  void setUp() {
    dataSource = mock(DataSource.class);
    catalogRepository = new CatalogRepositoryImpl(dataSource);
  }

  @Test
  void testGetBasicCatalogs() throws SQLException {
    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);

    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getString("catalog_id")).thenReturn(UUID.randomUUID().toString());
    when(resultSet.getString("name")).thenReturn("Catalog 1");

    List<Catalog> catalogs = catalogRepository.getBasicCatalogs();

    assert catalogs.size() == 1;
    assert catalogs.getFirst().getName().equals("Catalog 1");

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

    assert catalogs.size() == 1;
    assert catalogs.getFirst().getName().equals("User Catalog 1");

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

    when(resultSet.next()).thenReturn(true);

    boolean exists = catalogRepository.existsByName(userId, catalogName);

    assert exists;

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

    assert catalog != null;
    assert catalog.getName().equals(catalogName);

    verify(dataSource).getConnection();
    verify(connection).prepareStatement(anyString());
    verify(statement).executeQuery();
  }

}
