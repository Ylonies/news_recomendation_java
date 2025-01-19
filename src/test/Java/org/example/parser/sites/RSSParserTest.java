package org.example.parser.sites;

import org.example.parser.Article;
import org.example.parser.ParserDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RSSParserTest extends ParserDownloader {
  private RSSParser parser;

  @BeforeEach
  @Override
  protected void beforeEach() {
    super.beforeEach();
    parser = new RSSParser("");
  }

  @Test
  void parseLastArticles() {
    List<Article> articles = parser.parseLastArticles(getPage("org/example/parser/RSSParserTest/mainPage.xml"));
    Article article = articles.getFirst();

    assertAll(
        () -> assertEquals("Санду вручили Почетную премию Нансена", article.name(), "Assert article name"),
        () -> assertEquals("Очередная награда в копилку президента", article.description(), "Assert article description"),
        () -> assertEquals("Sat, 18 Jan 2025 18:28:58 +0300", article.date(), "Assert article date"),
        () -> assertEquals("https://bloknot-moldova.ru/news/sandu-vruchili-pochetnuyu-premiyu-nansena-1816441", article.link(), "Assert article link")
    );
  }
}