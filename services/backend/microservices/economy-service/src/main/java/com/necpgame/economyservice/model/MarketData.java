package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.MarketDataPriceTrends;
import com.necpgame.economyservice.model.MarketDataSupplyDemand;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * MarketData
 */


public class MarketData {

  private @Nullable String category;

  private @Nullable String region;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  @Valid
  private Map<String, Integer> averagePrices = new HashMap<>();

  private @Nullable MarketDataPriceTrends priceTrends;

  private @Nullable MarketDataSupplyDemand supplyDemand;

  public MarketData category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public MarketData region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public MarketData timestamp(@Nullable OffsetDateTime timestamp) {
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

  public MarketData averagePrices(Map<String, Integer> averagePrices) {
    this.averagePrices = averagePrices;
    return this;
  }

  public MarketData putAveragePricesItem(String key, Integer averagePricesItem) {
    if (this.averagePrices == null) {
      this.averagePrices = new HashMap<>();
    }
    this.averagePrices.put(key, averagePricesItem);
    return this;
  }

  /**
   * Get averagePrices
   * @return averagePrices
   */
  
  @Schema(name = "average_prices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_prices")
  public Map<String, Integer> getAveragePrices() {
    return averagePrices;
  }

  public void setAveragePrices(Map<String, Integer> averagePrices) {
    this.averagePrices = averagePrices;
  }

  public MarketData priceTrends(@Nullable MarketDataPriceTrends priceTrends) {
    this.priceTrends = priceTrends;
    return this;
  }

  /**
   * Get priceTrends
   * @return priceTrends
   */
  @Valid 
  @Schema(name = "price_trends", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_trends")
  public @Nullable MarketDataPriceTrends getPriceTrends() {
    return priceTrends;
  }

  public void setPriceTrends(@Nullable MarketDataPriceTrends priceTrends) {
    this.priceTrends = priceTrends;
  }

  public MarketData supplyDemand(@Nullable MarketDataSupplyDemand supplyDemand) {
    this.supplyDemand = supplyDemand;
    return this;
  }

  /**
   * Get supplyDemand
   * @return supplyDemand
   */
  @Valid 
  @Schema(name = "supply_demand", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supply_demand")
  public @Nullable MarketDataSupplyDemand getSupplyDemand() {
    return supplyDemand;
  }

  public void setSupplyDemand(@Nullable MarketDataSupplyDemand supplyDemand) {
    this.supplyDemand = supplyDemand;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketData marketData = (MarketData) o;
    return Objects.equals(this.category, marketData.category) &&
        Objects.equals(this.region, marketData.region) &&
        Objects.equals(this.timestamp, marketData.timestamp) &&
        Objects.equals(this.averagePrices, marketData.averagePrices) &&
        Objects.equals(this.priceTrends, marketData.priceTrends) &&
        Objects.equals(this.supplyDemand, marketData.supplyDemand);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, region, timestamp, averagePrices, priceTrends, supplyDemand);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketData {\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    averagePrices: ").append(toIndentedString(averagePrices)).append("\n");
    sb.append("    priceTrends: ").append(toIndentedString(priceTrends)).append("\n");
    sb.append("    supplyDemand: ").append(toIndentedString(supplyDemand)).append("\n");
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

