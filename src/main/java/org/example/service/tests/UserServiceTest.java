package org.example.service.tests;

import org.example.service.Service;
import org.example.service.UserService;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest extends ServiceTest {
    private static final String USER_URL = BASE_URL + "/user";

    @Test
    public void testLoginEndpoint() throws IOException {
        int responseCode = sendPostRequest(USER_URL + "/login");
        assertEquals(501, responseCode);
    }

    @Test
    public void testRegisterEndpoint() throws IOException {
        int responseCode = sendPostRequest(USER_URL + "/register");
        assertEquals(501, responseCode);
    }

    @Test
    public void testGetCatalog() throws IOException {
        int responseCode = sendGetRequest(USER_URL + "/ML");
        assertEquals(501, responseCode); // Проверяем, что ответ успешный
    }

}