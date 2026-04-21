package services;

import infra.LruCache;
import models.ExchangeRate;

public class CurrencyConversionService {
  private static LruCache<String, Double> lruCache = new LruCache<>(7);

  public static void manager() {
    conversion("BRL", "USD", 100.00);
    conversion("AOA", "BRL", 1000.00);
  }

  private static void conversion(String baseCurrency, String targetCurrency, Double value) {
    ExchangeRate exchangeRate = ApiCoins.requestConversion(baseCurrency, targetCurrency, value);

    lruCache.put(exchangeRate.base_code() + "-" + exchangeRate.target_code(), exchangeRate.conversion_rate());

    System.out.println(lruCache);

  }
}
