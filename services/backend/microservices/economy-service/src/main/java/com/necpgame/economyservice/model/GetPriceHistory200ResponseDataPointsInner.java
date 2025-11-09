package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * GetPriceHistory200ResponseDataPointsInner
 */

@JsonTypeName("getPriceHistory_200_response_data_points_inner")

public class GetPriceHistory200ResponseDataPointsInner {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable BigDecimal averagePrice;

  private @Nullable BigDecimal minPrice;

  private @Nullable BigDecimal maxPrice;

  private @Nullable Integer volume;

  private @Nullable Integer tradesCount;

  public GetPriceHistory200ResponseDataPointsInner timestamp(@Nullable OffsetDateTime timestamp) {
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

  public GetPriceHistory200ResponseDataPointsInner averagePrice(@Nullable BigDecimal averagePrice) {
    this.averagePrice = averagePrice;
    return this;
  }

  /**
   * Get averagePrice
   * @return averagePrice
   */
  @Valid 
  @Schema(name = "average_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_price")
  public @Nullable BigDecimal getAveragePrice() {
    return averagePrice;
  }

  public void setAveragePrice(@Nullable BigDecimal averagePrice) {
    this.averagePrice = averagePrice;
  }

  public GetPriceHistory200ResponseDataPointsInner minPrice(@Nullable BigDecimal minPrice) {
    this.minPrice = minPrice;
    return this;
  }

  /**
   * Get minPrice
   * @return minPrice
   */
  @Valid 
  @Schema(name = "min_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_price")
  public @Nullable BigDecimal getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(@Nullable BigDecimal minPrice) {
    this.minPrice = minPrice;
  }

  public GetPriceHistory200ResponseDataPointsInner maxPrice(@Nullable BigDecimal maxPrice) {
    this.maxPrice = maxPrice;
    return this;
  }

  /**
   * Get maxPrice
   * @return maxPrice
   */
  @Valid 
  @Schema(name = "max_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_price")
  public @Nullable BigDecimal getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(@Nullable BigDecimal maxPrice) {
    this.maxPrice = maxPrice;
  }

  public GetPriceHistory200ResponseDataPointsInner volume(@Nullable Integer volume) {
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

  public GetPriceHistory200ResponseDataPointsInner tradesCount(@Nullable Integer tradesCount) {
    this.tradesCount = tradesCount;
    return this;
  }

  /**
   * Get tradesCount
   * @return tradesCount
   */
  
  @Schema(name = "trades_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trades_count")
  public @Nullable Integer getTradesCount() {
    return tradesCount;
  }

  public void setTradesCount(@Nullable Integer tradesCount) {
    this.tradesCount = tradesCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPriceHistory200ResponseDataPointsInner getPriceHistory200ResponseDataPointsInner = (GetPriceHistory200ResponseDataPointsInner) o;
    return Objects.equals(this.timestamp, getPriceHistory200ResponseDataPointsInner.timestamp) &&
        Objects.equals(this.averagePrice, getPriceHistory200ResponseDataPointsInner.averagePrice) &&
        Objects.equals(this.minPrice, getPriceHistory200ResponseDataPointsInner.minPrice) &&
        Objects.equals(this.maxPrice, getPriceHistory200ResponseDataPointsInner.maxPrice) &&
        Objects.equals(this.volume, getPriceHistory200ResponseDataPointsInner.volume) &&
        Objects.equals(this.tradesCount, getPriceHistory200ResponseDataPointsInner.tradesCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, averagePrice, minPrice, maxPrice, volume, tradesCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPriceHistory200ResponseDataPointsInner {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    averagePrice: ").append(toIndentedString(averagePrice)).append("\n");
    sb.append("    minPrice: ").append(toIndentedString(minPrice)).append("\n");
    sb.append("    maxPrice: ").append(toIndentedString(maxPrice)).append("\n");
    sb.append("    volume: ").append(toIndentedString(volume)).append("\n");
    sb.append("    tradesCount: ").append(toIndentedString(tradesCount)).append("\n");
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

