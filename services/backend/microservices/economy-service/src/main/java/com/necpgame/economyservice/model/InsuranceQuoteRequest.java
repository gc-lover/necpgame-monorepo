package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InsuranceQuoteRequest
 */


public class InsuranceQuoteRequest {

  private UUID orderId;

  private Float riskScore;

  private Float desiredCoverage;

  /**
   * Gets or Sets planTier
   */
  public enum PlanTierEnum {
    BASIC("basic"),
    
    ENHANCED("enhanced"),
    
    PREMIUM("premium"),
    
    CORPORATE("corporate");

    private final String value;

    PlanTierEnum(String value) {
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
    public static PlanTierEnum fromValue(String value) {
      for (PlanTierEnum b : PlanTierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PlanTierEnum planTier;

  private @Nullable Boolean preferEscrowIncrease;

  public InsuranceQuoteRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InsuranceQuoteRequest(UUID orderId, Float riskScore, Float desiredCoverage) {
    this.orderId = orderId;
    this.riskScore = riskScore;
    this.desiredCoverage = desiredCoverage;
  }

  public InsuranceQuoteRequest orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @NotNull @Valid 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderId")
  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public InsuranceQuoteRequest riskScore(Float riskScore) {
    this.riskScore = riskScore;
    return this;
  }

  /**
   * Get riskScore
   * @return riskScore
   */
  @NotNull 
  @Schema(name = "riskScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskScore")
  public Float getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(Float riskScore) {
    this.riskScore = riskScore;
  }

  public InsuranceQuoteRequest desiredCoverage(Float desiredCoverage) {
    this.desiredCoverage = desiredCoverage;
    return this;
  }

  /**
   * Get desiredCoverage
   * minimum: 0
   * maximum: 1
   * @return desiredCoverage
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "desiredCoverage", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("desiredCoverage")
  public Float getDesiredCoverage() {
    return desiredCoverage;
  }

  public void setDesiredCoverage(Float desiredCoverage) {
    this.desiredCoverage = desiredCoverage;
  }

  public InsuranceQuoteRequest planTier(@Nullable PlanTierEnum planTier) {
    this.planTier = planTier;
    return this;
  }

  /**
   * Get planTier
   * @return planTier
   */
  
  @Schema(name = "planTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("planTier")
  public @Nullable PlanTierEnum getPlanTier() {
    return planTier;
  }

  public void setPlanTier(@Nullable PlanTierEnum planTier) {
    this.planTier = planTier;
  }

  public InsuranceQuoteRequest preferEscrowIncrease(@Nullable Boolean preferEscrowIncrease) {
    this.preferEscrowIncrease = preferEscrowIncrease;
    return this;
  }

  /**
   * Get preferEscrowIncrease
   * @return preferEscrowIncrease
   */
  
  @Schema(name = "preferEscrowIncrease", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferEscrowIncrease")
  public @Nullable Boolean getPreferEscrowIncrease() {
    return preferEscrowIncrease;
  }

  public void setPreferEscrowIncrease(@Nullable Boolean preferEscrowIncrease) {
    this.preferEscrowIncrease = preferEscrowIncrease;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InsuranceQuoteRequest insuranceQuoteRequest = (InsuranceQuoteRequest) o;
    return Objects.equals(this.orderId, insuranceQuoteRequest.orderId) &&
        Objects.equals(this.riskScore, insuranceQuoteRequest.riskScore) &&
        Objects.equals(this.desiredCoverage, insuranceQuoteRequest.desiredCoverage) &&
        Objects.equals(this.planTier, insuranceQuoteRequest.planTier) &&
        Objects.equals(this.preferEscrowIncrease, insuranceQuoteRequest.preferEscrowIncrease);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, riskScore, desiredCoverage, planTier, preferEscrowIncrease);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsuranceQuoteRequest {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    riskScore: ").append(toIndentedString(riskScore)).append("\n");
    sb.append("    desiredCoverage: ").append(toIndentedString(desiredCoverage)).append("\n");
    sb.append("    planTier: ").append(toIndentedString(planTier)).append("\n");
    sb.append("    preferEscrowIncrease: ").append(toIndentedString(preferEscrowIncrease)).append("\n");
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

