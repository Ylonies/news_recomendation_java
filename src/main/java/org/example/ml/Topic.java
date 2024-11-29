package org.example.ml;

public enum Topic {
  DEVOPS("DevOps"),
  IT("IT"),
  FRONTEND("Frontend"),
  BACKEND("Backend"),
  DATA_SCIENCE("Data Science"),
  DATABASE_ADMINISTRATION("Database Administration"),
  CYBERSECURITY("Cybersecurity"),
  CLOUD_COMPUTING("Cloud Computing"),
  MOBILE_DEVELOPMENT("Mobile Development"),
  GAME_DEVELOPMENT("Game Development"),
  MACHINE_LEARNING("Machine learning");

  private final int id;
  private final String label;

  private static class Counter
  {
    private static int nextValue = 0;
  }

  Topic(String label) {
    this.label = label;
    Counter.nextValue += 1;
    id = Counter.nextValue;
  }

  public int getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }
}
