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
 * TechnicalDebtSummaryByCategory
 */

@JsonTypeName("TechnicalDebtSummary_by_category")

public class TechnicalDebtSummaryByCategory {

  private @Nullable Integer security;

  private @Nullable Integer performance;

  private @Nullable Integer scalability;

  private @Nullable Integer maintainability;

  public TechnicalDebtSummaryByCategory security(@Nullable Integer security) {
    this.security = security;
    return this;
  }

  /**
   * Get security
   * @return security
   */
  
  @Schema(name = "security", example = "20", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("security")
  public @Nullable Integer getSecurity() {
    return security;
  }

  public void setSecurity(@Nullable Integer security) {
    this.security = security;
  }

  public TechnicalDebtSummaryByCategory performance(@Nullable Integer performance) {
    this.performance = performance;
    return this;
  }

  /**
   * Get performance
   * @return performance
   */
  
  @Schema(name = "performance", example = "40", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performance")
  public @Nullable Integer getPerformance() {
    return performance;
  }

  public void setPerformance(@Nullable Integer performance) {
    this.performance = performance;
  }

  public TechnicalDebtSummaryByCategory scalability(@Nullable Integer scalability) {
    this.scalability = scalability;
    return this;
  }

  /**
   * Get scalability
   * @return scalability
   */
  
  @Schema(name = "scalability", example = "30", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scalability")
  public @Nullable Integer getScalability() {
    return scalability;
  }

  public void setScalability(@Nullable Integer scalability) {
    this.scalability = scalability;
  }

  public TechnicalDebtSummaryByCategory maintainability(@Nullable Integer maintainability) {
    this.maintainability = maintainability;
    return this;
  }

  /**
   * Get maintainability
   * @return maintainability
   */
  
  @Schema(name = "maintainability", example = "30", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maintainability")
  public @Nullable Integer getMaintainability() {
    return maintainability;
  }

  public void setMaintainability(@Nullable Integer maintainability) {
    this.maintainability = maintainability;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnicalDebtSummaryByCategory technicalDebtSummaryByCategory = (TechnicalDebtSummaryByCategory) o;
    return Objects.equals(this.security, technicalDebtSummaryByCategory.security) &&
        Objects.equals(this.performance, technicalDebtSummaryByCategory.performance) &&
        Objects.equals(this.scalability, technicalDebtSummaryByCategory.scalability) &&
        Objects.equals(this.maintainability, technicalDebtSummaryByCategory.maintainability);
  }

  @Override
  public int hashCode() {
    return Objects.hash(security, performance, scalability, maintainability);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnicalDebtSummaryByCategory {\n");
    sb.append("    security: ").append(toIndentedString(security)).append("\n");
    sb.append("    performance: ").append(toIndentedString(performance)).append("\n");
    sb.append("    scalability: ").append(toIndentedString(scalability)).append("\n");
    sb.append("    maintainability: ").append(toIndentedString(maintainability)).append("\n");
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

