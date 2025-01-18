package org.example.ml;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class TextClassifierTest {

  @Test
  @Disabled("LFS limited")
  void predictTopicsForText() throws Exception {
    final String TEXT = "Для S.T.A.L.K.E.R. 2: Heart of Chornobyl вышел первый крупный патч 1.0.1 с исправлениями 650 багов";

    List<Topic> defaultTopics = List.of(
        new Topic(1, "DevOps"),
        new Topic(2, "IT"),
        new Topic(3, "Frontend"),
        new Topic(4, "Backend"),
        new Topic(5, "Data Science"),
        new Topic(6, "Database Administration"),
        new Topic(7, "Cybersecurity"),
        new Topic(8, "Cloud Computing"),
        new Topic(9, "Mobile Development")
    );
    List<Topic> additionalTopics = List.of(
        new Topic(10, "Game Development"),
        new Topic(11, "Machine learning")
    );


    try (TextClassifier textClassifier = new TextClassifier(
        "org/example/ml/nli-roberta-base.onnx",
        "org/example/ml/model_data/tokenizer.json",
        defaultTopics
    )) {

      Map<Topic, Float> predicts = textClassifier.predictTopicsForText(TEXT, additionalTopics);
      assertAll(
          () -> assertEquals(11, predicts.size(), "Assert predicts size"),
          () -> predicts.values().stream()
              .forEach(predict -> assertTrue(predict >= 0, "Predicted value should be non-negative."))
      );
    }
  }
}