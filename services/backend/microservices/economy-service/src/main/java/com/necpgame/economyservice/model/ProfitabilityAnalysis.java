package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * ProfitabilityAnalysis
 */


public class ProfitabilityAnalysis {

  private @Nullable String chainId;

  private @Nullable Integer totalInputCost;

  private @Nullable Integer totalProductionCost;

  private @Nullable Integer estimatedSellingPrice;

  private @Nullable Integer estimatedProfit;

  private @Nullable Float profitMargin;

  private @Nullable Float roi;

  private @Nullable BigDecimal timeToCompleteHours;

  private @Nullable BigDecimal profitPerHour;

  @Valid
  private List<String> bottlenecks = new ArrayList<>();

  public ProfitabilityAnalysis chainId(@Nullable String chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  
  @Schema(name = "chain_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chain_id")
  public @Nullable String getChainId() {
    return chainId;
  }

  public void setChainId(@Nullable String chainId) {
    this.chainId = chainId;
  }

  public ProfitabilityAnalysis totalInputCost(@Nullable Integer totalInputCost) {
    this.totalInputCost = totalInputCost;
    return this;
  }

  /**
   * Get totalInputCost
   * @return totalInputCost
   */
  
  @Schema(name = "total_input_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_input_cost")
  public @Nullable Integer getTotalInputCost() {
    return totalInputCost;
  }

  public void setTotalInputCost(@Nullable Integer totalInputCost) {
    this.totalInputCost = totalInputCost;
  }

  public ProfitabilityAnalysis totalProductionCost(@Nullable Integer totalProductionCost) {
    this.totalProductionCost = totalProductionCost;
    return this;
  }

  /**
   * Get totalProductionCost
   * @return totalProductionCost
   */
  
  @Schema(name = "total_production_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_production_cost")
  public @Nullable Integer getTotalProductionCost() {
    return totalProductionCost;
  }

  public void setTotalProductionCost(@Nullable Integer totalProductionCost) {
    this.totalProductionCost = totalProductionCost;
  }

  public ProfitabilityAnalysis estimatedSellingPrice(@Nullable Integer estimatedSellingPrice) {
    this.estimatedSellingPrice = estimatedSellingPrice;
    return this;
  }

  /**
   * Get estimatedSellingPrice
   * @return estimatedSellingPrice
   */
  
  @Schema(name = "estimated_selling_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_selling_price")
  public @Nullable Integer getEstimatedSellingPrice() {
    return estimatedSellingPrice;
  }

  public void setEstimatedSellingPrice(@Nullable Integer estimatedSellingPrice) {
    this.estimatedSellingPrice = estimatedSellingPrice;
  }

  public ProfitabilityAnalysis estimatedProfit(@Nullable Integer estimatedProfit) {
    this.estimatedProfit = estimatedProfit;
    return this;
  }

  /**
   * Get estimatedProfit
   * @return estimatedProfit
   */
  
  @Schema(name = "estimated_profit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_profit")
  public @Nullable Integer getEstimatedProfit() {
    return estimatedProfit;
  }

  public void setEstimatedProfit(@Nullable Integer estimatedProfit) {
    this.estimatedProfit = estimatedProfit;
  }

  public ProfitabilityAnalysis profitMargin(@Nullable Float profitMargin) {
    this.profitMargin = profitMargin;
    return this;
  }

  /**
   * Маржа в %
   * @return profitMargin
   */
  
  @Schema(name = "profit_margin", description = "Маржа в %", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_margin")
  public @Nullable Float getProfitMargin() {
    return profitMargin;
  }

  public void setProfitMargin(@Nullable Float profitMargin) {
    this.profitMargin = profitMargin;
  }

  public ProfitabilityAnalysis roi(@Nullable Float roi) {
    this.roi = roi;
    return this;
  }

  /**
   * Get roi
   * @return roi
   */
  
  @Schema(name = "roi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roi")
  public @Nullable Float getRoi() {
    return roi;
  }

  public void setRoi(@Nullable Float roi) {
    this.roi = roi;
  }

  public ProfitabilityAnalysis timeToCompleteHours(@Nullable BigDecimal timeToCompleteHours) {
    this.timeToCompleteHours = timeToCompleteHours;
    return this;
  }

  /**
   * Get timeToCompleteHours
   * @return timeToCompleteHours
   */
  @Valid 
  @Schema(name = "time_to_complete_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_to_complete_hours")
  public @Nullable BigDecimal getTimeToCompleteHours() {
    return timeToCompleteHours;
  }

  public void setTimeToCompleteHours(@Nullable BigDecimal timeToCompleteHours) {
    this.timeToCompleteHours = timeToCompleteHours;
  }

  public ProfitabilityAnalysis profitPerHour(@Nullable BigDecimal profitPerHour) {
    this.profitPerHour = profitPerHour;
    return this;
  }

  /**
   * Get profitPerHour
   * @return profitPerHour
   */
  @Valid 
  @Schema(name = "profit_per_hour", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_per_hour")
  public @Nullable BigDecimal getProfitPerHour() {
    return profitPerHour;
  }

  public void setProfitPerHour(@Nullable BigDecimal profitPerHour) {
    this.profitPerHour = profitPerHour;
  }

  public ProfitabilityAnalysis bottlenecks(List<String> bottlenecks) {
    this.bottlenecks = bottlenecks;
    return this;
  }

  public ProfitabilityAnalysis addBottlenecksItem(String bottlenecksItem) {
    if (this.bottlenecks == null) {
      this.bottlenecks = new ArrayList<>();
    }
    this.bottlenecks.add(bottlenecksItem);
    return this;
  }

  /**
   * Узкие места в цепочке
   * @return bottlenecks
   */
  
  @Schema(name = "bottlenecks", description = "Узкие места в цепочке", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bottlenecks")
  public List<String> getBottlenecks() {
    return bottlenecks;
  }

  public void setBottlenecks(List<String> bottlenecks) {
    this.bottlenecks = bottlenecks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProfitabilityAnalysis profitabilityAnalysis = (ProfitabilityAnalysis) o;
    return Objects.equals(this.chainId, profitabilityAnalysis.chainId) &&
        Objects.equals(this.totalInputCost, profitabilityAnalysis.totalInputCost) &&
        Objects.equals(this.totalProductionCost, profitabilityAnalysis.totalProductionCost) &&
        Objects.equals(this.estimatedSellingPrice, profitabilityAnalysis.estimatedSellingPrice) &&
        Objects.equals(this.estimatedProfit, profitabilityAnalysis.estimatedProfit) &&
        Objects.equals(this.profitMargin, profitabilityAnalysis.profitMargin) &&
        Objects.equals(this.roi, profitabilityAnalysis.roi) &&
        Objects.equals(this.timeToCompleteHours, profitabilityAnalysis.timeToCompleteHours) &&
        Objects.equals(this.profitPerHour, profitabilityAnalysis.profitPerHour) &&
        Objects.equals(this.bottlenecks, profitabilityAnalysis.bottlenecks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, totalInputCost, totalProductionCost, estimatedSellingPrice, estimatedProfit, profitMargin, roi, timeToCompleteHours, profitPerHour, bottlenecks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProfitabilityAnalysis {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    totalInputCost: ").append(toIndentedString(totalInputCost)).append("\n");
    sb.append("    totalProductionCost: ").append(toIndentedString(totalProductionCost)).append("\n");
    sb.append("    estimatedSellingPrice: ").append(toIndentedString(estimatedSellingPrice)).append("\n");
    sb.append("    estimatedProfit: ").append(toIndentedString(estimatedProfit)).append("\n");
    sb.append("    profitMargin: ").append(toIndentedString(profitMargin)).append("\n");
    sb.append("    roi: ").append(toIndentedString(roi)).append("\n");
    sb.append("    timeToCompleteHours: ").append(toIndentedString(timeToCompleteHours)).append("\n");
    sb.append("    profitPerHour: ").append(toIndentedString(profitPerHour)).append("\n");
    sb.append("    bottlenecks: ").append(toIndentedString(bottlenecks)).append("\n");
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

