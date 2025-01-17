package org.example.parser.sites;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.parser.SiteParse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class HiTechParser implements SiteParse, AutoCloseable {
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
  private static final String DOMAIN = "https://hi-tech.mail.ru";
  private static final String BLOG_LINK = "https://hi-tech.mail.ru/news/";
  private static final String SITE_TITLE = "Hi-Tech Mail";
  private static final Logger log = LoggerFactory.getLogger(HiTechParser.class);
  private static final int THREAD_COUNT = 25;
  private static final int TIMEOUT = 20000;
  private static final int THREADS_TIMEOUT = 60000;

  private ExecutorService executor;

  public NewsParser() {
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
    List<Element> titleElements = page.select("h3[data-qa='Title'] a");
    List<String> links = new ArrayList<>();

    for (Element titleElement : titleElements) {
      String link = titleElement.attr("href");
      links.add(link);
    }

    return links;
  }

  private Article getArticle(String link) {
    Document page = getPage(link);
    if (page == null) {
      return null;
    }
    return getArticle(link, page);
  }

  public Article getArticle(String link, Document page) {
    Element titleElement = page.selectFirst("h1");
    Element descriptionElement = page.selectFirst("div[data-qa='Text']");
    Element dateElement = page.selectFirst("time");

    String title = titleElement != null ? titleElement.text() : "Unknown title";
    String description = descriptionElement != null ? descriptionElement.text() : "";
    String date = dateElement != null ? dateElement.attr("datetime") : "Unknown date";

    return new Article(title, description, date, link);
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
