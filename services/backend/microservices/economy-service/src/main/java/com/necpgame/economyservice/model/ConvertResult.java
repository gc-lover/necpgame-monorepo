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
 * ConvertResult
 */


public class ConvertResult {

  private @Nullable String fromCurrency;

  private @Nullable String toCurrency;

  private @Nullable Float amountFrom;

  private @Nullable Float amountTo;

  private @Nullable Float exchangeRate;

  private @Nullable Float commission;

  private @Nullable Float totalCost;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public ConvertResult fromCurrency(@Nullable String fromCurrency) {
    this.fromCurrency = fromCurrency;
    return this;
  }

  /**
   * Get fromCurrency
   * @return fromCurrency
   */
  
  @Schema(name = "from_currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from_currency")
  public @Nullable String getFromCurrency() {
    return fromCurrency;
  }

  public void setFromCurrency(@Nullable String fromCurrency) {
    this.fromCurrency = fromCurrency;
  }

  public ConvertResult toCurrency(@Nullable String toCurrency) {
    this.toCurrency = toCurrency;
    return this;
  }

  /**
   * Get toCurrency
   * @return toCurrency
   */
  
  @Schema(name = "to_currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("to_currency")
  public @Nullable String getToCurrency() {
    return toCurrency;
  }

  public void setToCurrency(@Nullable String toCurrency) {
    this.toCurrency = toCurrency;
  }

  public ConvertResult amountFrom(@Nullable Float amountFrom) {
    this.amountFrom = amountFrom;
    return this;
  }

  /**
   * Get amountFrom
   * @return amountFrom
   */
  
  @Schema(name = "amount_from", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount_from")
  public @Nullable Float getAmountFrom() {
    return amountFrom;
  }

  public void setAmountFrom(@Nullable Float amountFrom) {
    this.amountFrom = amountFrom;
  }

  public ConvertResult amountTo(@Nullable Float amountTo) {
    this.amountTo = amountTo;
    return this;
  }

  /**
   * Get amountTo
   * @return amountTo
   */
  
  @Schema(name = "amount_to", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount_to")
  public @Nullable Float getAmountTo() {
    return amountTo;
  }

  public void setAmountTo(@Nullable Float amountTo) {
    this.amountTo = amountTo;
  }

  public ConvertResult exchangeRate(@Nullable Float exchangeRate) {
    this.exchangeRate = exchangeRate;
    return this;
  }

  /**
   * Get exchangeRate
   * @return exchangeRate
   */
  
  @Schema(name = "exchange_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exchange_rate")
  public @Nullable Float getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(@Nullable Float exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

  public ConvertResult commission(@Nullable Float commission) {
    this.commission = commission;
    return this;
  }

  /**
   * Get commission
   * @return commission
   */
  
  @Schema(name = "commission", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commission")
  public @Nullable Float getCommission() {
    return commission;
  }

  public void setCommission(@Nullable Float commission) {
    this.commission = commission;
  }

  public ConvertResult totalCost(@Nullable Float totalCost) {
    this.totalCost = totalCost;
    return this;
  }

  /**
   * Get totalCost
   * @return totalCost
   */
  
  @Schema(name = "total_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_cost")
  public @Nullable Float getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(@Nullable Float totalCost) {
    this.totalCost = totalCost;
  }

  public ConvertResult timestamp(@Nullable OffsetDateTime timestamp) {
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
    ConvertResult convertResult = (ConvertResult) o;
    return Objects.equals(this.fromCurrency, convertResult.fromCurrency) &&
        Objects.equals(this.toCurrency, convertResult.toCurrency) &&
        Objects.equals(this.amountFrom, convertResult.amountFrom) &&
        Objects.equals(this.amountTo, convertResult.amountTo) &&
        Objects.equals(this.exchangeRate, convertResult.exchangeRate) &&
        Objects.equals(this.commission, convertResult.commission) &&
        Objects.equals(this.totalCost, convertResult.totalCost) &&
        Objects.equals(this.timestamp, convertResult.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromCurrency, toCurrency, amountFrom, amountTo, exchangeRate, commission, totalCost, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConvertResult {\n");
    sb.append("    fromCurrency: ").append(toIndentedString(fromCurrency)).append("\n");
    sb.append("    toCurrency: ").append(toIndentedString(toCurrency)).append("\n");
    sb.append("    amountFrom: ").append(toIndentedString(amountFrom)).append("\n");
    sb.append("    amountTo: ").append(toIndentedString(amountTo)).append("\n");
    sb.append("    exchangeRate: ").append(toIndentedString(exchangeRate)).append("\n");
    sb.append("    commission: ").append(toIndentedString(commission)).append("\n");
    sb.append("    totalCost: ").append(toIndentedString(totalCost)).append("\n");
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

