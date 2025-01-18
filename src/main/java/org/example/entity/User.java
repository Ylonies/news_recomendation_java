package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
  private final String name;
  private final String password;
  private final List<String> catalogs;
  private final List<String> websites;
  private final List<String> lastArticles;

  public User(String name, String password) {
    this.name = name;
    this.password = password;
    this.catalogs = new ArrayList<>();
    this.websites =new ArrayList<>();
    this.lastArticles = new ArrayList<>();
  }

  public String getName() {
    return name;
  }
  public String getPassword() {
    return password;
  }
  public List<String> getCatalogs() {
    return catalogs;
  }
  public List<String> getLastArticles() {
    return lastArticles;
  }
  public List<String> getWebsites() {
    return websites;
  }
}
