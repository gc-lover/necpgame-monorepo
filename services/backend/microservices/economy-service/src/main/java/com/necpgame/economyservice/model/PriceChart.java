package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.PriceChartDataPointsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PriceChart
 */


public class PriceChart {

  private @Nullable String itemId;

  private @Nullable String chartType;

  private @Nullable String timeframe;

  @Valid
  private List<PriceChartDataPointsInner> dataPoints = new ArrayList<>();

  public PriceChart itemId(@Nullable String itemId) {
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

  public PriceChart chartType(@Nullable String chartType) {
    this.chartType = chartType;
    return this;
  }

  /**
   * Get chartType
   * @return chartType
   */
  
  @Schema(name = "chart_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chart_type")
  public @Nullable String getChartType() {
    return chartType;
  }

  public void setChartType(@Nullable String chartType) {
    this.chartType = chartType;
  }

  public PriceChart timeframe(@Nullable String timeframe) {
    this.timeframe = timeframe;
    return this;
  }

  /**
   * Get timeframe
   * @return timeframe
   */
  
  @Schema(name = "timeframe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeframe")
  public @Nullable String getTimeframe() {
    return timeframe;
  }

  public void setTimeframe(@Nullable String timeframe) {
    this.timeframe = timeframe;
  }

  public PriceChart dataPoints(List<PriceChartDataPointsInner> dataPoints) {
    this.dataPoints = dataPoints;
    return this;
  }

  public PriceChart addDataPointsItem(PriceChartDataPointsInner dataPointsItem) {
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
  public List<PriceChartDataPointsInner> getDataPoints() {
    return dataPoints;
  }

  public void setDataPoints(List<PriceChartDataPointsInner> dataPoints) {
    this.dataPoints = dataPoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceChart priceChart = (PriceChart) o;
    return Objects.equals(this.itemId, priceChart.itemId) &&
        Objects.equals(this.chartType, priceChart.chartType) &&
        Objects.equals(this.timeframe, priceChart.timeframe) &&
        Objects.equals(this.dataPoints, priceChart.dataPoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, chartType, timeframe, dataPoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceChart {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    chartType: ").append(toIndentedString(chartType)).append("\n");
    sb.append("    timeframe: ").append(toIndentedString(timeframe)).append("\n");
    sb.append("    dataPoints: ").append(toIndentedString(dataPoints)).append("\n");
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

