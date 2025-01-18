package org.example.parser;

import org.example.parser.sites.HiTechParser;
import org.example.parser.sites.InfoqParser;
import org.example.parser.sites.ThreeDNewsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogParserManager {
  private final Map<String, SiteParse> parsers = new HashMap<>();

  public BlogParserManager() {
    parsers.put("infoq", new InfoqParser()); // Идентификатор для сайта Infoq
    parsers.put("3dnews", new ThreeDNewsParser()); // Добавить сюда другие парсеры
    parsers.put("hitech", new HiTechParser());
  }

  public List<Article> parse(String siteId) {
    SiteParse parser = parsers.get(siteId);

    if (parser != null) {
      return parser.parseLastArticles();
    } else {
      throw new IllegalArgumentException("Парсер для указанного сайта не найден: " + siteId);
    }
  }
}
