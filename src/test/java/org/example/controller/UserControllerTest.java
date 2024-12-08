package org.example.controller;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends ControllerTest {
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