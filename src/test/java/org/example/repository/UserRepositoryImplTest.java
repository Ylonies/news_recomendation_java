//package org.example.repository;
//
//import org.example.entity.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.sql.DataSource;
//import java.sql.*;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserRepositoryImplTest {
//
//  private DataSource dataSource;
//  private UserRepositoryImpl userRepository;
//
//  @BeforeEach
//  public void setUp() {
//    dataSource = mock(DataSource.class);
//    userRepository = new UserRepositoryImpl(dataSource);
//  }
//
//  @Test
//  void testSave() throws SQLException {
//    String name = "John";
//    String password = "password123";
//
//    Connection connection = mock(Connection.class);
//    PreparedStatement statement = mock(PreparedStatement.class);
//    ResultSet resultSet = mock(ResultSet.class);
//
//    when(dataSource.getConnection()).thenReturn(connection);
//    when(connection.prepareStatement(anyString())).thenReturn(statement);
//    when(statement.executeQuery()).thenReturn(resultSet);
//    when(resultSet.next()).thenReturn(true);
//    when(resultSet.getLong("user_id")).thenReturn(1L);
//
//    Optional<User> user = userRepository.save(name, password);
//
//    assertTrue(user.isPresent());
//    assertEquals(name, user.get().getName());
//    assertEquals(password, user.get().getPassword());
//  }
//
//  @Test
//  void testFindByName() throws SQLException {
//    String name = "John";
//
//    Connection connection = mock(Connection.class);
//    PreparedStatement statement = mock(PreparedStatement.class);
//    ResultSet resultSet = mock(ResultSet.class);
//
//    when(dataSource.getConnection()).thenReturn(connection);
//    when(connection.prepareStatement(anyString())).thenReturn(statement);
//    when(statement.executeQuery()).thenReturn(resultSet);
//    when(resultSet.next()).thenReturn(true);
//    when(resultSet.getLong("user_id")).thenReturn(1L);
//    when(resultSet.getString("name")).thenReturn(name);
//    when(resultSet.getString("password")).thenReturn("password123");
//
//    Optional<User> user = userRepository.findByName(name);
//
//    assertTrue(user.isPresent());
//    assertEquals(name, user.get().getName());
//  }
//
//  @Test
//  void testFindByNameNotFound() throws SQLException {
//    String name = "John";
//
//    Connection connection = mock(Connection.class);
//    PreparedStatement statement = mock(PreparedStatement.class);
//    ResultSet resultSet = mock(ResultSet.class);
//
//    when(dataSource.getConnection()).thenReturn(connection);
//    when(connection.prepareStatement(anyString())).thenReturn(statement);
//    when(statement.executeQuery()).thenReturn(resultSet);
//    when(resultSet.next()).thenReturn(false);
//
//    Optional<User> user = userRepository.findByName(name);
//
//    assertFalse(user.isPresent());
//  }
//
//  @Test
//  void testExists() throws SQLException {
//    String name = "John";
//
//    Connection connection = mock(Connection.class);
//    PreparedStatement statement = mock(PreparedStatement.class);
//    ResultSet resultSet = mock(ResultSet.class);
//
//    when(dataSource.getConnection()).thenReturn(connection);
//    when(connection.prepareStatement(anyString())).thenReturn(statement);
//    when(statement.executeQuery()).thenReturn(resultSet);
//    when(resultSet.next()).thenReturn(true);
//
//    assertTrue(userRepository.exists(name));
//  }
//
//  @Test
//  void testExistsNotFound() throws SQLException {
//    String name = "John";
//
//    Connection connection = mock(Connection.class);
//    PreparedStatement statement = mock(PreparedStatement.class);
//    ResultSet resultSet = mock(ResultSet.class);
//
//    when(dataSource.getConnection()).thenReturn(connection);
//    when(connection.prepareStatement(anyString())).thenReturn(statement);
//    when(statement.executeQuery()).thenReturn(resultSet);
//    when(resultSet.next()).thenReturn(false);
//
//    assertFalse(userRepository.exists(name));
//  }
//}
