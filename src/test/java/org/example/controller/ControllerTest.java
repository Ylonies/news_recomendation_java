package org.example.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

abstract class ControllerTest {
    static final String BASE_URL = "http://localhost:4567/";
    protected static Thread serverThread;

    @BeforeEach
    public void setUp() {
        ControllerManager manager = new ControllerManager();
        serverThread = new Thread(manager::startAll);
        serverThread.start();
        try {
            Thread.sleep(1000); // Увеличьте время, если сервер запускается дольше
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

     public int sendPostRequest(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.getOutputStream().write(new byte[0]);
        return connection.getResponseCode();
    }

    public int sendGetRequest(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection.getResponseCode();
    }

    public int sendDeleteRequest(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.connect();
        return connection.getResponseCode();
    }

    @AfterEach
    public void tearDown() {
        serverThread.interrupt(); // Прерываем поток сервера
    }
}
