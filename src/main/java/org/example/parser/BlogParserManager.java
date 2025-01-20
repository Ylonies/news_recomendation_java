package org.example.parser;

import org.example.entity.Article;
import org.example.parser.sites.HiTechParser;
import org.example.parser.sites.InfoqParser;
import org.example.parser.sites.RSSParser;
import org.example.parser.sites.ThreeDNewsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogParserManager {
  private final Map<Integer, SiteParse> parsers = new HashMap<>();

  private int parserId = 3;

  public BlogParserManager() {
    parsers.put(ArticleIds.INFOQ.getId(), new InfoqParser()); // Идентификатор для сайта Infoq
    parsers.put(ArticleIds.THREE_D.getId(), new ThreeDNewsParser()); // Добавить сюда другие парсеры
    parsers.put(ArticleIds.HI_TECH.getId(), new HiTechParser());
  }

  public void addRSSParser(String link) {
    parsers.put(parserId, new RSSParser(link));
    parserId++;
  }

  public List<Article> parse(int siteId) {
    SiteParse parser = parsers.get(siteId);

    if (parser != null) {
      return parser.parseLastArticles();
    } else {
      throw new IllegalArgumentException("Парсер для указанного сайта не найден: " + siteId);
    }
  }
}
