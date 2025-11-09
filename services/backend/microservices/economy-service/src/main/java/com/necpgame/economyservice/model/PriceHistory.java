package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.PriceHistoryDataPointsInner;
import com.necpgame.economyservice.model.PriceHistoryStatistics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PriceHistory
 */


public class PriceHistory {

  private @Nullable UUID itemId;

  private @Nullable String period;

  private @Nullable String region;

  @Valid
  private List<@Valid PriceHistoryDataPointsInner> dataPoints = new ArrayList<>();

  private @Nullable PriceHistoryStatistics statistics;

  public PriceHistory itemId(@Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @Valid 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable UUID getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable UUID itemId) {
    this.itemId = itemId;
  }

  public PriceHistory period(@Nullable String period) {
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

  public PriceHistory region(@Nullable String region) {
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

  public PriceHistory dataPoints(List<@Valid PriceHistoryDataPointsInner> dataPoints) {
    this.dataPoints = dataPoints;
    return this;
  }

  public PriceHistory addDataPointsItem(PriceHistoryDataPointsInner dataPointsItem) {
    if (this.dataPoints == null) {
      this.dataPoints = new ArrayList<>();
    }
    this.dataPoints.add(dataPointsItem);
    return this;
  }

  /**
   * Get dataPoints
   * @return dataPoints
   */
  @Valid 
  @Schema(name = "data_points", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data_points")
  public List<@Valid PriceHistoryDataPointsInner> getDataPoints() {
    return dataPoints;
  }

  public void setDataPoints(List<@Valid PriceHistoryDataPointsInner> dataPoints) {
    this.dataPoints = dataPoints;
  }

  public PriceHistory statistics(@Nullable PriceHistoryStatistics statistics) {
    this.statistics = statistics;
    return this;
  }

  /**
   * Get statistics
   * @return statistics
   */
  @Valid 
  @Schema(name = "statistics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statistics")
  public @Nullable PriceHistoryStatistics getStatistics() {
    return statistics;
  }

  public void setStatistics(@Nullable PriceHistoryStatistics statistics) {
    this.statistics = statistics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceHistory priceHistory = (PriceHistory) o;
    return Objects.equals(this.itemId, priceHistory.itemId) &&
        Objects.equals(this.period, priceHistory.period) &&
        Objects.equals(this.region, priceHistory.region) &&
        Objects.equals(this.dataPoints, priceHistory.dataPoints) &&
        Objects.equals(this.statistics, priceHistory.statistics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, period, region, dataPoints, statistics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceHistory {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    dataPoints: ").append(toIndentedString(dataPoints)).append("\n");
    sb.append("    statistics: ").append(toIndentedString(statistics)).append("\n");
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

