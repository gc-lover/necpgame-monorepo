package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.Violation;
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
 * GetPlayerViolationHistory200Response
 */

@JsonTypeName("getPlayerViolationHistory_200_response")

public class GetPlayerViolationHistory200Response {

  @Valid
  private List<@Valid Violation> violations = new ArrayList<>();

  private @Nullable Integer totalViolations;

  private @Nullable BigDecimal riskScore;

  public GetPlayerViolationHistory200Response violations(List<@Valid Violation> violations) {
    this.violations = violations;
    return this;
  }

  public GetPlayerViolationHistory200Response addViolationsItem(Violation violationsItem) {
    if (this.violations == null) {
      this.violations = new ArrayList<>();
    }
    this.violations.add(violationsItem);
    return this;
  }

  /**
   * Get violations
   * @return violations
   */
  @Valid 
  @Schema(name = "violations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("violations")
  public List<@Valid Violation> getViolations() {
    return violations;
  }

  public void setViolations(List<@Valid Violation> violations) {
    this.violations = violations;
  }

  public GetPlayerViolationHistory200Response totalViolations(@Nullable Integer totalViolations) {
    this.totalViolations = totalViolations;
    return this;
  }

  /**
   * Get totalViolations
   * @return totalViolations
   */
  
  @Schema(name = "total_violations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_violations")
  public @Nullable Integer getTotalViolations() {
    return totalViolations;
  }

  public void setTotalViolations(@Nullable Integer totalViolations) {
    this.totalViolations = totalViolations;
  }

  public GetPlayerViolationHistory200Response riskScore(@Nullable BigDecimal riskScore) {
    this.riskScore = riskScore;
    return this;
  }

  /**
   * Риск скор игрока (0-100)
   * @return riskScore
   */
  @Valid 
  @Schema(name = "risk_score", description = "Риск скор игрока (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_score")
  public @Nullable BigDecimal getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(@Nullable BigDecimal riskScore) {
    this.riskScore = riskScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPlayerViolationHistory200Response getPlayerViolationHistory200Response = (GetPlayerViolationHistory200Response) o;
    return Objects.equals(this.violations, getPlayerViolationHistory200Response.violations) &&
        Objects.equals(this.totalViolations, getPlayerViolationHistory200Response.totalViolations) &&
        Objects.equals(this.riskScore, getPlayerViolationHistory200Response.riskScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(violations, totalViolations, riskScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPlayerViolationHistory200Response {\n");
    sb.append("    violations: ").append(toIndentedString(violations)).append("\n");
    sb.append("    totalViolations: ").append(toIndentedString(totalViolations)).append("\n");
    sb.append("    riskScore: ").append(toIndentedString(riskScore)).append("\n");
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

