package org.example.parser.sites;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.parser.Article;
import org.example.parser.SiteParse;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreeDNewsParser implements SiteParse, AutoCloseable {
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
  private static final String BLOG_LINK = "https://3dnews.ru";
  private static final Logger log = LoggerFactory.getLogger(ThreeDNewsParser.class);
  private static final int THREAD_COUNT = 25;
  private static final int TIMEOUT = 20000;
  private static final int THREADS_TIMEOUT = 60000;

  private int limitPageCount;
  private ExecutorService executor;

  public ThreeDNewsParser() {
    this(10);
  }

  public ThreeDNewsParser(int limitPageCount) {
    this.limitPageCount = limitPageCount;

    executor = Executors.newFixedThreadPool(THREAD_COUNT);
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
          log.error("Article parsing error1");
        }
      }
    } catch (InterruptedException e) {
      log.error("Website {} parsing error!", BLOG_LINK, e);
    }

    return articles;
  }

  private List<String> getArticleLinks() {
    Document page = getPage(BLOG_LINK);
    if (page == null) {
      return List.of();
    }
    return getArticleLinks();
  }

  public List<String> getArticleLinks(Document page) {
    List<Element> articleBlocks = page.select("div.content-block-data.white");
    List<String> links = new ArrayList<>();

    for (Element articleBlock : articleBlocks) {
      links.add(articleBlock.selectFirst("a").attr("href"));

      if (links.size() == limitPageCount) {
        break;
      }
    }
    return links;
  }

  private Article getArticle(String link) {
    Document page = getPage(BLOG_LINK + link);
    if (page == null) {
      return null;
    }
    return getArticle(link, page);
  }

  public Article getArticle(String link, Document page) {
    Element titleElement = page.selectFirst("title");
    Element descriptionElement = page.selectFirst("div.js-mediator-article p");
    Element dateElement = page.selectFirst("span.entry-date.tttes");

    String name = titleElement.text();
    String description = descriptionElement.text();
    String dateString = dateElement.text().split(",")[0];

    return new Article(name, description, dateString, link);
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
