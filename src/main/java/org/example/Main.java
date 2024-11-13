package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static final int MAX_COUNT = 5; // Avoid magic numbers

  public static void main(String[] args) {
    logger.info("Hello and welcome!"); // Logger instead of System.out

    for (int i = 1; i <= MAX_COUNT; i++) {
      logger.info("i = {}", i); // Logger for loop output
    }
  }
}
