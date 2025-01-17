package org.example.parser;

import java.util.List;

public interface SiteParse {
  String getArticleTag();
  List<Article> parseLastArticles();
}
