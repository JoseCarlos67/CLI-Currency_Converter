package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.ExchangeRate;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class ApiCoins {



  private static String loadKey() {
    Properties properties = new Properties();

    try (FileInputStream file = new FileInputStream("config.properties")) {
      properties.load(file);
      return properties.getProperty("exchange.api.key");
    } catch (IOException e) {
      System.out.println("Error: The config.properties file could not be read!");
      return null;
    }
  }

  public static ExchangeRate requestConversion(String baseCurrency, String targetCurrency, Double amount) {

    String apiKey = loadKey();

    if (apiKey == null || apiKey.isEmpty()) {
      System.out.println("API Key not found!");
      return null;
    }

    String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + baseCurrency + "/" + targetCurrency + "/" + amount;

    try {
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest httpRequest = HttpRequest.newBuilder()
              .uri(URI.create(url))
              .GET()
              .build();

      HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

      if (httpResponse.statusCode() == 200) {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(httpResponse.body(), ExchangeRate.class);
      }

      System.out.println("Error: " + httpResponse.statusCode());
      return null;

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return null;
    }
  }
}
