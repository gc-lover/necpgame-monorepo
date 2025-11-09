package com.necpgame.economyservice.model;

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
 * CalculationBreakdownFactors
 */

@JsonTypeName("CalculationBreakdown_factors")

public class CalculationBreakdownFactors {

  private Float complexityScore;

  private Float riskModifier;

  private Float marketIndex;

  private Float timeModifier;

  private @Nullable Float districtAdjustment;

  private @Nullable Float factionAdjustment;

  private @Nullable Float manualAdjustment;

  public CalculationBreakdownFactors() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculationBreakdownFactors(Float complexityScore, Float riskModifier, Float marketIndex, Float timeModifier) {
    this.complexityScore = complexityScore;
    this.riskModifier = riskModifier;
    this.marketIndex = marketIndex;
    this.timeModifier = timeModifier;
  }

  public CalculationBreakdownFactors complexityScore(Float complexityScore) {
    this.complexityScore = complexityScore;
    return this;
  }

  /**
   * Get complexityScore
   * @return complexityScore
   */
  @NotNull 
  @Schema(name = "complexityScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("complexityScore")
  public Float getComplexityScore() {
    return complexityScore;
  }

  public void setComplexityScore(Float complexityScore) {
    this.complexityScore = complexityScore;
  }

  public CalculationBreakdownFactors riskModifier(Float riskModifier) {
    this.riskModifier = riskModifier;
    return this;
  }

  /**
   * Get riskModifier
   * @return riskModifier
   */
  @NotNull 
  @Schema(name = "riskModifier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskModifier")
  public Float getRiskModifier() {
    return riskModifier;
  }

  public void setRiskModifier(Float riskModifier) {
    this.riskModifier = riskModifier;
  }

  public CalculationBreakdownFactors marketIndex(Float marketIndex) {
    this.marketIndex = marketIndex;
    return this;
  }

  /**
   * Get marketIndex
   * @return marketIndex
   */
  @NotNull 
  @Schema(name = "marketIndex", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("marketIndex")
  public Float getMarketIndex() {
    return marketIndex;
  }

  public void setMarketIndex(Float marketIndex) {
    this.marketIndex = marketIndex;
  }

  public CalculationBreakdownFactors timeModifier(Float timeModifier) {
    this.timeModifier = timeModifier;
    return this;
  }

  /**
   * Get timeModifier
   * @return timeModifier
   */
  @NotNull 
  @Schema(name = "timeModifier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timeModifier")
  public Float getTimeModifier() {
    return timeModifier;
  }

  public void setTimeModifier(Float timeModifier) {
    this.timeModifier = timeModifier;
  }

  public CalculationBreakdownFactors districtAdjustment(@Nullable Float districtAdjustment) {
    this.districtAdjustment = districtAdjustment;
    return this;
  }

  /**
   * Get districtAdjustment
   * @return districtAdjustment
   */
  
  @Schema(name = "districtAdjustment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("districtAdjustment")
  public @Nullable Float getDistrictAdjustment() {
    return districtAdjustment;
  }

  public void setDistrictAdjustment(@Nullable Float districtAdjustment) {
    this.districtAdjustment = districtAdjustment;
  }

  public CalculationBreakdownFactors factionAdjustment(@Nullable Float factionAdjustment) {
    this.factionAdjustment = factionAdjustment;
    return this;
  }

  /**
   * Get factionAdjustment
   * @return factionAdjustment
   */
  
  @Schema(name = "factionAdjustment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionAdjustment")
  public @Nullable Float getFactionAdjustment() {
    return factionAdjustment;
  }

  public void setFactionAdjustment(@Nullable Float factionAdjustment) {
    this.factionAdjustment = factionAdjustment;
  }

  public CalculationBreakdownFactors manualAdjustment(@Nullable Float manualAdjustment) {
    this.manualAdjustment = manualAdjustment;
    return this;
  }

  /**
   * Get manualAdjustment
   * @return manualAdjustment
   */
  
  @Schema(name = "manualAdjustment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("manualAdjustment")
  public @Nullable Float getManualAdjustment() {
    return manualAdjustment;
  }

  public void setManualAdjustment(@Nullable Float manualAdjustment) {
    this.manualAdjustment = manualAdjustment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculationBreakdownFactors calculationBreakdownFactors = (CalculationBreakdownFactors) o;
    return Objects.equals(this.complexityScore, calculationBreakdownFactors.complexityScore) &&
        Objects.equals(this.riskModifier, calculationBreakdownFactors.riskModifier) &&
        Objects.equals(this.marketIndex, calculationBreakdownFactors.marketIndex) &&
        Objects.equals(this.timeModifier, calculationBreakdownFactors.timeModifier) &&
        Objects.equals(this.districtAdjustment, calculationBreakdownFactors.districtAdjustment) &&
        Objects.equals(this.factionAdjustment, calculationBreakdownFactors.factionAdjustment) &&
        Objects.equals(this.manualAdjustment, calculationBreakdownFactors.manualAdjustment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(complexityScore, riskModifier, marketIndex, timeModifier, districtAdjustment, factionAdjustment, manualAdjustment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculationBreakdownFactors {\n");
    sb.append("    complexityScore: ").append(toIndentedString(complexityScore)).append("\n");
    sb.append("    riskModifier: ").append(toIndentedString(riskModifier)).append("\n");
    sb.append("    marketIndex: ").append(toIndentedString(marketIndex)).append("\n");
    sb.append("    timeModifier: ").append(toIndentedString(timeModifier)).append("\n");
    sb.append("    districtAdjustment: ").append(toIndentedString(districtAdjustment)).append("\n");
    sb.append("    factionAdjustment: ").append(toIndentedString(factionAdjustment)).append("\n");
    sb.append("    manualAdjustment: ").append(toIndentedString(manualAdjustment)).append("\n");
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

