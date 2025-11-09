package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.PortfolioAnalysisPerformanceMetricsBestPerformer;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PortfolioAnalysisPerformanceMetrics
 */

@JsonTypeName("PortfolioAnalysis_performance_metrics")

public class PortfolioAnalysisPerformanceMetrics {

  private @Nullable PortfolioAnalysisPerformanceMetricsBestPerformer bestPerformer;

  private @Nullable PortfolioAnalysisPerformanceMetricsBestPerformer worstPerformer;

  public PortfolioAnalysisPerformanceMetrics bestPerformer(@Nullable PortfolioAnalysisPerformanceMetricsBestPerformer bestPerformer) {
    this.bestPerformer = bestPerformer;
    return this;
  }

  /**
   * Get bestPerformer
   * @return bestPerformer
   */
  @Valid 
  @Schema(name = "best_performer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("best_performer")
  public @Nullable PortfolioAnalysisPerformanceMetricsBestPerformer getBestPerformer() {
    return bestPerformer;
  }

  public void setBestPerformer(@Nullable PortfolioAnalysisPerformanceMetricsBestPerformer bestPerformer) {
    this.bestPerformer = bestPerformer;
  }

  public PortfolioAnalysisPerformanceMetrics worstPerformer(@Nullable PortfolioAnalysisPerformanceMetricsBestPerformer worstPerformer) {
    this.worstPerformer = worstPerformer;
    return this;
  }

  /**
   * Get worstPerformer
   * @return worstPerformer
   */
  @Valid 
  @Schema(name = "worst_performer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worst_performer")
  public @Nullable PortfolioAnalysisPerformanceMetricsBestPerformer getWorstPerformer() {
    return worstPerformer;
  }

  public void setWorstPerformer(@Nullable PortfolioAnalysisPerformanceMetricsBestPerformer worstPerformer) {
    this.worstPerformer = worstPerformer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortfolioAnalysisPerformanceMetrics portfolioAnalysisPerformanceMetrics = (PortfolioAnalysisPerformanceMetrics) o;
    return Objects.equals(this.bestPerformer, portfolioAnalysisPerformanceMetrics.bestPerformer) &&
        Objects.equals(this.worstPerformer, portfolioAnalysisPerformanceMetrics.worstPerformer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bestPerformer, worstPerformer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortfolioAnalysisPerformanceMetrics {\n");
    sb.append("    bestPerformer: ").append(toIndentedString(bestPerformer)).append("\n");
    sb.append("    worstPerformer: ").append(toIndentedString(worstPerformer)).append("\n");
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

