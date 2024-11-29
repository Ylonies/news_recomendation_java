package org.example.ml;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class TextClassifier {
  private static final int MAX_LENGTH = 512;

  private final OrtEnvironment environment;
  private final OrtSession session;
  private final HuggingFaceTokenizer tokenizer;

  public TextClassifier(String modelPath, String tokenizerPath) throws OrtException, IOException {
    String localPath = System.getProperty("user.dir");
    modelPath = String.valueOf(Paths.get(localPath, modelPath));

    environment = OrtEnvironment.getEnvironment();
    session = environment.createSession(modelPath, new OrtSession.SessionOptions());
    tokenizer = HuggingFaceTokenizer.newInstance(Paths.get(localPath, tokenizerPath));
  }

  public List<Topic> getTopics(String inputText) throws Exception {
    if (inputText.length() > MAX_LENGTH) {
      inputText = inputText.substring(0, MAX_LENGTH);
    }

    return getSortTopics(getCategoryProbabilities(inputText));
  }

  private float[] getCategoryProbabilities(String inputText) throws Exception {
    List<Float> logits = new ArrayList<>();
    for (Topic topic: Topic.values()) {

      var encode = tokenizer.encode(List.of(inputText, topic.getLabel()));
      long[] inputTokens = encode.getIds();
      long[] attentionMask = encode.getAttentionMask();


      try (OnnxTensor inputTensor = OnnxTensor.createTensor(environment, new long[][]{inputTokens});
           OnnxTensor maskTensor = OnnxTensor.createTensor(environment, new long[][]{attentionMask})) {
        var inputs = Map.of("input_ids", inputTensor, "attention_mask", maskTensor);

        try (var result = session.run(inputs)) {
          float[][] resultLogits = (float[][]) result.get(0).getValue();
          logits.add(resultLogits[0][0]);
        }
      }
    }

    return softmax(logits);
  }

  private static float[] softmax(List<Float> logits) {
    float maxLogit = logits.stream().max(Float::compareTo).orElse(Float.NEGATIVE_INFINITY);
    float sumExp = 0.0f;
    float[] expLogits = new float[logits.size()];
    for (int i = 0; i < logits.size(); i++) {
      expLogits[i] = (float) Math.exp(logits.get(i) - maxLogit);
      sumExp += expLogits[i];
    }
    for (int i = 0; i < expLogits.length; i++) {
      expLogits[i] /= sumExp;
    }
    return expLogits;
  }

  private static List<Topic> getSortTopics(float[] probabilities) {
    List<Map.Entry<Float, Topic>> probabilityToTopic = new ArrayList<>();

    int item = 0;
    for (Topic topic : Topic.values()) {
      probabilityToTopic.add(new AbstractMap.SimpleEntry<>(probabilities[item], topic));
      item++;
    }

    probabilityToTopic.sort((entry1, entry2) -> Float.compare(entry2.getKey(), entry1.getKey()));
    List<Topic> result = new ArrayList<>();
    for (Map.Entry<Float, Topic> topicEntry : probabilityToTopic) {
      result.add(topicEntry.getValue());
    }
    return result;
  }
}
