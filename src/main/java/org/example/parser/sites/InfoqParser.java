package org.example.parser.sites;

import org.example.parser.Article;
import org.example.parser.SiteParse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InfoqParser implements SiteParse {
  private static final int TIMEOUT = 20000;
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
  private static final String BLOG_LINK = "https://www.infoq.com/development/";
  private static final Logger log = LoggerFactory.getLogger(InfoqParser.class);
  private static final int THREAD_COUNT = 25;

  @Override
  public String getArticleTag() {
    return "Infoq";
  }

  @Override
  public List<Article> parseLastArticles() {
    final var articles = new ArrayList<Article>();
    ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    try {
      Document document = Jsoup.connect(BLOG_LINK)
          .timeout(TIMEOUT)
          .userAgent(USER_AGENT)
          .get();
      Elements titleElements = document.select("h4.card__title a");
      List<Callable<Article>> tasks = new ArrayList<>();
      for (Element titleElement : titleElements) {
        String link = titleElement.absUrl("href");
        if (!link.contains("news") && !link.contains("articles")) {
          continue;
        }
        String title = titleElement.attr("title");
        final String finalTitle = title.isEmpty() ? enrichTitle(link) : title;
        final String finalLink = link;
        tasks.add(() -> parseArticle(finalTitle, finalLink));
      }
      List<Future<Article>> results = executor.invokeAll(tasks);
      for (Future<Article> future : results) {
        try {
          Article article = future.get();
          if (article != null) {
            articles.add(article);
          }
        } catch (Exception e) {
          log.error("Ошибка при получении данных статьи", e);
        }
      }
    } catch (Exception e) {
      log.error("Ошибка во время парсинга страницы: {}", BLOG_LINK, e);
    } finally {
      executor.shutdown();
    }
    return articles;
  }

  public String enrichTitle(String link) {
    String title = "";
    if (link.contains("articles")) {
      title = link
          .replace("https://www.infoq.com/articles/", "")
          .replace("-", " ");
    } else if (link.contains("news")) {
      title = link
          .replace("https://www.infoq.com/news/", "")
          .replace("-", " ");
    }
    title = Character.toUpperCase(title.charAt(0)) + title.substring(1);
    return title;
  }

  public Article parseArticle(String title, String link) {
    try {
      Document articleDocument = Jsoup.connect(link)
          .timeout(TIMEOUT)
          .userAgent(USER_AGENT)
          .get();
      Element dateElement = articleDocument.selectFirst("p[class=article__readTime date]");
      String date = dateElement != null ? dateElement.text() : "Неизвестная дата";
      Elements articleContent = articleDocument.select("p");
      StringBuilder textBuilder = new StringBuilder();
      for (Element content : articleContent) {
        if (!content.text().trim().isEmpty()) {
          textBuilder.append(content.text().trim());
        }
      }
      String text = textBuilder.toString();
      return new Article(title, text, date, link, null);
    } catch (Exception e) {
      log.error("Ошибка во время парсинга страницы: {}", link, e);
      return null;
    }
  }
}