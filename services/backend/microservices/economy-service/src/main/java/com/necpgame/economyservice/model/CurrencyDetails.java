package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CurrencyDetails
 */


public class CurrencyDetails {

  private @Nullable String currencyId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String symbol;

  private @Nullable String type;

  private @Nullable String lore;

  private @Nullable BigDecimal exchangeRateToEd;

  private @Nullable BigDecimal exchangeFee;

  private @Nullable BigDecimal dailyExchangeCap;

  @Valid
  private List<String> usageRegions = new ArrayList<>();

  public CurrencyDetails currencyId(@Nullable String currencyId) {
    this.currencyId = currencyId;
    return this;
  }

  /**
   * Get currencyId
   * @return currencyId
   */
  
  @Schema(name = "currency_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency_id")
  public @Nullable String getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(@Nullable String currencyId) {
    this.currencyId = currencyId;
  }

  public CurrencyDetails name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public CurrencyDetails description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public CurrencyDetails symbol(@Nullable String symbol) {
    this.symbol = symbol;
    return this;
  }

  /**
   * Get symbol
   * @return symbol
   */
  
  @Schema(name = "symbol", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("symbol")
  public @Nullable String getSymbol() {
    return symbol;
  }

  public void setSymbol(@Nullable String symbol) {
    this.symbol = symbol;
  }

  public CurrencyDetails type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public CurrencyDetails lore(@Nullable String lore) {
    this.lore = lore;
    return this;
  }

  /**
   * Лор валюты из Cyberpunk
   * @return lore
   */
  
  @Schema(name = "lore", description = "Лор валюты из Cyberpunk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lore")
  public @Nullable String getLore() {
    return lore;
  }

  public void setLore(@Nullable String lore) {
    this.lore = lore;
  }

  public CurrencyDetails exchangeRateToEd(@Nullable BigDecimal exchangeRateToEd) {
    this.exchangeRateToEd = exchangeRateToEd;
    return this;
  }

  /**
   * Get exchangeRateToEd
   * @return exchangeRateToEd
   */
  @Valid 
  @Schema(name = "exchange_rate_to_ed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exchange_rate_to_ed")
  public @Nullable BigDecimal getExchangeRateToEd() {
    return exchangeRateToEd;
  }

  public void setExchangeRateToEd(@Nullable BigDecimal exchangeRateToEd) {
    this.exchangeRateToEd = exchangeRateToEd;
  }

  public CurrencyDetails exchangeFee(@Nullable BigDecimal exchangeFee) {
    this.exchangeFee = exchangeFee;
    return this;
  }

  /**
   * Комиссия при обмене (%)
   * @return exchangeFee
   */
  @Valid 
  @Schema(name = "exchange_fee", description = "Комиссия при обмене (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exchange_fee")
  public @Nullable BigDecimal getExchangeFee() {
    return exchangeFee;
  }

  public void setExchangeFee(@Nullable BigDecimal exchangeFee) {
    this.exchangeFee = exchangeFee;
  }

  public CurrencyDetails dailyExchangeCap(@Nullable BigDecimal dailyExchangeCap) {
    this.dailyExchangeCap = dailyExchangeCap;
    return this;
  }

  /**
   * Дневной лимит обмена (если есть)
   * @return dailyExchangeCap
   */
  @Valid 
  @Schema(name = "daily_exchange_cap", description = "Дневной лимит обмена (если есть)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("daily_exchange_cap")
  public @Nullable BigDecimal getDailyExchangeCap() {
    return dailyExchangeCap;
  }

  public void setDailyExchangeCap(@Nullable BigDecimal dailyExchangeCap) {
    this.dailyExchangeCap = dailyExchangeCap;
  }

  public CurrencyDetails usageRegions(List<String> usageRegions) {
    this.usageRegions = usageRegions;
    return this;
  }

  public CurrencyDetails addUsageRegionsItem(String usageRegionsItem) {
    if (this.usageRegions == null) {
      this.usageRegions = new ArrayList<>();
    }
    this.usageRegions.add(usageRegionsItem);
    return this;
  }

  /**
   * Регионы, где валюта активно используется
   * @return usageRegions
   */
  
  @Schema(name = "usage_regions", description = "Регионы, где валюта активно используется", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("usage_regions")
  public List<String> getUsageRegions() {
    return usageRegions;
  }

  public void setUsageRegions(List<String> usageRegions) {
    this.usageRegions = usageRegions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurrencyDetails currencyDetails = (CurrencyDetails) o;
    return Objects.equals(this.currencyId, currencyDetails.currencyId) &&
        Objects.equals(this.name, currencyDetails.name) &&
        Objects.equals(this.description, currencyDetails.description) &&
        Objects.equals(this.symbol, currencyDetails.symbol) &&
        Objects.equals(this.type, currencyDetails.type) &&
        Objects.equals(this.lore, currencyDetails.lore) &&
        Objects.equals(this.exchangeRateToEd, currencyDetails.exchangeRateToEd) &&
        Objects.equals(this.exchangeFee, currencyDetails.exchangeFee) &&
        Objects.equals(this.dailyExchangeCap, currencyDetails.dailyExchangeCap) &&
        Objects.equals(this.usageRegions, currencyDetails.usageRegions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currencyId, name, description, symbol, type, lore, exchangeRateToEd, exchangeFee, dailyExchangeCap, usageRegions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurrencyDetails {\n");
    sb.append("    currencyId: ").append(toIndentedString(currencyId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    lore: ").append(toIndentedString(lore)).append("\n");
    sb.append("    exchangeRateToEd: ").append(toIndentedString(exchangeRateToEd)).append("\n");
    sb.append("    exchangeFee: ").append(toIndentedString(exchangeFee)).append("\n");
    sb.append("    dailyExchangeCap: ").append(toIndentedString(dailyExchangeCap)).append("\n");
    sb.append("    usageRegions: ").append(toIndentedString(usageRegions)).append("\n");
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

