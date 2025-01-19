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

class InfoqParserTest {
  private ClassLoader classLoader;
  private InfoqParser parser;

  @BeforeEach
  void beforeEach() {
    classLoader = Thread.currentThread().getContextClassLoader();
    parser = new InfoqParser();
  }

  public Document getPage(String path) {
    InputStream page = classLoader.getResourceAsStream(path);

    try {
      return Jsoup.parse(page, "UTF-8", "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void getArticleLinks() {
    List<String> links = parser.getArticleLinks(getPage("org/example/parser/InfoqParserTest/mainPage.html"));

    assertAll(
        () -> assertEquals(15, links.size(), "Assert links count"),
        () -> assertEquals("/news/2025/01/microservices-llms-topologies/", links.getFirst(), "Assert first link")
    );
  }

  @Test
  void getArticle() {
    Article article = parser.getArticle("", getPage("org/example/parser/InfoqParserTest/articlePage.html"));

    assertAll(
        () -> assertEquals("Express 5.0 Released, Focuses on Stability and Security", article.name(), "Assert article name"),
        () -> assertEquals("A monthly overview of things you need to know as an architect or aspiring architect.", article.description().substring(0, 84), "Assert article description"),
        () -> assertEquals("Jan 11, 2025 2 min read", article.date(), "Assert article date")
    );
  }
}