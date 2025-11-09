package com.necpgame.economyservice.model;

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
 * PriceHistoryDataPointsInner
 */

@JsonTypeName("PriceHistory_data_points_inner")

public class PriceHistoryDataPointsInner {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable Integer price;

  private @Nullable Integer volume;

  public PriceHistoryDataPointsInner timestamp(@Nullable OffsetDateTime timestamp) {
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

  public PriceHistoryDataPointsInner price(@Nullable Integer price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
   */
  
  @Schema(name = "price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public @Nullable Integer getPrice() {
    return price;
  }

  public void setPrice(@Nullable Integer price) {
    this.price = price;
  }

  public PriceHistoryDataPointsInner volume(@Nullable Integer volume) {
    this.volume = volume;
    return this;
  }

  /**
   * Объем торговли
   * @return volume
   */
  
  @Schema(name = "volume", description = "Объем торговли", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    PriceHistoryDataPointsInner priceHistoryDataPointsInner = (PriceHistoryDataPointsInner) o;
    return Objects.equals(this.timestamp, priceHistoryDataPointsInner.timestamp) &&
        Objects.equals(this.price, priceHistoryDataPointsInner.price) &&
        Objects.equals(this.volume, priceHistoryDataPointsInner.volume);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, price, volume);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceHistoryDataPointsInner {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
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

