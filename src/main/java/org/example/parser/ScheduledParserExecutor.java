package org.example.parser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public record ScheduledParserExecutor(BlogParserManager blogParserManager) {
  private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public void scheduleParsing(int siteId) {
    scheduler.scheduleAtFixedRate(() -> {
      blogParserManager.parse(siteId);
      // TODO: сохранение статей в БД
    }, 0, 5, TimeUnit.MINUTES);
  }
}
