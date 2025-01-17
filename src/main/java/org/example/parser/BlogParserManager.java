package org.example.parser;

import org.example.parser.sites.InfoqParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogParserManager {
  private final Map<String, SiteParse> parsers = new HashMap<>();

  public BlogParserManager() {
    parsers.put("infoq", new InfoqParser()); // Идентификатор для сайта Infoq
    // Добавить сюда другие парсеры
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
