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
 * PortfolioAnalysisRiskBreakdown
 */

@JsonTypeName("PortfolioAnalysis_risk_breakdown")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class PortfolioAnalysisRiskBreakdown {

  private @Nullable BigDecimal lowRisk;

  private @Nullable BigDecimal mediumRisk;

  private @Nullable BigDecimal highRisk;

  public PortfolioAnalysisRiskBreakdown lowRisk(@Nullable BigDecimal lowRisk) {
    this.lowRisk = lowRisk;
    return this;
  }

  /**
   * Get lowRisk
   * @return lowRisk
   */
  @Valid 
  @Schema(name = "low_risk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("low_risk")
  public @Nullable BigDecimal getLowRisk() {
    return lowRisk;
  }

  public void setLowRisk(@Nullable BigDecimal lowRisk) {
    this.lowRisk = lowRisk;
  }

  public PortfolioAnalysisRiskBreakdown mediumRisk(@Nullable BigDecimal mediumRisk) {
    this.mediumRisk = mediumRisk;
    return this;
  }

  /**
   * Get mediumRisk
   * @return mediumRisk
   */
  @Valid 
  @Schema(name = "medium_risk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medium_risk")
  public @Nullable BigDecimal getMediumRisk() {
    return mediumRisk;
  }

  public void setMediumRisk(@Nullable BigDecimal mediumRisk) {
    this.mediumRisk = mediumRisk;
  }

  public PortfolioAnalysisRiskBreakdown highRisk(@Nullable BigDecimal highRisk) {
    this.highRisk = highRisk;
    return this;
  }

  /**
   * Get highRisk
   * @return highRisk
   */
  @Valid 
  @Schema(name = "high_risk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("high_risk")
  public @Nullable BigDecimal getHighRisk() {
    return highRisk;
  }

  public void setHighRisk(@Nullable BigDecimal highRisk) {
    this.highRisk = highRisk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortfolioAnalysisRiskBreakdown portfolioAnalysisRiskBreakdown = (PortfolioAnalysisRiskBreakdown) o;
    return Objects.equals(this.lowRisk, portfolioAnalysisRiskBreakdown.lowRisk) &&
        Objects.equals(this.mediumRisk, portfolioAnalysisRiskBreakdown.mediumRisk) &&
        Objects.equals(this.highRisk, portfolioAnalysisRiskBreakdown.highRisk);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lowRisk, mediumRisk, highRisk);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortfolioAnalysisRiskBreakdown {\n");
    sb.append("    lowRisk: ").append(toIndentedString(lowRisk)).append("\n");
    sb.append("    mediumRisk: ").append(toIndentedString(mediumRisk)).append("\n");
    sb.append("    highRisk: ").append(toIndentedString(highRisk)).append("\n");
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

