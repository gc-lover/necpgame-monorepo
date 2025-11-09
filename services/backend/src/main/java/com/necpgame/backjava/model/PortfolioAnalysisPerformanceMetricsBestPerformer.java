package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * PortfolioAnalysisPerformanceMetricsBestPerformer
 */

@JsonTypeName("PortfolioAnalysis_performance_metrics_best_performer")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class PortfolioAnalysisPerformanceMetricsBestPerformer {

  private @Nullable String investmentId;

  private @Nullable BigDecimal roi;

  public PortfolioAnalysisPerformanceMetricsBestPerformer investmentId(@Nullable String investmentId) {
    this.investmentId = investmentId;
    return this;
  }

  /**
   * Get investmentId
   * @return investmentId
   */
  
  @Schema(name = "investment_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("investment_id")
  public @Nullable String getInvestmentId() {
    return investmentId;
  }

  public void setInvestmentId(@Nullable String investmentId) {
    this.investmentId = investmentId;
  }

  public PortfolioAnalysisPerformanceMetricsBestPerformer roi(@Nullable BigDecimal roi) {
    this.roi = roi;
    return this;
  }

  /**
   * Get roi
   * @return roi
   */
  @Valid 
  @Schema(name = "roi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roi")
  public @Nullable BigDecimal getRoi() {
    return roi;
  }

  public void setRoi(@Nullable BigDecimal roi) {
    this.roi = roi;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortfolioAnalysisPerformanceMetricsBestPerformer portfolioAnalysisPerformanceMetricsBestPerformer = (PortfolioAnalysisPerformanceMetricsBestPerformer) o;
    return Objects.equals(this.investmentId, portfolioAnalysisPerformanceMetricsBestPerformer.investmentId) &&
        Objects.equals(this.roi, portfolioAnalysisPerformanceMetricsBestPerformer.roi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(investmentId, roi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortfolioAnalysisPerformanceMetricsBestPerformer {\n");
    sb.append("    investmentId: ").append(toIndentedString(investmentId)).append("\n");
    sb.append("    roi: ").append(toIndentedString(roi)).append("\n");
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

