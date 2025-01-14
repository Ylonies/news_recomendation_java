package org.example.ml;

public class Topic {
  private final int id;
  private final String label;

  public Topic(int id, String label) {
    this.id = id;
    this.label = label;
  }

  public int getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }
}
