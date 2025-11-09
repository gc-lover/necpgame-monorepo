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
 * MarketIndexSnapshot
 */


public class MarketIndexSnapshot {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private Float value;

  private @Nullable Float volatility;

  private @Nullable String source;

  public MarketIndexSnapshot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MarketIndexSnapshot(OffsetDateTime timestamp, Float value) {
    this.timestamp = timestamp;
    this.value = value;
  }

  public MarketIndexSnapshot timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public MarketIndexSnapshot value(Float value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @NotNull 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  public MarketIndexSnapshot volatility(@Nullable Float volatility) {
    this.volatility = volatility;
    return this;
  }

  /**
   * Get volatility
   * @return volatility
   */
  
  @Schema(name = "volatility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volatility")
  public @Nullable Float getVolatility() {
    return volatility;
  }

  public void setVolatility(@Nullable Float volatility) {
    this.volatility = volatility;
  }

  public MarketIndexSnapshot source(@Nullable String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @Size(min = 2, max = 64) 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable String getSource() {
    return source;
  }

  public void setSource(@Nullable String source) {
    this.source = source;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketIndexSnapshot marketIndexSnapshot = (MarketIndexSnapshot) o;
    return Objects.equals(this.timestamp, marketIndexSnapshot.timestamp) &&
        Objects.equals(this.value, marketIndexSnapshot.value) &&
        Objects.equals(this.volatility, marketIndexSnapshot.volatility) &&
        Objects.equals(this.source, marketIndexSnapshot.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, value, volatility, source);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketIndexSnapshot {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    volatility: ").append(toIndentedString(volatility)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

