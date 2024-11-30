package org.example.ml;

public enum Topic {
  DEVOPS(1, "DevOps"),
  IT(2, "IT"),
  FRONTEND(3, "Frontend"),
  BACKEND(4, "Backend"),
  DATA_SCIENCE(5, "Data Science"),
  DATABASE_ADMINISTRATION(6, "Database Administration"),
  CYBERSECURITY(7, "Cybersecurity"),
  CLOUD_COMPUTING(8, "Cloud Computing"),
  MOBILE_DEVELOPMENT(9, "Mobile Development"),
  GAME_DEVELOPMENT(10, "Game Development"),
  MACHINE_LEARNING(11, "Machine learning");

  private final int id;
  private final String label;

  Topic(int id, String label) {
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
