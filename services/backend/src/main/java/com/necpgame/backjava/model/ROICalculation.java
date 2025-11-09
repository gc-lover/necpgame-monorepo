package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * ROICalculation
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ROICalculation {

  private @Nullable String opportunityId;

  private @Nullable Integer investmentAmount;

  private @Nullable Integer durationDays;

  private @Nullable Integer expectedReturn;

  private @Nullable Float expectedRoi;

  private @Nullable Float riskAdjustedRoi;

  /**
   * Gets or Sets confidenceLevel
   */
  public enum ConfidenceLevelEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH");

    private final String value;

    ConfidenceLevelEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ConfidenceLevelEnum fromValue(String value) {
      for (ConfidenceLevelEnum b : ConfidenceLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ConfidenceLevelEnum confidenceLevel;

  public ROICalculation opportunityId(@Nullable String opportunityId) {
    this.opportunityId = opportunityId;
    return this;
  }

  /**
   * Get opportunityId
   * @return opportunityId
   */
  
  @Schema(name = "opportunity_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opportunity_id")
  public @Nullable String getOpportunityId() {
    return opportunityId;
  }

  public void setOpportunityId(@Nullable String opportunityId) {
    this.opportunityId = opportunityId;
  }

  public ROICalculation investmentAmount(@Nullable Integer investmentAmount) {
    this.investmentAmount = investmentAmount;
    return this;
  }

  /**
   * Get investmentAmount
   * @return investmentAmount
   */
  
  @Schema(name = "investment_amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("investment_amount")
  public @Nullable Integer getInvestmentAmount() {
    return investmentAmount;
  }

  public void setInvestmentAmount(@Nullable Integer investmentAmount) {
    this.investmentAmount = investmentAmount;
  }

  public ROICalculation durationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
    return this;
  }

  /**
   * Get durationDays
   * @return durationDays
   */
  
  @Schema(name = "duration_days", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_days")
  public @Nullable Integer getDurationDays() {
    return durationDays;
  }

  public void setDurationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
  }

  public ROICalculation expectedReturn(@Nullable Integer expectedReturn) {
    this.expectedReturn = expectedReturn;
    return this;
  }

  /**
   * Get expectedReturn
   * @return expectedReturn
   */
  
  @Schema(name = "expected_return", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expected_return")
  public @Nullable Integer getExpectedReturn() {
    return expectedReturn;
  }

  public void setExpectedReturn(@Nullable Integer expectedReturn) {
    this.expectedReturn = expectedReturn;
  }

  public ROICalculation expectedRoi(@Nullable Float expectedRoi) {
    this.expectedRoi = expectedRoi;
    return this;
  }

  /**
   * Get expectedRoi
   * @return expectedRoi
   */
  
  @Schema(name = "expected_roi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expected_roi")
  public @Nullable Float getExpectedRoi() {
    return expectedRoi;
  }

  public void setExpectedRoi(@Nullable Float expectedRoi) {
    this.expectedRoi = expectedRoi;
  }

  public ROICalculation riskAdjustedRoi(@Nullable Float riskAdjustedRoi) {
    this.riskAdjustedRoi = riskAdjustedRoi;
    return this;
  }

  /**
   * Get riskAdjustedRoi
   * @return riskAdjustedRoi
   */
  
  @Schema(name = "risk_adjusted_roi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_adjusted_roi")
  public @Nullable Float getRiskAdjustedRoi() {
    return riskAdjustedRoi;
  }

  public void setRiskAdjustedRoi(@Nullable Float riskAdjustedRoi) {
    this.riskAdjustedRoi = riskAdjustedRoi;
  }

  public ROICalculation confidenceLevel(@Nullable ConfidenceLevelEnum confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
    return this;
  }

  /**
   * Get confidenceLevel
   * @return confidenceLevel
   */
  
  @Schema(name = "confidence_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("confidence_level")
  public @Nullable ConfidenceLevelEnum getConfidenceLevel() {
    return confidenceLevel;
  }

  public void setConfidenceLevel(@Nullable ConfidenceLevelEnum confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ROICalculation roICalculation = (ROICalculation) o;
    return Objects.equals(this.opportunityId, roICalculation.opportunityId) &&
        Objects.equals(this.investmentAmount, roICalculation.investmentAmount) &&
        Objects.equals(this.durationDays, roICalculation.durationDays) &&
        Objects.equals(this.expectedReturn, roICalculation.expectedReturn) &&
        Objects.equals(this.expectedRoi, roICalculation.expectedRoi) &&
        Objects.equals(this.riskAdjustedRoi, roICalculation.riskAdjustedRoi) &&
        Objects.equals(this.confidenceLevel, roICalculation.confidenceLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(opportunityId, investmentAmount, durationDays, expectedReturn, expectedRoi, riskAdjustedRoi, confidenceLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ROICalculation {\n");
    sb.append("    opportunityId: ").append(toIndentedString(opportunityId)).append("\n");
    sb.append("    investmentAmount: ").append(toIndentedString(investmentAmount)).append("\n");
    sb.append("    durationDays: ").append(toIndentedString(durationDays)).append("\n");
    sb.append("    expectedReturn: ").append(toIndentedString(expectedReturn)).append("\n");
    sb.append("    expectedRoi: ").append(toIndentedString(expectedRoi)).append("\n");
    sb.append("    riskAdjustedRoi: ").append(toIndentedString(riskAdjustedRoi)).append("\n");
    sb.append("    confidenceLevel: ").append(toIndentedString(confidenceLevel)).append("\n");
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

