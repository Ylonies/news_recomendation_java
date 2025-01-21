package org.example.ml;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.example.entity.Catalog;

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

  private final List<Catalog> defaultCatalogs;

  public TextClassifier(List<Catalog> defaultCatalogs) throws OrtException, IOException, URISyntaxException {
    this.defaultCatalogs = defaultCatalogs;

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    String modelPath = "org/example/ml/nli-roberta-base.onnx";
    URL modelURL = classLoader.getResource(modelPath);
    String tokenizerPath = "org/example/ml/model_data/tokenizer.json";
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


  public Map<Catalog, Float> predictTopicsForText(String inputText, List<Catalog> additionalCatalogs) throws Exception {
    List<Catalog> catalogs = new ArrayList<>();
    catalogs.addAll(defaultCatalogs);
    catalogs.addAll(additionalCatalogs);

    String text = inputText;
    if (text.length() > maxLength) {
      text = text.substring(0, maxLength);
    }

    return createTopicProbabilityMap(catalogs, getCategoryProbabilities(text, catalogs));
  }

  private float[] getCategoryProbabilities(String inputText, List<Catalog> catalogs) throws Exception {
    List<Float> logits = new ArrayList<>();
    for (Catalog catalog : catalogs) {
      var encode = tokenizer.encode(List.of(inputText, catalog.getName()));
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

  private static Map<Catalog, Float> createTopicProbabilityMap(List<Catalog> topics, float... probabilities) {
    Map<Catalog, Float> topicProbabilities = new LinkedHashMap<>();

    for (int i = 0; i < probabilities.length; i++) {
      topicProbabilities.put(topics.get(i), probabilities[i]);
    }

    return  topicProbabilities;
  }

  @Override
  public void close() throws Exception {
    session.close();
    environment.close();
  }
}
