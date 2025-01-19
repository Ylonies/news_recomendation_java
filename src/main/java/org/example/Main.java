package org.example;
import org.example.controller.ControllerManager;
import org.example.utils.DataSourceConfig;
import org.example.utils.DatabaseInitializer;

import static spark.Spark.port;

public class Main {
  public static void main(String[] args) {
    // Инициализация таблиц
    DatabaseInitializer databaseInitializer = new DatabaseInitializer(DataSourceConfig.getDataSource());
    databaseInitializer.initializeDatabase();
    // Создание экземпляра ControllerManager
    ControllerManager controllerManager = new ControllerManager();
    // Запуск всех контроллеров
    controllerManager.startAll();
    // Добавьте обработчик для завершения работы приложения
//    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//      controllerManager.stopAll();
//      System.out.println("Application stopped.");
//    }));

  }
}
