package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.EconomicMultipliersTradeRestrictionsInner;
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
 * EconomicMultipliers
 */


public class EconomicMultipliers {

  @Valid
  private Map<String, BigDecimal> priceMultipliers = new HashMap<>();

  @Valid
  private List<@Valid EconomicMultipliersTradeRestrictionsInner> tradeRestrictions = new ArrayList<>();

  @Valid
  private Map<String, BigDecimal> currencyExchangeRates = new HashMap<>();

  public EconomicMultipliers priceMultipliers(Map<String, BigDecimal> priceMultipliers) {
    this.priceMultipliers = priceMultipliers;
    return this;
  }

  public EconomicMultipliers putPriceMultipliersItem(String key, BigDecimal priceMultipliersItem) {
    if (this.priceMultipliers == null) {
      this.priceMultipliers = new HashMap<>();
    }
    this.priceMultipliers.put(key, priceMultipliersItem);
    return this;
  }

  /**
   * Get priceMultipliers
   * @return priceMultipliers
   */
  @Valid 
  @Schema(name = "price_multipliers", example = "{\"weapons\":1.5,\"cyberware\":2.0,\"food\":0.8}", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_multipliers")
  public Map<String, BigDecimal> getPriceMultipliers() {
    return priceMultipliers;
  }

  public void setPriceMultipliers(Map<String, BigDecimal> priceMultipliers) {
    this.priceMultipliers = priceMultipliers;
  }

  public EconomicMultipliers tradeRestrictions(List<@Valid EconomicMultipliersTradeRestrictionsInner> tradeRestrictions) {
    this.tradeRestrictions = tradeRestrictions;
    return this;
  }

  public EconomicMultipliers addTradeRestrictionsItem(EconomicMultipliersTradeRestrictionsInner tradeRestrictionsItem) {
    if (this.tradeRestrictions == null) {
      this.tradeRestrictions = new ArrayList<>();
    }
    this.tradeRestrictions.add(tradeRestrictionsItem);
    return this;
  }

  /**
   * Get tradeRestrictions
   * @return tradeRestrictions
   */
  @Valid 
  @Schema(name = "trade_restrictions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trade_restrictions")
  public List<@Valid EconomicMultipliersTradeRestrictionsInner> getTradeRestrictions() {
    return tradeRestrictions;
  }

  public void setTradeRestrictions(List<@Valid EconomicMultipliersTradeRestrictionsInner> tradeRestrictions) {
    this.tradeRestrictions = tradeRestrictions;
  }

  public EconomicMultipliers currencyExchangeRates(Map<String, BigDecimal> currencyExchangeRates) {
    this.currencyExchangeRates = currencyExchangeRates;
    return this;
  }

  public EconomicMultipliers putCurrencyExchangeRatesItem(String key, BigDecimal currencyExchangeRatesItem) {
    if (this.currencyExchangeRates == null) {
      this.currencyExchangeRates = new HashMap<>();
    }
    this.currencyExchangeRates.put(key, currencyExchangeRatesItem);
    return this;
  }

  /**
   * Get currencyExchangeRates
   * @return currencyExchangeRates
   */
  @Valid 
  @Schema(name = "currency_exchange_rates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency_exchange_rates")
  public Map<String, BigDecimal> getCurrencyExchangeRates() {
    return currencyExchangeRates;
  }

  public void setCurrencyExchangeRates(Map<String, BigDecimal> currencyExchangeRates) {
    this.currencyExchangeRates = currencyExchangeRates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomicMultipliers economicMultipliers = (EconomicMultipliers) o;
    return Objects.equals(this.priceMultipliers, economicMultipliers.priceMultipliers) &&
        Objects.equals(this.tradeRestrictions, economicMultipliers.tradeRestrictions) &&
        Objects.equals(this.currencyExchangeRates, economicMultipliers.currencyExchangeRates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(priceMultipliers, tradeRestrictions, currencyExchangeRates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomicMultipliers {\n");
    sb.append("    priceMultipliers: ").append(toIndentedString(priceMultipliers)).append("\n");
    sb.append("    tradeRestrictions: ").append(toIndentedString(tradeRestrictions)).append("\n");
    sb.append("    currencyExchangeRates: ").append(toIndentedString(currencyExchangeRates)).append("\n");
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

