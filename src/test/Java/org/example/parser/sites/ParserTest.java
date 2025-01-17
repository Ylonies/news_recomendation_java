package org.example.parser.sites;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;

public abstract class ParserTest {
  protected ClassLoader classLoader;

  protected Document getPage(String path) {
    InputStream page = classLoader.getResourceAsStream(path);

    try {
      return Jsoup.parse(page, "UTF-8", "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
