package org.example.parser.sites;

import org.example.parser.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InfoqParserTest extends ParserTest {
  private InfoqParser parser;

  @BeforeEach
  void beforeEach() {
    classLoader = Thread.currentThread().getContextClassLoader();
    parser = new InfoqParser();
  }

  @Test
  void getArticleLinks() {
    List<String> links = parser.getArticleLinks(getPage("org/example/parser/InfoqParserTest/mainPage.html"));

    assertAll(
        () -> assertEquals(15, links.size(), "Assert links count"),
        () -> assertEquals("https://www.infoq.com/news/2025/01/microservices-llms-topologies/", links.getFirst(), "Assert first link")
    );
  }

  @Test
  void getArticle() {
    Article article = parser.getArticle("", getPage("org/example/parse/InfoqParserTest/articlePage.html"));

    assertAll(
        () -> assertEquals("vlt Introduces New JavaScript Package Manager and Serverless Registry", article.name(), "Assert article name"),
        () -> assertEquals("A monthly overview of things you need to know as an architect or aspiring architect.", article.description().substring(0, 84), "Assert article description"),
        () -> assertEquals("Jan 10, 2025 1 min read", article.date(), "Assert article date")
    );
  }
}