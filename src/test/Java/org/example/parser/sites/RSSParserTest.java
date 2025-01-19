package org.example.parser.sites;

import org.example.parser.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RSSParserTest {
  private ClassLoader classLoader;
  private RSSParser parser;

  @BeforeEach
  void beforeEach() {
    classLoader = Thread.currentThread().getContextClassLoader();
    parser = new RSSParser("");
  }

  public Document getPage(String path) {
    InputStream page = classLoader.getResourceAsStream(path);

    try {
      return Jsoup.parse(page, "UTF-8", "", Parser.xmlParser());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void parseLastArticles() {
    List<Article> articles = parser.parseLastArticles(getPage("org/example/parser/RSSParser/mainPage.xml"));
    Article article = articles.getFirst();

    assertAll(
        () -> assertEquals("Санду вручили Почетную премию Нансена", article.name(), "Assert article name"),
        () -> assertEquals("Очередная награда в копилку президента", article.description(), "Assert article description"),
        () -> assertEquals("Sat, 18 Jan 2025 18:28:58 +0300", article.date(), "Assert article date"),
        () -> assertEquals("https://bloknot-moldova.ru/news/sandu-vruchili-pochetnuyu-premiyu-nansena-1816441", article.link(), "Assert article link")
    );
  }
}