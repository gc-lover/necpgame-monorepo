package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CurrencyPair
 */


public class CurrencyPair {

  private @Nullable String pair;

  private @Nullable String base;

  private @Nullable String quote;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    MAJOR("MAJOR"),
    
    MINOR("MINOR"),
    
    EXOTIC("EXOTIC");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable BigDecimal minTradeAmount;

  private @Nullable Integer maxLeverage;

  private @Nullable Float commissionRate;

  public CurrencyPair pair(@Nullable String pair) {
    this.pair = pair;
    return this;
  }

  /**
   * Get pair
   * @return pair
   */
  
  @Schema(name = "pair", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pair")
  public @Nullable String getPair() {
    return pair;
  }

  public void setPair(@Nullable String pair) {
    this.pair = pair;
  }

  public CurrencyPair base(@Nullable String base) {
    this.base = base;
    return this;
  }

  /**
   * Get base
   * @return base
   */
  
  @Schema(name = "base", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base")
  public @Nullable String getBase() {
    return base;
  }

  public void setBase(@Nullable String base) {
    this.base = base;
  }

  public CurrencyPair quote(@Nullable String quote) {
    this.quote = quote;
    return this;
  }

  /**
   * Get quote
   * @return quote
   */
  
  @Schema(name = "quote", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quote")
  public @Nullable String getQuote() {
    return quote;
  }

  public void setQuote(@Nullable String quote) {
    this.quote = quote;
  }

  public CurrencyPair type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public CurrencyPair minTradeAmount(@Nullable BigDecimal minTradeAmount) {
    this.minTradeAmount = minTradeAmount;
    return this;
  }

  /**
   * Get minTradeAmount
   * @return minTradeAmount
   */
  @Valid 
  @Schema(name = "min_trade_amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_trade_amount")
  public @Nullable BigDecimal getMinTradeAmount() {
    return minTradeAmount;
  }

  public void setMinTradeAmount(@Nullable BigDecimal minTradeAmount) {
    this.minTradeAmount = minTradeAmount;
  }

  public CurrencyPair maxLeverage(@Nullable Integer maxLeverage) {
    this.maxLeverage = maxLeverage;
    return this;
  }

  /**
   * Get maxLeverage
   * @return maxLeverage
   */
  
  @Schema(name = "max_leverage", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_leverage")
  public @Nullable Integer getMaxLeverage() {
    return maxLeverage;
  }

  public void setMaxLeverage(@Nullable Integer maxLeverage) {
    this.maxLeverage = maxLeverage;
  }

  public CurrencyPair commissionRate(@Nullable Float commissionRate) {
    this.commissionRate = commissionRate;
    return this;
  }

  /**
   * Get commissionRate
   * @return commissionRate
   */
  
  @Schema(name = "commission_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commission_rate")
  public @Nullable Float getCommissionRate() {
    return commissionRate;
  }

  public void setCommissionRate(@Nullable Float commissionRate) {
    this.commissionRate = commissionRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurrencyPair currencyPair = (CurrencyPair) o;
    return Objects.equals(this.pair, currencyPair.pair) &&
        Objects.equals(this.base, currencyPair.base) &&
        Objects.equals(this.quote, currencyPair.quote) &&
        Objects.equals(this.type, currencyPair.type) &&
        Objects.equals(this.minTradeAmount, currencyPair.minTradeAmount) &&
        Objects.equals(this.maxLeverage, currencyPair.maxLeverage) &&
        Objects.equals(this.commissionRate, currencyPair.commissionRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pair, base, quote, type, minTradeAmount, maxLeverage, commissionRate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurrencyPair {\n");
    sb.append("    pair: ").append(toIndentedString(pair)).append("\n");
    sb.append("    base: ").append(toIndentedString(base)).append("\n");
    sb.append("    quote: ").append(toIndentedString(quote)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    minTradeAmount: ").append(toIndentedString(minTradeAmount)).append("\n");
    sb.append("    maxLeverage: ").append(toIndentedString(maxLeverage)).append("\n");
    sb.append("    commissionRate: ").append(toIndentedString(commissionRate)).append("\n");
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

