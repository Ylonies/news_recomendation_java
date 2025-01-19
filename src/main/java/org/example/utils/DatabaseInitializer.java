package org.example.utils;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

  private final DataSource dataSource;
  private final String file = "database/migration/tables.sql";

  public DatabaseInitializer(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void initializeDatabase() {
    String sql = loadSQLFromFile();
    if (sql == null || sql.isEmpty()) {
      throw new RuntimeException("SQL schema file is empty or not found.");
    }

    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {

      statement.execute(sql);
      System.out.println("Database schema initialized successfully.");
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize database schema", e);
    }
  }

  private String loadSQLFromFile() {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file)) {
      if (inputStream == null) {
        throw new RuntimeException("File not found: " + "tables.sql");
      }
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        return reader.lines().collect(Collectors.joining("\n"));
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to read SQL file: " + "tables.sql", e);
    }
  }
}
