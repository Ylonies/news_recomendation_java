package org.example.service.tests;

import org.example.service.CatalogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatalogServiceTest extends ServiceTest {
    private static final String CATALOG_URL = BASE_URL + "/catalog";
    private static final String EXAMPLE_NAME = "/ML";

    @Test
    public void testGetCatalogEndpoint() throws IOException {
        int responseCode = sendGetRequest(CATALOG_URL + "/");
        assertEquals(501, responseCode);
    }

    @Test
    public void testGetWithNameEndpoint() throws IOException {
        int responseCode = sendGetRequest(CATALOG_URL + EXAMPLE_NAME);
        assertEquals(501, responseCode);
    }

    @Test
    public void testPostWithNameEndpoint() throws IOException {
        int responseCode = sendPostRequest(CATALOG_URL + EXAMPLE_NAME);
        assertEquals(501, responseCode);
    }

    @Test
    public void testDeleteWithNameEndpoint() throws IOException {
        int responseCode = sendDeleteRequest(CATALOG_URL + EXAMPLE_NAME);
        assertEquals(501, responseCode);
    }
}