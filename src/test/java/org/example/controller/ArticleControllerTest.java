package org.example.controller;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArticleControllerTest extends ControllerTest {
    private static final String ARTICLE_URL = BASE_URL + "/article";
    private static final String EXAMPLE_ID = "/12345";

    @Test
    public void testGetWithIdEndpoint() throws IOException {
        int responseCode = sendGetRequest(ARTICLE_URL + EXAMPLE_ID);
        assertEquals(501, responseCode);
    }

    @Test
    public void testPostWithIdEndpoint() throws IOException {
        int responseCode = sendPostRequest(ARTICLE_URL + EXAMPLE_ID);
        assertEquals(501, responseCode);
    }

    @Test
    public void testDeleteWithIdEndpoint() throws IOException {
        int responseCode = sendDeleteRequest(ARTICLE_URL + EXAMPLE_ID);
        assertEquals(501, responseCode);
    }

    @Test
    public void testGetWithCatalogEndpoint() throws IOException {
        int responseCode = sendGetRequest(ARTICLE_URL + "/catalog/ML");
        assertEquals(501, responseCode);
    }
}