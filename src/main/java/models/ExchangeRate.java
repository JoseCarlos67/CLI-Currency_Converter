package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeRate(String base_code, String target_code, Double conversion_rate, Double conversion_result) {
}
