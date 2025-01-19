package org.example.repository;

import org.example.entity.Catalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    // Мокаем подключение и результат запроса
    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);

    // Устанавливаем поведение для resultSet
    when(resultSet.next()).thenReturn(true).thenReturn(false); // Имитируем 1 строку в результате
    when(resultSet.getString("catalog_id")).thenReturn(UUID.randomUUID().toString());
    when(resultSet.getString("name")).thenReturn("Catalog 1");

    // Вызов метода
    List<Catalog> catalogs = catalogRepository.getBasicCatalogs();

    // Проверки
    assert catalogs.size() == 1;
    assert catalogs.get(0).getName().equals("Catalog 1");

    // Проверяем взаимодействие с mock-объектами
    verify(dataSource).getConnection();
    verify(connection).prepareStatement(anyString());
    verify(statement).executeQuery();
  }

  @Test
  void testGetUserCatalogs() throws SQLException {
    UUID userId = UUID.randomUUID();

    // Мокаем подключение и результат запроса
    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);

    // Устанавливаем поведение для resultSet
    when(resultSet.next()).thenReturn(true).thenReturn(false); // Имитируем 1 строку в результате
    when(resultSet.getString("catalog_id")).thenReturn(UUID.randomUUID().toString());
    when(resultSet.getString("name")).thenReturn("User Catalog 1");

    // Вызов метода
    List<Catalog> catalogs = catalogRepository.getUserCatalogs(userId);

    // Проверки
    assert catalogs.size() == 1;
    assert catalogs.get(0).getName().equals("User Catalog 1");

    // Проверяем взаимодействие с mock-объектами
    verify(dataSource).getConnection();
    verify(connection).prepareStatement(anyString());
    verify(statement).executeQuery();
  }

  @Test
  void testExistsByName() throws SQLException {
    UUID userId = UUID.randomUUID();
    String catalogName = "Catalog 1";

    // Мокаем подключение и результат запроса
    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);

    // Устанавливаем поведение для resultSet
    when(resultSet.next()).thenReturn(true); // Имитируем существование записи

    // Вызов метода
    boolean exists = catalogRepository.existsByName(userId, catalogName);

    // Проверки
    assert exists;

    // Проверяем взаимодействие с mock-объектами
    verify(dataSource).getConnection();
    verify(connection).prepareStatement(anyString());
    verify(statement).executeQuery();
  }

  @Test
  void testGetByName() throws SQLException {
    UUID userId = UUID.randomUUID();
    String catalogName = "Catalog 1";

    // Мокаем подключение и результат запроса
    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);

    // Устанавливаем поведение для resultSet
    when(resultSet.next()).thenReturn(true); // Имитируем существование записи
    when(resultSet.getString("catalog_id")).thenReturn(UUID.randomUUID().toString());
    when(resultSet.getString("name")).thenReturn(catalogName);

    // Вызов метода
    Catalog catalog = catalogRepository.getByName(userId, catalogName);

    // Проверки
    assert catalog != null;
    assert catalog.getName().equals(catalogName);

    // Проверяем взаимодействие с mock-объектами
    verify(dataSource).getConnection();
    verify(connection).prepareStatement(anyString());
    verify(statement).executeQuery();
  }

}
