package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.RetentionMetricsCohortAnalysisInner;
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
 * RetentionMetrics
 */


public class RetentionMetrics {

  private @Nullable BigDecimal day1Retention;

  private @Nullable BigDecimal day7Retention;

  private @Nullable BigDecimal day30Retention;

  private @Nullable BigDecimal churnRate;

  @Valid
  private List<@Valid RetentionMetricsCohortAnalysisInner> cohortAnalysis = new ArrayList<>();

  public RetentionMetrics day1Retention(@Nullable BigDecimal day1Retention) {
    this.day1Retention = day1Retention;
    return this;
  }

  /**
   * Get day1Retention
   * @return day1Retention
   */
  @Valid 
  @Schema(name = "day1_retention", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("day1_retention")
  public @Nullable BigDecimal getDay1Retention() {
    return day1Retention;
  }

  public void setDay1Retention(@Nullable BigDecimal day1Retention) {
    this.day1Retention = day1Retention;
  }

  public RetentionMetrics day7Retention(@Nullable BigDecimal day7Retention) {
    this.day7Retention = day7Retention;
    return this;
  }

  /**
   * Get day7Retention
   * @return day7Retention
   */
  @Valid 
  @Schema(name = "day7_retention", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("day7_retention")
  public @Nullable BigDecimal getDay7Retention() {
    return day7Retention;
  }

  public void setDay7Retention(@Nullable BigDecimal day7Retention) {
    this.day7Retention = day7Retention;
  }

  public RetentionMetrics day30Retention(@Nullable BigDecimal day30Retention) {
    this.day30Retention = day30Retention;
    return this;
  }

  /**
   * Get day30Retention
   * @return day30Retention
   */
  @Valid 
  @Schema(name = "day30_retention", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("day30_retention")
  public @Nullable BigDecimal getDay30Retention() {
    return day30Retention;
  }

  public void setDay30Retention(@Nullable BigDecimal day30Retention) {
    this.day30Retention = day30Retention;
  }

  public RetentionMetrics churnRate(@Nullable BigDecimal churnRate) {
    this.churnRate = churnRate;
    return this;
  }

  /**
   * Get churnRate
   * @return churnRate
   */
  @Valid 
  @Schema(name = "churn_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("churn_rate")
  public @Nullable BigDecimal getChurnRate() {
    return churnRate;
  }

  public void setChurnRate(@Nullable BigDecimal churnRate) {
    this.churnRate = churnRate;
  }

  public RetentionMetrics cohortAnalysis(List<@Valid RetentionMetricsCohortAnalysisInner> cohortAnalysis) {
    this.cohortAnalysis = cohortAnalysis;
    return this;
  }

  public RetentionMetrics addCohortAnalysisItem(RetentionMetricsCohortAnalysisInner cohortAnalysisItem) {
    if (this.cohortAnalysis == null) {
      this.cohortAnalysis = new ArrayList<>();
    }
    this.cohortAnalysis.add(cohortAnalysisItem);
    return this;
  }

  /**
   * Get cohortAnalysis
   * @return cohortAnalysis
   */
  @Valid 
  @Schema(name = "cohort_analysis", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cohort_analysis")
  public List<@Valid RetentionMetricsCohortAnalysisInner> getCohortAnalysis() {
    return cohortAnalysis;
  }

  public void setCohortAnalysis(List<@Valid RetentionMetricsCohortAnalysisInner> cohortAnalysis) {
    this.cohortAnalysis = cohortAnalysis;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RetentionMetrics retentionMetrics = (RetentionMetrics) o;
    return Objects.equals(this.day1Retention, retentionMetrics.day1Retention) &&
        Objects.equals(this.day7Retention, retentionMetrics.day7Retention) &&
        Objects.equals(this.day30Retention, retentionMetrics.day30Retention) &&
        Objects.equals(this.churnRate, retentionMetrics.churnRate) &&
        Objects.equals(this.cohortAnalysis, retentionMetrics.cohortAnalysis);
  }

  @Override
  public int hashCode() {
    return Objects.hash(day1Retention, day7Retention, day30Retention, churnRate, cohortAnalysis);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetentionMetrics {\n");
    sb.append("    day1Retention: ").append(toIndentedString(day1Retention)).append("\n");
    sb.append("    day7Retention: ").append(toIndentedString(day7Retention)).append("\n");
    sb.append("    day30Retention: ").append(toIndentedString(day30Retention)).append("\n");
    sb.append("    churnRate: ").append(toIndentedString(churnRate)).append("\n");
    sb.append("    cohortAnalysis: ").append(toIndentedString(cohortAnalysis)).append("\n");
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

