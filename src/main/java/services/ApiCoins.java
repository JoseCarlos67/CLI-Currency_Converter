package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class ApiCoins {
  private String apiKey;

  public ApiCoins() {
    this.apiKey = loadKey();
  }

  private String loadKey() {
    Properties properties = new Properties();

    try (FileInputStream file = new FileInputStream("config.properties")) {
      properties.load(file);
      return properties.getProperty("exchange.api.key");
    } catch (IOException e) {
      System.out.println("Error: The config.properties file could not be read!");
      return null;
    }
  }

  public double requestQuote(String baseCurrency, String targetCurrency) {
    if (this.apiKey == null || this.apiKey.isEmpty()) {
      System.out.println("API Key not found!");
      return 0.0;
    }

    String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;

    try {
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest httpRequest = HttpRequest.newBuilder()
              .uri(URI.create(url))
              .GET()
              .build();

      HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

      if (httpResponse.statusCode() == 200) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(httpResponse.body());
        JsonNode ratesNode = rootNode.path("conversion_rates");

        return ratesNode.path(targetCurrency).asDouble();
      }

      System.out.println("Error: " + httpResponse.statusCode());
      return 0.0;

    } catch (Exception e) {
      System.out.println("Error: Connection error!");
      return 0.0;
    }
  }
}
