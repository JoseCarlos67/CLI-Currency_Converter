package services;

import infra.LruCache;
import models.ExchangeRate;

public class CurrencyConversionService {
  private static LruCache<String, Double> lruCache = new LruCache<>(4);

  public static double conversion(String baseCurrency, String targetCurrency, Double value) {
    ExchangeRate exchangeRate = ApiCoins.requestConversion(baseCurrency, targetCurrency, value);

    if (lruCache.containsKey(baseCurrency + "-" + targetCurrency)) {
      return value * lruCache.get(baseCurrency + "-" + targetCurrency);
    }

    lruCache.put(exchangeRate.base_code() + "-" + exchangeRate.target_code(), exchangeRate.conversion_rate());
    return exchangeRate.conversion_result();

  }
}
