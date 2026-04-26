package services;

import gui.TerminalInterface;
import infra.LruCache;
import models.ExchangeRate;

public class CurrencyConversionService {
  private static LruCache<String, Double> lruCache = new LruCache<>(7);

  public static double conversion(String baseCurrency, String targetCurrency, Double value) {
    ExchangeRate exchangeRate = ApiCoins.requestConversion(baseCurrency, targetCurrency, value);

    lruCache.put(exchangeRate.base_code() + "-" + exchangeRate.target_code(), exchangeRate.conversion_rate());
    return exchangeRate.conversion_result();

  }
}
