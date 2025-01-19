package org.example.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;

public class ParserDownloader {
  private ClassLoader classLoader;

  protected void beforeEach() {
    classLoader = Thread.currentThread().getContextClassLoader();
  }

  protected Document getPage(String path) {
    InputStream page = classLoader.getResourceAsStream(path);

    try {
      if (path.contains(".xml")) {
        return Jsoup.parse(page, "UTF-8", "", Parser.xmlParser());
      }

      return Jsoup.parse(page, "UTF-8", "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
