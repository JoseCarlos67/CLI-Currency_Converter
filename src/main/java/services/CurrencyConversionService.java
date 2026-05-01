package services;

import infra.LruCache;
import models.ExchangeRate;

public class CurrencyConversionService {
  private static LruCache<String, Double> lruCache = new LruCache<>(4);

  public static Double conversion(String apiKey, String baseCurrency, String targetCurrency, Double value) {

    if (lruCache.containsKey(baseCurrency + "-" + targetCurrency)) {
      return value * lruCache.get(baseCurrency + "-" + targetCurrency);
    }

    ExchangeRate exchangeRate = ApiCoins.requestConversion(apiKey, baseCurrency, targetCurrency, value);
    if (!(exchangeRate == null)){
      lruCache.put(exchangeRate.base_code() + "-" + exchangeRate.target_code(), exchangeRate.conversion_rate());
      return exchangeRate.conversion_result();
    }
    return null;
  }
}
