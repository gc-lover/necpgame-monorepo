package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExchangeRates
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ExchangeRates {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private String baseCurrency = "NCRD";

  @Valid
  private Map<String, Float> rates = new HashMap<>();

  public ExchangeRates timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ExchangeRates baseCurrency(String baseCurrency) {
    this.baseCurrency = baseCurrency;
    return this;
  }

  /**
   * Get baseCurrency
   * @return baseCurrency
   */
  
  @Schema(name = "base_currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_currency")
  public String getBaseCurrency() {
    return baseCurrency;
  }

  public void setBaseCurrency(String baseCurrency) {
    this.baseCurrency = baseCurrency;
  }

  public ExchangeRates rates(Map<String, Float> rates) {
    this.rates = rates;
    return this;
  }

  public ExchangeRates putRatesItem(String key, Float ratesItem) {
    if (this.rates == null) {
      this.rates = new HashMap<>();
    }
    this.rates.put(key, ratesItem);
    return this;
  }

  /**
   * Get rates
   * @return rates
   */
  
  @Schema(name = "rates", example = "{\"EURO\":0.85,\"YUAN\":6.5,\"EBUCK\":1.2}", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rates")
  public Map<String, Float> getRates() {
    return rates;
  }

  public void setRates(Map<String, Float> rates) {
    this.rates = rates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExchangeRates exchangeRates = (ExchangeRates) o;
    return Objects.equals(this.timestamp, exchangeRates.timestamp) &&
        Objects.equals(this.baseCurrency, exchangeRates.baseCurrency) &&
        Objects.equals(this.rates, exchangeRates.rates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, baseCurrency, rates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExchangeRates {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    baseCurrency: ").append(toIndentedString(baseCurrency)).append("\n");
    sb.append("    rates: ").append(toIndentedString(rates)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

