package org.example.ml;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TextClassifier implements AutoCloseable {
  private final int maxLength;

  private final OrtEnvironment environment;
  private final OrtSession session;
  private final HuggingFaceTokenizer tokenizer;

  private final List<Topic> defaultTopics;

  public TextClassifier(String modelPath, String tokenizerPath, List<Topic> defaultTopics) throws OrtException, IOException, URISyntaxException {
    this.defaultTopics = defaultTopics;

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL modelURL = classLoader.getResource(modelPath);
    URL tokenizerURL = classLoader.getResource(tokenizerPath);

    if (modelURL == null || tokenizerURL == null) {
      throw new IllegalArgumentException("Model or tokenizer file not found. Check the paths: " +
          "modelPath=" + modelPath + ", tokenizerPath=" + tokenizerPath);
    }

    Path modelFilePath = Paths.get(modelURL.toURI());
    Path tokenizerFilePath = Paths.get(tokenizerURL.toURI());

    environment = OrtEnvironment.getEnvironment();
    session = environment.createSession(modelFilePath.toString(), new OrtSession.SessionOptions());
    tokenizer = HuggingFaceTokenizer.newInstance(tokenizerFilePath);

    maxLength = tokenizer.getMaxLength();
  }


  public Map<Topic, Float> predictTopicsForText(String inputText, List<Topic> additionalTopics) throws Exception {
    List<Topic> topics = new ArrayList<>();
    topics.addAll(defaultTopics);
    topics.addAll(additionalTopics);

    String text = inputText;
    if (text.length() > maxLength) {
      text = text.substring(0, maxLength);
    }

    return createTopicProbabilityMap(topics, getCategoryProbabilities(text, topics));
  }

  private float[] getCategoryProbabilities(String inputText, List<Topic> topics) throws Exception {
    List<Float> logits = new ArrayList<>();
    for (Topic topic : topics) {
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

  private static Map<Topic, Float> createTopicProbabilityMap(List<Topic> topics, float... probabilities) {
    List<Map.Entry<Topic, Float>> topicProbabilityList = new ArrayList<>();
    for (int i = 0; i < topics.size(); i++) {
      topicProbabilityList.add(new AbstractMap.SimpleEntry<>(topics.get(i), probabilities[i]));
    }

    topicProbabilityList.sort((entry1, entry2) -> Float.compare(entry2.getValue(), entry1.getValue()));

    Map<Topic, Float> sortedTopicProbabilities = new LinkedHashMap<>();
    for (Map.Entry<Topic, Float> entry : topicProbabilityList) {
      sortedTopicProbabilities.put(entry.getKey(), entry.getValue());
    }

    return sortedTopicProbabilities;
  }

  @Override
  public void close() throws Exception {
    session.close();
    environment.close();
  }
}
