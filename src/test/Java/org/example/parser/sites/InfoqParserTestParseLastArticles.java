package org.example.parser.sites;

import org.example.parser.Article;

import java.util.List;

public class InfoqParserTestParseLastArticles {
  public static void main(String[] args){
    InfoqParser parser = new InfoqParser();
    List<Article> articles = parser.parseLastArticles();
    for (Article article : articles) {
      System.out.println("Title: " + article.getName());
      System.out.println("Link: " + article.getLink());
      System.out.println("Date: " + article.getDate());
      System.out.println("Content: " + article.getDescription());
      System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
    }
  }
}
