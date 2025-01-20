package org.example.parser;

import org.example.entity.Article;

import java.util.List;

public interface SiteParse {
  List<Article> parseLastArticles();
}