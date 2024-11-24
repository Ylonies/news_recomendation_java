package org.example.dto;

import java.util.List;

public class User {
  private final String name;
  private final String password;
  private final List<String> interests;
  private final List<String> newsSources;
  private final List<String> lastArticles;

  public User(String name, String password, List<String> interests, List<String> newsSourses, List<String> lastArticles) {
    this.name = name;
    this.password = password;
    this.interests = interests;
    this.newsSources = newsSourses;
    this.lastArticles = lastArticles;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public List<String> getInterests() {
    return interests;
  }

  public List<String> getLastArticles() {
    return lastArticles;
  }

  public List<String> getNewsSources() {
    return newsSources;
  }
}
