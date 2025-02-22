package org.example.controller;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WebsiteControllerTest extends ControllerTest {
    private static final String WEBSITE_URL = BASE_URL + "/website";
    private static final String EXAMPLE_URL = "/itproger.com";

    @Test
    public void testGetWebsiteEndpoint() throws IOException {
        int responseCode = sendGetRequest(WEBSITE_URL + "/");
        assertEquals(501, responseCode);
    }

    @Test
    public void testGetWithUrlEndpoint() throws IOException {
        int responseCode = sendGetRequest(WEBSITE_URL + EXAMPLE_URL);
        assertEquals(501, responseCode);
    }

    @Test
    public void testPostWithUrlEndpoint() throws IOException {
        int responseCode = sendPostRequest(WEBSITE_URL + EXAMPLE_URL);
        assertEquals(501, responseCode); // Проверяем, что ответ успешный
    }

    @Test
    public void testDeleteWithUrlEndpoint() throws IOException {
        int responseCode = sendDeleteRequest(WEBSITE_URL + EXAMPLE_URL);
        assertEquals(501, responseCode); // Проверяем, что ответ успешный
    }
}