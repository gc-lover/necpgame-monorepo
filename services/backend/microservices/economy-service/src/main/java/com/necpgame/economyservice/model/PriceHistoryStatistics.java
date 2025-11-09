package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PriceHistoryStatistics
 */

@JsonTypeName("PriceHistory_statistics")

public class PriceHistoryStatistics {

  private @Nullable Integer minPrice;

  private @Nullable Integer maxPrice;

  private @Nullable Integer averagePrice;

  private @Nullable Integer currentPrice;

  /**
   * Gets or Sets trend
   */
  public enum TrendEnum {
    RISING("RISING"),
    
    FALLING("FALLING"),
    
    STABLE("STABLE");

    private final String value;

    TrendEnum(String value) {
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
    public static TrendEnum fromValue(String value) {
      for (TrendEnum b : TrendEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TrendEnum trend;

  public PriceHistoryStatistics minPrice(@Nullable Integer minPrice) {
    this.minPrice = minPrice;
    return this;
  }

  /**
   * Get minPrice
   * @return minPrice
   */
  
  @Schema(name = "min_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_price")
  public @Nullable Integer getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(@Nullable Integer minPrice) {
    this.minPrice = minPrice;
  }

  public PriceHistoryStatistics maxPrice(@Nullable Integer maxPrice) {
    this.maxPrice = maxPrice;
    return this;
  }

  /**
   * Get maxPrice
   * @return maxPrice
   */
  
  @Schema(name = "max_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_price")
  public @Nullable Integer getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(@Nullable Integer maxPrice) {
    this.maxPrice = maxPrice;
  }

  public PriceHistoryStatistics averagePrice(@Nullable Integer averagePrice) {
    this.averagePrice = averagePrice;
    return this;
  }

  /**
   * Get averagePrice
   * @return averagePrice
   */
  
  @Schema(name = "average_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_price")
  public @Nullable Integer getAveragePrice() {
    return averagePrice;
  }

  public void setAveragePrice(@Nullable Integer averagePrice) {
    this.averagePrice = averagePrice;
  }

  public PriceHistoryStatistics currentPrice(@Nullable Integer currentPrice) {
    this.currentPrice = currentPrice;
    return this;
  }

  /**
   * Get currentPrice
   * @return currentPrice
   */
  
  @Schema(name = "current_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_price")
  public @Nullable Integer getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(@Nullable Integer currentPrice) {
    this.currentPrice = currentPrice;
  }

  public PriceHistoryStatistics trend(@Nullable TrendEnum trend) {
    this.trend = trend;
    return this;
  }

  /**
   * Get trend
   * @return trend
   */
  
  @Schema(name = "trend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trend")
  public @Nullable TrendEnum getTrend() {
    return trend;
  }

  public void setTrend(@Nullable TrendEnum trend) {
    this.trend = trend;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceHistoryStatistics priceHistoryStatistics = (PriceHistoryStatistics) o;
    return Objects.equals(this.minPrice, priceHistoryStatistics.minPrice) &&
        Objects.equals(this.maxPrice, priceHistoryStatistics.maxPrice) &&
        Objects.equals(this.averagePrice, priceHistoryStatistics.averagePrice) &&
        Objects.equals(this.currentPrice, priceHistoryStatistics.currentPrice) &&
        Objects.equals(this.trend, priceHistoryStatistics.trend);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minPrice, maxPrice, averagePrice, currentPrice, trend);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceHistoryStatistics {\n");
    sb.append("    minPrice: ").append(toIndentedString(minPrice)).append("\n");
    sb.append("    maxPrice: ").append(toIndentedString(maxPrice)).append("\n");
    sb.append("    averagePrice: ").append(toIndentedString(averagePrice)).append("\n");
    sb.append("    currentPrice: ").append(toIndentedString(currentPrice)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
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

