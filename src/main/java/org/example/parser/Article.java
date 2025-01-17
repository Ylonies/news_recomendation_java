package org.example.parser;

public class Article {
  private final String name;
  private final String description;
  private final String date;
  private final String link;

  public Article(String name, String description, String date, String link) {
    this.name = name;
    this.description = description;
    this.date = date;
    this.link = link;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getDate() {
    return date;
  }

  public String getLink() {
    return link;
  }
}