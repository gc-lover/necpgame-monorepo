package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EconomyState
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class EconomyState {

  private @Nullable BigDecimal inflationRate;

  @Valid
  private Map<String, BigDecimal> currencyRates = new HashMap<>();

  /**
   * Gets or Sets marketHealth
   */
  public enum MarketHealthEnum {
    STRONG("STRONG"),
    
    STABLE("STABLE"),
    
    WEAK("WEAK"),
    
    CRISIS("CRISIS");

    private final String value;

    MarketHealthEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static MarketHealthEnum fromValue(String value) {
      for (MarketHealthEnum b : MarketHealthEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MarketHealthEnum marketHealth;

  @Valid
  private List<String> activeEvents = new ArrayList<>();

  public EconomyState inflationRate(@Nullable BigDecimal inflationRate) {
    this.inflationRate = inflationRate;
    return this;
  }

  /**
   * Get inflationRate
   * @return inflationRate
   */
  @Valid 
  @Schema(name = "inflation_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inflation_rate")
  public @Nullable BigDecimal getInflationRate() {
    return inflationRate;
  }

  public void setInflationRate(@Nullable BigDecimal inflationRate) {
    this.inflationRate = inflationRate;
  }

  public EconomyState currencyRates(Map<String, BigDecimal> currencyRates) {
    this.currencyRates = currencyRates;
    return this;
  }

  public EconomyState putCurrencyRatesItem(String key, BigDecimal currencyRatesItem) {
    if (this.currencyRates == null) {
      this.currencyRates = new HashMap<>();
    }
    this.currencyRates.put(key, currencyRatesItem);
    return this;
  }

  /**
   * Get currencyRates
   * @return currencyRates
   */
  @Valid 
  @Schema(name = "currency_rates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency_rates")
  public Map<String, BigDecimal> getCurrencyRates() {
    return currencyRates;
  }

  public void setCurrencyRates(Map<String, BigDecimal> currencyRates) {
    this.currencyRates = currencyRates;
  }

  public EconomyState marketHealth(@Nullable MarketHealthEnum marketHealth) {
    this.marketHealth = marketHealth;
    return this;
  }

  /**
   * Get marketHealth
   * @return marketHealth
   */
  
  @Schema(name = "market_health", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("market_health")
  public @Nullable MarketHealthEnum getMarketHealth() {
    return marketHealth;
  }

  public void setMarketHealth(@Nullable MarketHealthEnum marketHealth) {
    this.marketHealth = marketHealth;
  }

  public EconomyState activeEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
    return this;
  }

  public EconomyState addActiveEventsItem(String activeEventsItem) {
    if (this.activeEvents == null) {
      this.activeEvents = new ArrayList<>();
    }
    this.activeEvents.add(activeEventsItem);
    return this;
  }

  /**
   * Get activeEvents
   * @return activeEvents
   */
  
  @Schema(name = "active_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_events")
  public List<String> getActiveEvents() {
    return activeEvents;
  }

  public void setActiveEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomyState economyState = (EconomyState) o;
    return Objects.equals(this.inflationRate, economyState.inflationRate) &&
        Objects.equals(this.currencyRates, economyState.currencyRates) &&
        Objects.equals(this.marketHealth, economyState.marketHealth) &&
        Objects.equals(this.activeEvents, economyState.activeEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inflationRate, currencyRates, marketHealth, activeEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomyState {\n");
    sb.append("    inflationRate: ").append(toIndentedString(inflationRate)).append("\n");
    sb.append("    currencyRates: ").append(toIndentedString(currencyRates)).append("\n");
    sb.append("    marketHealth: ").append(toIndentedString(marketHealth)).append("\n");
    sb.append("    activeEvents: ").append(toIndentedString(activeEvents)).append("\n");
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

