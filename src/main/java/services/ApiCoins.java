package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.ExchangeRate;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ApiCoins {
  //private static String apiKey = loadKey();
  private static ObjectMapper objectMapper = new ObjectMapper();

  private static String loadKey() {
    Properties properties = new Properties();

    try (FileInputStream file = new FileInputStream("config.properties")) {
      properties.load(file);
      return properties.getProperty("exchange.api.key");
    } catch (IOException e) {
      return null;
    }
  }

  private static String sendRequest(String apiKey, String url) {
    if (apiKey == null || apiKey.isEmpty()) {
      return null;
    }

    try {
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url))
              .GET()
              .build();

      HttpResponse<String> httpResponse = httpClient
              .send(httpRequest, HttpResponse.BodyHandlers.ofString());

      if (httpResponse.statusCode() == 200) {
        return httpResponse.body();
      }

      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public static ExchangeRate requestConversion(String apiKey, String baseCurrency, String targetCurrency, Double amount) {

    String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + baseCurrency + "/" + targetCurrency + "/" + amount;

    String response = sendRequest(apiKey, url);
    try {
      return objectMapper.readValue(response, ExchangeRate.class);
    } catch (Exception e) {
      return null;
    }
  }

  public static List<String> getSupportedCodes(String apiKey) {
    String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/codes";

    String response = sendRequest(apiKey, url);

    List<String> suportedCodes = new ArrayList<>();
    JsonNode codesNode = null;

    if (!(response == null)) {
      try {
        JsonNode rootNode = objectMapper.readTree(response);
        codesNode = rootNode.path("supported_codes");
      } catch (JsonProcessingException e) {
        return Collections.emptyList();
      }

      if (codesNode.isArray()) {
        for (JsonNode node : codesNode) {
          suportedCodes.add(node.get(0).asText());
        }
      }
      return suportedCodes;
    }
    return Collections.emptyList();
  }
}
