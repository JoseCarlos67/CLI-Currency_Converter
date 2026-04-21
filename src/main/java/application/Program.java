package application;

import models.ExchangeRate;
import services.ApiCoins;

public class Program {
  public static void main(String[] args) {
    ApiCoins apiCoins = new ApiCoins();
    ExchangeRate value = apiCoins.requestConversion("USD", "BRL", 100.00);
    System.out.println("Cotação atual " + value);
  }
}
