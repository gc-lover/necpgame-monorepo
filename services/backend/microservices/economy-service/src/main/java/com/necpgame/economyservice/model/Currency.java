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
 * Currency
 */


public class Currency {

  private @Nullable String currencyId;

  private @Nullable String name;

  private @Nullable String symbol;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    PRIMARY("primary"),
    
    REGIONAL("regional"),
    
    FACTION("faction"),
    
    CRYPTO("crypto"),
    
    PREMIUM("premium");

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

  private @Nullable BigDecimal exchangeRateToEd;

  private @Nullable Boolean isTradeable;

  public Currency currencyId(@Nullable String currencyId) {
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

  public Currency name(@Nullable String name) {
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

  public Currency symbol(@Nullable String symbol) {
    this.symbol = symbol;
    return this;
  }

  /**
   * Get symbol
   * @return symbol
   */
  
  @Schema(name = "symbol", example = "ED", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("symbol")
  public @Nullable String getSymbol() {
    return symbol;
  }

  public void setSymbol(@Nullable String symbol) {
    this.symbol = symbol;
  }

  public Currency type(@Nullable TypeEnum type) {
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

  public Currency exchangeRateToEd(@Nullable BigDecimal exchangeRateToEd) {
    this.exchangeRateToEd = exchangeRateToEd;
    return this;
  }

  /**
   * Курс к Eurodollar
   * @return exchangeRateToEd
   */
  @Valid 
  @Schema(name = "exchange_rate_to_ed", description = "Курс к Eurodollar", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exchange_rate_to_ed")
  public @Nullable BigDecimal getExchangeRateToEd() {
    return exchangeRateToEd;
  }

  public void setExchangeRateToEd(@Nullable BigDecimal exchangeRateToEd) {
    this.exchangeRateToEd = exchangeRateToEd;
  }

  public Currency isTradeable(@Nullable Boolean isTradeable) {
    this.isTradeable = isTradeable;
    return this;
  }

  /**
   * Get isTradeable
   * @return isTradeable
   */
  
  @Schema(name = "is_tradeable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_tradeable")
  public @Nullable Boolean getIsTradeable() {
    return isTradeable;
  }

  public void setIsTradeable(@Nullable Boolean isTradeable) {
    this.isTradeable = isTradeable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Currency currency = (Currency) o;
    return Objects.equals(this.currencyId, currency.currencyId) &&
        Objects.equals(this.name, currency.name) &&
        Objects.equals(this.symbol, currency.symbol) &&
        Objects.equals(this.type, currency.type) &&
        Objects.equals(this.exchangeRateToEd, currency.exchangeRateToEd) &&
        Objects.equals(this.isTradeable, currency.isTradeable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currencyId, name, symbol, type, exchangeRateToEd, isTradeable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Currency {\n");
    sb.append("    currencyId: ").append(toIndentedString(currencyId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    exchangeRateToEd: ").append(toIndentedString(exchangeRateToEd)).append("\n");
    sb.append("    isTradeable: ").append(toIndentedString(isTradeable)).append("\n");
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

