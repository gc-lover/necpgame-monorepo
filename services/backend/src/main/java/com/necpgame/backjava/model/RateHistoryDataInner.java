package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * RateHistoryDataInner
 */

@JsonTypeName("RateHistory_data_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RateHistoryDataInner {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable Float open;

  private @Nullable Float high;

  private @Nullable Float low;

  private @Nullable Float close;

  private @Nullable Integer volume;

  public RateHistoryDataInner timestamp(@Nullable OffsetDateTime timestamp) {
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

  public RateHistoryDataInner open(@Nullable Float open) {
    this.open = open;
    return this;
  }

  /**
   * Get open
   * @return open
   */
  
  @Schema(name = "open", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("open")
  public @Nullable Float getOpen() {
    return open;
  }

  public void setOpen(@Nullable Float open) {
    this.open = open;
  }

  public RateHistoryDataInner high(@Nullable Float high) {
    this.high = high;
    return this;
  }

  /**
   * Get high
   * @return high
   */
  
  @Schema(name = "high", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("high")
  public @Nullable Float getHigh() {
    return high;
  }

  public void setHigh(@Nullable Float high) {
    this.high = high;
  }

  public RateHistoryDataInner low(@Nullable Float low) {
    this.low = low;
    return this;
  }

  /**
   * Get low
   * @return low
   */
  
  @Schema(name = "low", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("low")
  public @Nullable Float getLow() {
    return low;
  }

  public void setLow(@Nullable Float low) {
    this.low = low;
  }

  public RateHistoryDataInner close(@Nullable Float close) {
    this.close = close;
    return this;
  }

  /**
   * Get close
   * @return close
   */
  
  @Schema(name = "close", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("close")
  public @Nullable Float getClose() {
    return close;
  }

  public void setClose(@Nullable Float close) {
    this.close = close;
  }

  public RateHistoryDataInner volume(@Nullable Integer volume) {
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
    RateHistoryDataInner rateHistoryDataInner = (RateHistoryDataInner) o;
    return Objects.equals(this.timestamp, rateHistoryDataInner.timestamp) &&
        Objects.equals(this.open, rateHistoryDataInner.open) &&
        Objects.equals(this.high, rateHistoryDataInner.high) &&
        Objects.equals(this.low, rateHistoryDataInner.low) &&
        Objects.equals(this.close, rateHistoryDataInner.close) &&
        Objects.equals(this.volume, rateHistoryDataInner.volume);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, open, high, low, close, volume);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RateHistoryDataInner {\n");
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

