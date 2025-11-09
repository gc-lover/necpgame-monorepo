package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CandlestickDataPoint
 */


public class CandlestickDataPoint implements PriceChartDataPointsInner {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable BigDecimal open;

  private @Nullable BigDecimal high;

  private @Nullable BigDecimal low;

  private @Nullable BigDecimal close;

  private @Nullable Integer volume;

  public CandlestickDataPoint timestamp(@Nullable OffsetDateTime timestamp) {
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

  public CandlestickDataPoint open(@Nullable BigDecimal open) {
    this.open = open;
    return this;
  }

  /**
   * Get open
   * @return open
   */
  @Valid 
  @Schema(name = "open", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("open")
  public @Nullable BigDecimal getOpen() {
    return open;
  }

  public void setOpen(@Nullable BigDecimal open) {
    this.open = open;
  }

  public CandlestickDataPoint high(@Nullable BigDecimal high) {
    this.high = high;
    return this;
  }

  /**
   * Get high
   * @return high
   */
  @Valid 
  @Schema(name = "high", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("high")
  public @Nullable BigDecimal getHigh() {
    return high;
  }

  public void setHigh(@Nullable BigDecimal high) {
    this.high = high;
  }

  public CandlestickDataPoint low(@Nullable BigDecimal low) {
    this.low = low;
    return this;
  }

  /**
   * Get low
   * @return low
   */
  @Valid 
  @Schema(name = "low", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("low")
  public @Nullable BigDecimal getLow() {
    return low;
  }

  public void setLow(@Nullable BigDecimal low) {
    this.low = low;
  }

  public CandlestickDataPoint close(@Nullable BigDecimal close) {
    this.close = close;
    return this;
  }

  /**
   * Get close
   * @return close
   */
  @Valid 
  @Schema(name = "close", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("close")
  public @Nullable BigDecimal getClose() {
    return close;
  }

  public void setClose(@Nullable BigDecimal close) {
    this.close = close;
  }

  public CandlestickDataPoint volume(@Nullable Integer volume) {
    this.volume = volume;
    return this;
  }

  /**
   * Get volume
   * @return volume
   */
  
  @Schema(name = "volume", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volume")
  public @Nullable Integer getVolume() {
    return volume;
  }

  public void setVolume(@Nullable Integer volume) {
    this.volume = volume;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CandlestickDataPoint candlestickDataPoint = (CandlestickDataPoint) o;
    return Objects.equals(this.timestamp, candlestickDataPoint.timestamp) &&
        Objects.equals(this.open, candlestickDataPoint.open) &&
        Objects.equals(this.high, candlestickDataPoint.high) &&
        Objects.equals(this.low, candlestickDataPoint.low) &&
        Objects.equals(this.close, candlestickDataPoint.close) &&
        Objects.equals(this.volume, candlestickDataPoint.volume);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, open, high, low, close, volume);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CandlestickDataPoint {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    open: ").append(toIndentedString(open)).append("\n");
    sb.append("    high: ").append(toIndentedString(high)).append("\n");
    sb.append("    low: ").append(toIndentedString(low)).append("\n");
    sb.append("    close: ").append(toIndentedString(close)).append("\n");
    sb.append("    volume: ").append(toIndentedString(volume)).append("\n");
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

