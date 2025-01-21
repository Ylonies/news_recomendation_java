package org.example;
import org.example.controller.ControllerManager;
import org.example.parser.ScheduledParserExecutor;
import org.example.utils.DataSourceConfig;
import org.example.utils.DatabaseInitializer;

public class Main {

  public static void main(String[] args) {
    // Инициализация таблиц
    DatabaseInitializer databaseInitializer = new DatabaseInitializer(DataSourceConfig.getDataSource());
    databaseInitializer.initializeDatabase();
    ControllerManager controllerManager = new ControllerManager();
    controllerManager.startAll();
    new ScheduledParserExecutor();

  }
}
