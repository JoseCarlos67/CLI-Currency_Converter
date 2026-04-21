package application;

import services.ApiCoins;

public class Program {
  public static void main(String[] args) {
    ApiCoins apiCoins = new ApiCoins();
    double value = apiCoins.requestQuote("USD", "BRL");
    System.out.println("Cotação atual " + value);
  }
}
