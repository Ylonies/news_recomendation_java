package org.example.parser.sites;

import org.example.parser.Article;

import java.util.List;

public class InfoqParserTestParseLastArticles {
  public static void main(String[] args){
    InfoqParser parser = new InfoqParser();
    List<Article> articles = parser.parseLastArticles();
    for (Article article : articles) {
      System.out.println("Title: " + article.name());
      System.out.println("Link: " + article.link());
      System.out.println("Date: " + article.date());
      System.out.println("Content: " + article.description());
      System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
    }
  }
}
