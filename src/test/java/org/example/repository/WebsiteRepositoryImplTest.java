package org.example.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.entity.Website;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.UUID;

class WebsiteRepositoryImplTest {

  private WebsiteRepositoryImpl websiteRepository;
  private PreparedStatement preparedStatementMock;
  private ResultSet resultSetMock;

  @BeforeEach
  void setUp() throws SQLException {
    DataSource dataSourceMock = mock(DataSource.class);
    Connection connectionMock = mock(Connection.class);
    preparedStatementMock = mock(PreparedStatement.class);
    resultSetMock = mock(ResultSet.class);

    when(dataSourceMock.getConnection()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    websiteRepository = new WebsiteRepositoryImpl(dataSourceMock);
  }

  @Test
  void testGetBasicWebsites() throws SQLException {
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    when(resultSetMock.next()).thenReturn(true).thenReturn(false);
    when(resultSetMock.getString("website_id")).thenReturn(UUID.randomUUID().toString());
    when(resultSetMock.getString("url")).thenReturn("http://example.com");

    List<Website> websites = websiteRepository.getBasicWebsites();

    assertNotNull(websites);
    assertEquals(1, websites.size());
    assertEquals("http://example.com", websites.get(0).getUrl());
  }

  @Test
  void testExistsByName_WhenWebsiteExists() throws SQLException {
    String name = "example.com";

    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    when(resultSetMock.next()).thenReturn(true);

    boolean exists = websiteRepository.existsByName(name);

    assertTrue(exists);
  }

  @Test
  void testExistsByName_WhenWebsiteDoesNotExist() throws SQLException {
    String name = "nonexistent.com";

    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    when(resultSetMock.next()).thenReturn(false);

    boolean exists = websiteRepository.existsByName(name);

    assertFalse(exists);
  }

  @Test
  void testAddByName() throws SQLException {
    UUID userId = UUID.randomUUID();
    String name = "example.com";

    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getString("website_id")).thenReturn(UUID.randomUUID().toString());

    Website website = websiteRepository.addToUser(userId, name);

    assertNotNull(website);
    verify(preparedStatementMock, times(1)).executeQuery();
  }

  @Test
  void testDeleteByName() throws SQLException {
    UUID userId = UUID.randomUUID();
    String name = "example.com";

    when(preparedStatementMock.executeUpdate()).thenReturn(1);

    websiteRepository.deleteByName(userId, name);

    verify(preparedStatementMock, times(1)).executeUpdate();
  }


  @Test
  void testGetByName() throws SQLException {
    UUID userId = UUID.randomUUID();
    String name = "example.com";

    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getString("website_id")).thenReturn(UUID.randomUUID().toString());
    when(resultSetMock.getString("name")).thenReturn(name);

    Website website = websiteRepository.getByName(userId, name);

    assertNotNull(website);
  }

  @Test
  void testAddedByName_WhenWebsiteExists() throws SQLException {
    UUID userId = UUID.randomUUID();
    String name = "example.com";

    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    when(resultSetMock.next()).thenReturn(true);

    boolean added = websiteRepository.addedByName(userId, name);

    assertTrue(added);
  }

  @Test
  void testAddedByName_WhenWebsiteDoesNotExist() throws SQLException {
    UUID userId = UUID.randomUUID();
    String name = "nonexistent.com";

    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    when(resultSetMock.next()).thenReturn(false);

    boolean added = websiteRepository.addedByName(userId, name);

    assertFalse(added);
  }
}
