package org.example.parser.sites;

import org.example.parser.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThreeDNewsParserTest extends ParserTest {
  private ThreeDNewsParser parser;

  @BeforeEach
  void beforeEach() {
    classLoader = Thread.currentThread().getContextClassLoader();
    parser = new ThreeDNewsParser();
  }

  @Test
  void getArticleLinks() {
    List<String> links = parser.getArticleLinks(getPage("org/example/parse/ThreeDNewsParserTest/mainPage.html"));

    assertAll(
        () -> assertEquals(10, links.size(), "Assert links count"),
        () -> assertEquals("/1116855/s-1-marta-v-rossii-perestanut-rabotat-prilogenie-i-sistema-udalyonnogo-monitoringa-keenetic", links.getFirst(), "Assert first link")
    );
  }

  @Test
  void getArticle() {
    Article article = parser.getArticle("", getPage("org/example/parse/ThreeDNewsParserTest/articlePage.html"));

    assertAll(
        () -> assertEquals("В России перестанут работать мобильное приложение и сервисы Keenetic с 1 марта, но выход есть", article.name(), "Assert article name"),
        () -> assertEquals("С 1 марта 2025 года в России прекращают работу мобильное приложение", article.description().substring(0, 67), "Assert article description"),
        () -> assertEquals("16.01.2025 [17:17]", article.date(), "Assert article date")
    );
  }
}