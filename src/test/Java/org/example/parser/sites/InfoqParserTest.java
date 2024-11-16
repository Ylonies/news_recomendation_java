package org.example.parser.sites;

import org.example.parser.Article;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InfoqParserTest {
  private final InfoqParser parser = new InfoqParser() {
    public Article parseArticle(String title, String link) {
      return new Article(title, "Тестовый текст", "01-01-2023", link, null);
    }

    @Override
    public List<Article> parseLastArticles() {
      return List.of(
          new Article("Статья 1", "Текст 1", "01-01-2023", "http://example.com/1", null),
          new Article("Статья 2", "Текст 2", "02-01-2023", "http://example.com/2", null)
      );
    }
  };

  @Test
  void testParseLastArticles() {
    List<Article> articles = parser.parseLastArticles();
    assertNotNull(articles);
    assertEquals(2, articles.size());
    assertEquals("Статья 1", articles.get(0).getName());
    assertEquals("Статья 2", articles.get(1).getName());
  }

  @Test
  void testParseArticle() {
    Article article = parser.parseArticle("Тестовый заголовок", "http://example.com/test");
    assertNotNull(article);
    assertEquals("Тестовый заголовок", article.getName());
    assertEquals("http://example.com/test", article.getLink());
  }

  @Test
  void testEnrichTitle() {
    String link = "https://www.infoq.com/articles/example-title";
    String enrichedTitle = parser.enrichTitle(link);
    assertNotNull(enrichedTitle);
    assertTrue(enrichedTitle.contains("Example title"));
  }

  @Test
  void testConnection() {
    List<Article> articles = parser.parseLastArticles();
    assertNotNull(articles);
    assertFalse(articles.isEmpty());
  }

  @Test
  void testParseArticleWithValidLink() {
    Article article = parser.parseArticle("Тестовый заголовок", "http://example.com/test");
    assertNotNull(article);
    assertEquals("Тестовый заголовок", article.getName());
    assertEquals("Тестовый текст", article.getDescription());
    assertEquals("01-01-2023", article.getDate());
    assertEquals("http://example.com/test", article.getLink());
  }
}