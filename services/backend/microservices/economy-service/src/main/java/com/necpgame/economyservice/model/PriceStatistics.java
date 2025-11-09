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
 * PriceStatistics
 */


public class PriceStatistics {

  private @Nullable String itemId;

  private @Nullable String period;

  private @Nullable BigDecimal averagePrice;

  private @Nullable BigDecimal medianPrice;

  private @Nullable BigDecimal minPrice;

  private @Nullable BigDecimal maxPrice;

  private @Nullable BigDecimal priceChangePercent;

  /**
   * Gets or Sets trend
   */
  public enum TrendEnum {
    INCREASING("increasing"),
    
    DECREASING("decreasing"),
    
    STABLE("stable");

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

  private @Nullable Integer totalVolume;

  private @Nullable Integer totalTrades;

  public PriceStatistics itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public PriceStatistics period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public PriceStatistics averagePrice(@Nullable BigDecimal averagePrice) {
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

  public PriceStatistics medianPrice(@Nullable BigDecimal medianPrice) {
    this.medianPrice = medianPrice;
    return this;
  }

  /**
   * Get medianPrice
   * @return medianPrice
   */
  @Valid 
  @Schema(name = "median_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("median_price")
  public @Nullable BigDecimal getMedianPrice() {
    return medianPrice;
  }

  public void setMedianPrice(@Nullable BigDecimal medianPrice) {
    this.medianPrice = medianPrice;
  }

  public PriceStatistics minPrice(@Nullable BigDecimal minPrice) {
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

  public PriceStatistics maxPrice(@Nullable BigDecimal maxPrice) {
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

  public PriceStatistics priceChangePercent(@Nullable BigDecimal priceChangePercent) {
    this.priceChangePercent = priceChangePercent;
    return this;
  }

  /**
   * Изменение цены за период (%)
   * @return priceChangePercent
   */
  @Valid 
  @Schema(name = "price_change_percent", description = "Изменение цены за период (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_change_percent")
  public @Nullable BigDecimal getPriceChangePercent() {
    return priceChangePercent;
  }

  public void setPriceChangePercent(@Nullable BigDecimal priceChangePercent) {
    this.priceChangePercent = priceChangePercent;
  }

  public PriceStatistics trend(@Nullable TrendEnum trend) {
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

  public PriceStatistics totalVolume(@Nullable Integer totalVolume) {
    this.totalVolume = totalVolume;
    return this;
  }

  /**
   * Общий объем торгов
   * @return totalVolume
   */
  
  @Schema(name = "total_volume", description = "Общий объем торгов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_volume")
  public @Nullable Integer getTotalVolume() {
    return totalVolume;
  }

  public void setTotalVolume(@Nullable Integer totalVolume) {
    this.totalVolume = totalVolume;
  }

  public PriceStatistics totalTrades(@Nullable Integer totalTrades) {
    this.totalTrades = totalTrades;
    return this;
  }

  /**
   * Количество сделок
   * @return totalTrades
   */
  
  @Schema(name = "total_trades", description = "Количество сделок", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_trades")
  public @Nullable Integer getTotalTrades() {
    return totalTrades;
  }

  public void setTotalTrades(@Nullable Integer totalTrades) {
    this.totalTrades = totalTrades;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceStatistics priceStatistics = (PriceStatistics) o;
    return Objects.equals(this.itemId, priceStatistics.itemId) &&
        Objects.equals(this.period, priceStatistics.period) &&
        Objects.equals(this.averagePrice, priceStatistics.averagePrice) &&
        Objects.equals(this.medianPrice, priceStatistics.medianPrice) &&
        Objects.equals(this.minPrice, priceStatistics.minPrice) &&
        Objects.equals(this.maxPrice, priceStatistics.maxPrice) &&
        Objects.equals(this.priceChangePercent, priceStatistics.priceChangePercent) &&
        Objects.equals(this.trend, priceStatistics.trend) &&
        Objects.equals(this.totalVolume, priceStatistics.totalVolume) &&
        Objects.equals(this.totalTrades, priceStatistics.totalTrades);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, period, averagePrice, medianPrice, minPrice, maxPrice, priceChangePercent, trend, totalVolume, totalTrades);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceStatistics {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    averagePrice: ").append(toIndentedString(averagePrice)).append("\n");
    sb.append("    medianPrice: ").append(toIndentedString(medianPrice)).append("\n");
    sb.append("    minPrice: ").append(toIndentedString(minPrice)).append("\n");
    sb.append("    maxPrice: ").append(toIndentedString(maxPrice)).append("\n");
    sb.append("    priceChangePercent: ").append(toIndentedString(priceChangePercent)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
    sb.append("    totalVolume: ").append(toIndentedString(totalVolume)).append("\n");
    sb.append("    totalTrades: ").append(toIndentedString(totalTrades)).append("\n");
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

