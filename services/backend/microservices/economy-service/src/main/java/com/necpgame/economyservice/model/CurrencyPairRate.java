package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
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
 * CurrencyPairRate
 */


public class CurrencyPairRate {

  private @Nullable String pair;

  private @Nullable String base;

  private @Nullable String quote;

  private @Nullable Float rate;

  private @Nullable Float bid;

  private @Nullable Float ask;

  private @Nullable Float spread;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public CurrencyPairRate pair(@Nullable String pair) {
    this.pair = pair;
    return this;
  }

  /**
   * Get pair
   * @return pair
   */
  
  @Schema(name = "pair", example = "NCRD/EURO", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pair")
  public @Nullable String getPair() {
    return pair;
  }

  public void setPair(@Nullable String pair) {
    this.pair = pair;
  }

  public CurrencyPairRate base(@Nullable String base) {
    this.base = base;
    return this;
  }

  /**
   * Get base
   * @return base
   */
  
  @Schema(name = "base", example = "NCRD", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base")
  public @Nullable String getBase() {
    return base;
  }

  public void setBase(@Nullable String base) {
    this.base = base;
  }

  public CurrencyPairRate quote(@Nullable String quote) {
    this.quote = quote;
    return this;
  }

  /**
   * Get quote
   * @return quote
   */
  
  @Schema(name = "quote", example = "EURO", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quote")
  public @Nullable String getQuote() {
    return quote;
  }

  public void setQuote(@Nullable String quote) {
    this.quote = quote;
  }

  public CurrencyPairRate rate(@Nullable Float rate) {
    this.rate = rate;
    return this;
  }

  /**
   * Курс покупки
   * @return rate
   */
  
  @Schema(name = "rate", example = "0.85", description = "Курс покупки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rate")
  public @Nullable Float getRate() {
    return rate;
  }

  public void setRate(@Nullable Float rate) {
    this.rate = rate;
  }

  public CurrencyPairRate bid(@Nullable Float bid) {
    this.bid = bid;
    return this;
  }

  /**
   * Курс продажи
   * @return bid
   */
  
  @Schema(name = "bid", description = "Курс продажи", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bid")
  public @Nullable Float getBid() {
    return bid;
  }

  public void setBid(@Nullable Float bid) {
    this.bid = bid;
  }

  public CurrencyPairRate ask(@Nullable Float ask) {
    this.ask = ask;
    return this;
  }

  /**
   * Курс покупки
   * @return ask
   */
  
  @Schema(name = "ask", description = "Курс покупки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ask")
  public @Nullable Float getAsk() {
    return ask;
  }

  public void setAsk(@Nullable Float ask) {
    this.ask = ask;
  }

  public CurrencyPairRate spread(@Nullable Float spread) {
    this.spread = spread;
    return this;
  }

  /**
   * Spread (bid-ask)
   * @return spread
   */
  
  @Schema(name = "spread", description = "Spread (bid-ask)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spread")
  public @Nullable Float getSpread() {
    return spread;
  }

  public void setSpread(@Nullable Float spread) {
    this.spread = spread;
  }

  public CurrencyPairRate timestamp(@Nullable OffsetDateTime timestamp) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurrencyPairRate currencyPairRate = (CurrencyPairRate) o;
    return Objects.equals(this.pair, currencyPairRate.pair) &&
        Objects.equals(this.base, currencyPairRate.base) &&
        Objects.equals(this.quote, currencyPairRate.quote) &&
        Objects.equals(this.rate, currencyPairRate.rate) &&
        Objects.equals(this.bid, currencyPairRate.bid) &&
        Objects.equals(this.ask, currencyPairRate.ask) &&
        Objects.equals(this.spread, currencyPairRate.spread) &&
        Objects.equals(this.timestamp, currencyPairRate.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pair, base, quote, rate, bid, ask, spread, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurrencyPairRate {\n");
    sb.append("    pair: ").append(toIndentedString(pair)).append("\n");
    sb.append("    base: ").append(toIndentedString(base)).append("\n");
    sb.append("    quote: ").append(toIndentedString(quote)).append("\n");
    sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
    sb.append("    bid: ").append(toIndentedString(bid)).append("\n");
    sb.append("    ask: ").append(toIndentedString(ask)).append("\n");
    sb.append("    spread: ").append(toIndentedString(spread)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

