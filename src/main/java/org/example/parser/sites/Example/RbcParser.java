//package org.example.parser.sites.Example;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.example.parser.Article;
//import org.example.parser.SiteParse;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class RbcParser implements SiteParse {
//  private static final int TIMEOUT = 10000;
//  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
//  private static final String BLOG_LINK = "https://habr.com/ru/hubs/programming/articles/";
//  private static final Logger log = LoggerFactory.getLogger(RbcParser.class);
//
//  public List<Article> parseLastArticles() {
//    final var article = new ArrayList<Article>();
//    try {
//      Document document = Jsoup.connect(BLOG_LINK)
//          .timeout(TIMEOUT)
//          .userAgent(USER_AGENT)
//          .get();
//      String title = Objects.requireNonNull(document.selectFirst("")).text();
//      Element linkElement = document.selectFirst("a.tm-title__link");
//      assert linkElement != null;
//      String link = linkElement.attr("href");
//      Document articleDocument = Jsoup.connect(link)
//          .timeout(TIMEOUT)
//          .userAgent(USER_AGENT)
//          .get();
//      String date = Objects.requireNonNull(articleDocument.selectFirst("time")).text();
//      String summary = Objects.requireNonNull(articleDocument.selectFirst("meta[name=description]")).attr("content").trim();
//      Elements articleContent = articleDocument.select("li strong, p:not(.showcase-collection-card__text.js-rm-cm-item-text p):not(.showcase-collection__footer p):not(.showcase-collection__subtitle p):not(div.article__special_container p)");
//      String text = "";
//      for (Element content : articleContent) {
//        if (!content.text().trim().isEmpty() && !content.text().trim().contains("Читайте РБК в")) {
//          text = text + content.text().trim();
//        }
//      }
//      Elements mediaLinksElements = articleDocument.select("img");
//      List<String> media = new ArrayList<>();
//      for (Element image : mediaLinksElements) {
//        String img = image.attr("src");
//        if (img.contains(".jpeg")) {
//          media.add(img);
//        }
//      }
//      article.add(new Article(title, summary, date, link, media));
//    } catch(Exception e) {
//      log.error("Error while parsing articles on page url {}", BLOG_LINK, e);
//    }
//    return article;
//  }
//}
