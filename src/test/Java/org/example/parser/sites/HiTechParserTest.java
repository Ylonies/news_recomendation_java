package org.example.parser.sites;

import org.example.parser.ParserDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HiTechParserTest extends ParserDownloader {
  private HiTechParser parser;

  @BeforeEach
  @Override
  protected void beforeEach() {
    super.beforeEach();
    parser = new HiTechParser();
  }

  @Test
  void getArticleLinks() {
    List<String> links = parser.getArticleLinks(getPage("org/example/parser/HiTechParserTest/mainPage.html"));

    assertAll(
        () -> assertEquals(20, links.size(), "Assert links count"),
        () -> assertEquals("https://hi-tech.mail.ru/news/121136-v-rossii-sozdali-biosovmestimyj-elektrod-dlya-stimulyacii-nervnoj-tkani/", links.getFirst(), "Assert first link")
    );
  }

  @Test
  void getArticle() {
    Article article = parser.getArticle("", getPage("org/example/parser/HiTechParserTest/articlePage.html"));

    assertAll(
        () -> assertEquals("В России создали биосовместимый электрод для стимуляции нервной ткани", article.name(), "Assert article name"),
        () -> assertEquals("", article.description(), "Assert article description"),
        () -> assertEquals("2025-01-19T16:29:13+03:00", article.date(), "Assert article date")
    );
  }
}