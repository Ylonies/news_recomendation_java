package org.example.parser.sites;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.parser.Article;
import org.example.parser.SiteParse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class InfoqParser implements SiteParse, AutoCloseable {
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
  private static final String DOMAIN = "https://www.infoq.com";
  private static final String BLOG_LINK = "https://www.infoq.com/development";
  private static final String SITE_TITLE = "Infoq";
  private static final Logger log = LoggerFactory.getLogger(InfoqParser.class);
  private static final int THREAD_COUNT = 25;
  private static final int TIMEOUT = 20000;
  private static final int THREADS_TIMEOUT = 60000;

  private ExecutorService executor;

  public InfoqParser() {
    executor = Executors.newFixedThreadPool(THREAD_COUNT);
  }

  @Override
  public String getArticleTag() {
    return SITE_TITLE;
  }

  @Override
  public List<Article> parseLastArticles() {
    List<String> articleLinks = getArticleLinks();
    List<Callable<Article>> tasks = new ArrayList<>();

    for (String articleLink : articleLinks) {
      tasks.add(() -> getArticle(articleLink));
    }

    List<Article> articles = new ArrayList<>();

    try {
      List<Future<Article>> results = executor.invokeAll(tasks);

      for (Future<Article> result : results) {
        try {
          Article article = result.get();
          if (article != null) {
            articles.add(article);
          }
        } catch (ExecutionException e) {
          log.error("Article parsing error", e);
        }
      }
    } catch (InterruptedException e) {
      log.error("Website {} parsing error!", BLOG_LINK, e);
      Thread.currentThread().interrupt();
    }

    return articles;
  }

  private List<String> getArticleLinks() {
    Document page = getPage(BLOG_LINK);
    if (page == null) {
      return List.of();
    }
    return getArticleLinks(page);
  }

  public List<String> getArticleLinks(Document page) {
    List<Element> titleElements = page.select("h4.card__title a");
    List<String> links = new ArrayList<>();

    for (Element titleElement : titleElements) {
      String link = titleElement.attr("href");
      if (link.contains("news") || link.contains("articles")) {
        links.add(link);
      }
    }

    return links;
  }

  private Article getArticle(String link) {
    Document page = getPage(DOMAIN + link);
    if (page == null) {
      return null;
    }
    return getArticle(link, page);
  }

  public Article getArticle(String link, Document page) {
    Element titleElement = page.selectFirst("h1");
    Element dateElement = page.selectFirst("p.article__readTime.date");
    List<Element> contentElements = page.select("p");

    String title = titleElement != null ? titleElement.text() : enrichTitle(link);
    String date = dateElement != null ? dateElement.text() : "Unknown date";

    StringBuilder textBuilder = new StringBuilder();
    for (Element content : contentElements) {
      String text = content.text().trim();
      if (!text.isEmpty()) {
        textBuilder.append(text).append(" ");
      }
    }

    return new Article(title, textBuilder.toString().trim(), date, DOMAIN + link);
  }

  private String enrichTitle(String link) {
    String title = link;
    if (link.contains("articles")) {
      title = link.replace("https://www.infoq.com/articles/", "").replace("-", " ");
    } else if (link.contains("news")) {
      title = link.replace("https://www.infoq.com/news/", "").replace("-", " ");
    }
    return Character.toUpperCase(title.charAt(0)) + title.substring(1);
  }

  private Document getPage(String link) {
    try {
      return Jsoup.connect(link)
          .timeout(TIMEOUT)
          .userAgent(USER_AGENT)
          .get();
    } catch (Exception e) {
      log.error("Get request error: {}", link, e);
      return null;
    }
  }

  @Override
  public void close() {
    executor.shutdown();

    try {
      if (!executor.awaitTermination(THREADS_TIMEOUT, TimeUnit.MILLISECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
