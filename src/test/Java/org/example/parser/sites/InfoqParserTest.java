package org.example.parser.sites;

import org.example.parser.Article;
import org.example.parser.ParserDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InfoqParserTest extends ParserDownloader {
  private InfoqParser parser;

  @BeforeEach
  @Override
  protected void beforeEach() {
    super.beforeEach();
    parser = new InfoqParser();
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