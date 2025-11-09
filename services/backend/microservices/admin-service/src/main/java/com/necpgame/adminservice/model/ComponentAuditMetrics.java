package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ComponentAuditMetrics
 */

@JsonTypeName("ComponentAudit_metrics")

public class ComponentAuditMetrics {

  private @Nullable Integer codeCoverage;

  private @Nullable Integer performanceScore;

  private @Nullable Integer securityScore;

  private @Nullable Integer maintainabilityIndex;

  public ComponentAuditMetrics codeCoverage(@Nullable Integer codeCoverage) {
    this.codeCoverage = codeCoverage;
    return this;
  }

  /**
   * Code coverage %
   * minimum: 0
   * maximum: 100
   * @return codeCoverage
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "code_coverage", example = "95", description = "Code coverage %", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("code_coverage")
  public @Nullable Integer getCodeCoverage() {
    return codeCoverage;
  }

  public void setCodeCoverage(@Nullable Integer codeCoverage) {
    this.codeCoverage = codeCoverage;
  }

  public ComponentAuditMetrics performanceScore(@Nullable Integer performanceScore) {
    this.performanceScore = performanceScore;
    return this;
  }

  /**
   * Performance score (0-100)
   * @return performanceScore
   */
  
  @Schema(name = "performance_score", example = "92", description = "Performance score (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performance_score")
  public @Nullable Integer getPerformanceScore() {
    return performanceScore;
  }

  public void setPerformanceScore(@Nullable Integer performanceScore) {
    this.performanceScore = performanceScore;
  }

  public ComponentAuditMetrics securityScore(@Nullable Integer securityScore) {
    this.securityScore = securityScore;
    return this;
  }

  /**
   * Security score (0-100)
   * @return securityScore
   */
  
  @Schema(name = "security_score", example = "98", description = "Security score (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("security_score")
  public @Nullable Integer getSecurityScore() {
    return securityScore;
  }

  public void setSecurityScore(@Nullable Integer securityScore) {
    this.securityScore = securityScore;
  }

  public ComponentAuditMetrics maintainabilityIndex(@Nullable Integer maintainabilityIndex) {
    this.maintainabilityIndex = maintainabilityIndex;
    return this;
  }

  /**
   * Maintainability index (0-100)
   * @return maintainabilityIndex
   */
  
  @Schema(name = "maintainability_index", example = "85", description = "Maintainability index (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maintainability_index")
  public @Nullable Integer getMaintainabilityIndex() {
    return maintainabilityIndex;
  }

  public void setMaintainabilityIndex(@Nullable Integer maintainabilityIndex) {
    this.maintainabilityIndex = maintainabilityIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComponentAuditMetrics componentAuditMetrics = (ComponentAuditMetrics) o;
    return Objects.equals(this.codeCoverage, componentAuditMetrics.codeCoverage) &&
        Objects.equals(this.performanceScore, componentAuditMetrics.performanceScore) &&
        Objects.equals(this.securityScore, componentAuditMetrics.securityScore) &&
        Objects.equals(this.maintainabilityIndex, componentAuditMetrics.maintainabilityIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codeCoverage, performanceScore, securityScore, maintainabilityIndex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComponentAuditMetrics {\n");
    sb.append("    codeCoverage: ").append(toIndentedString(codeCoverage)).append("\n");
    sb.append("    performanceScore: ").append(toIndentedString(performanceScore)).append("\n");
    sb.append("    securityScore: ").append(toIndentedString(securityScore)).append("\n");
    sb.append("    maintainabilityIndex: ").append(toIndentedString(maintainabilityIndex)).append("\n");
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

