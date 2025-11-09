package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.CalculationBreakdownAuditTrailInner;
import com.necpgame.economyservice.model.CalculationBreakdownFactors;
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
 * CalculationBreakdown
 */


public class CalculationBreakdown {

  private String baseRewardFormula;

  private CalculationBreakdownFactors factors;

  @Valid
  private List<@Valid CalculationBreakdownAuditTrailInner> auditTrail = new ArrayList<>();

  public CalculationBreakdown() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculationBreakdown(String baseRewardFormula, CalculationBreakdownFactors factors) {
    this.baseRewardFormula = baseRewardFormula;
    this.factors = factors;
  }

  public CalculationBreakdown baseRewardFormula(String baseRewardFormula) {
    this.baseRewardFormula = baseRewardFormula;
    return this;
  }

  /**
   * Get baseRewardFormula
   * @return baseRewardFormula
   */
  @NotNull 
  @Schema(name = "baseRewardFormula", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baseRewardFormula")
  public String getBaseRewardFormula() {
    return baseRewardFormula;
  }

  public void setBaseRewardFormula(String baseRewardFormula) {
    this.baseRewardFormula = baseRewardFormula;
  }

  public CalculationBreakdown factors(CalculationBreakdownFactors factors) {
    this.factors = factors;
    return this;
  }

  /**
   * Get factors
   * @return factors
   */
  @NotNull @Valid 
  @Schema(name = "factors", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factors")
  public CalculationBreakdownFactors getFactors() {
    return factors;
  }

  public void setFactors(CalculationBreakdownFactors factors) {
    this.factors = factors;
  }

  public CalculationBreakdown auditTrail(List<@Valid CalculationBreakdownAuditTrailInner> auditTrail) {
    this.auditTrail = auditTrail;
    return this;
  }

  public CalculationBreakdown addAuditTrailItem(CalculationBreakdownAuditTrailInner auditTrailItem) {
    if (this.auditTrail == null) {
      this.auditTrail = new ArrayList<>();
    }
    this.auditTrail.add(auditTrailItem);
    return this;
  }

  /**
   * Get auditTrail
   * @return auditTrail
   */
  @Valid 
  @Schema(name = "auditTrail", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auditTrail")
  public List<@Valid CalculationBreakdownAuditTrailInner> getAuditTrail() {
    return auditTrail;
  }

  public void setAuditTrail(List<@Valid CalculationBreakdownAuditTrailInner> auditTrail) {
    this.auditTrail = auditTrail;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculationBreakdown calculationBreakdown = (CalculationBreakdown) o;
    return Objects.equals(this.baseRewardFormula, calculationBreakdown.baseRewardFormula) &&
        Objects.equals(this.factors, calculationBreakdown.factors) &&
        Objects.equals(this.auditTrail, calculationBreakdown.auditTrail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseRewardFormula, factors, auditTrail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculationBreakdown {\n");
    sb.append("    baseRewardFormula: ").append(toIndentedString(baseRewardFormula)).append("\n");
    sb.append("    factors: ").append(toIndentedString(factors)).append("\n");
    sb.append("    auditTrail: ").append(toIndentedString(auditTrail)).append("\n");
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

