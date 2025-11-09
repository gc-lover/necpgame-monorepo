package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * RetentionMetricsCohortAnalysisInner
 */

@JsonTypeName("RetentionMetrics_cohort_analysis_inner")

public class RetentionMetricsCohortAnalysisInner {

  private @Nullable String cohort;

  private @Nullable BigDecimal retention;

  public RetentionMetricsCohortAnalysisInner cohort(@Nullable String cohort) {
    this.cohort = cohort;
    return this;
  }

  /**
   * Get cohort
   * @return cohort
   */
  
  @Schema(name = "cohort", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cohort")
  public @Nullable String getCohort() {
    return cohort;
  }

  public void setCohort(@Nullable String cohort) {
    this.cohort = cohort;
  }

  public RetentionMetricsCohortAnalysisInner retention(@Nullable BigDecimal retention) {
    this.retention = retention;
    return this;
  }

  /**
   * Get retention
   * @return retention
   */
  @Valid 
  @Schema(name = "retention", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("retention")
  public @Nullable BigDecimal getRetention() {
    return retention;
  }

  public void setRetention(@Nullable BigDecimal retention) {
    this.retention = retention;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RetentionMetricsCohortAnalysisInner retentionMetricsCohortAnalysisInner = (RetentionMetricsCohortAnalysisInner) o;
    return Objects.equals(this.cohort, retentionMetricsCohortAnalysisInner.cohort) &&
        Objects.equals(this.retention, retentionMetricsCohortAnalysisInner.retention);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cohort, retention);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetentionMetricsCohortAnalysisInner {\n");
    sb.append("    cohort: ").append(toIndentedString(cohort)).append("\n");
    sb.append("    retention: ").append(toIndentedString(retention)).append("\n");
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

