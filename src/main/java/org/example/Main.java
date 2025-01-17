package org.example;
import org.example.controller.ControllerManager;

import static spark.Spark.port;

public class Main {
  public static void main(String[] args) {
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
