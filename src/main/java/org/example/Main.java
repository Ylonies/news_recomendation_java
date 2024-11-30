package org.example;

import org.example.ml.TextClassifier;
import org.example.ml.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    if (logger.isDebugEnabled()) {
      try {
        TextClassifier textClassifier = new TextClassifier(
            "src/main/resources/org/example/ml/nli-roberta-base.onnx",
            "src/main/resources/org/example/ml/model_data/tokenizer.json"
        );

        for (Topic topic : textClassifier.getTopics("Для S.T.A.L.K.E.R. 2: Heart of Chornobyl вышел первый крупный патч 1.0.1 с исправлениями 650 багов")) { // Topics in descending order of probability
          logger.debug("{} {}", topic.getId(), topic.getLabel());
        }

      } catch (IOException e) {
        logger.error("Ошибка при загрузке модели или токенизатора", e);
      } catch (Exception e) {
        logger.error("Общая ошибка при классификации текста", e);
      }
    }
  }

}
