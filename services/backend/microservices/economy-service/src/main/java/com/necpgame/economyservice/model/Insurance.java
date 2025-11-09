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
 * Insurance
 */


public class Insurance {

  private @Nullable UUID insuranceId;

  /**
   * Gets or Sets plan
   */
  public enum PlanEnum {
    BASIC("BASIC"),
    
    STANDARD("STANDARD"),
    
    PREMIUM("PREMIUM");

    private final String value;

    PlanEnum(String value) {
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
    public static PlanEnum fromValue(String value) {
      for (PlanEnum b : PlanEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PlanEnum plan;

  private @Nullable Integer coveragePercentage;

  private @Nullable Integer maxCoverage;

  private @Nullable Integer cost;

  private @Nullable UUID shipmentId;

  public Insurance insuranceId(@Nullable UUID insuranceId) {
    this.insuranceId = insuranceId;
    return this;
  }

  /**
   * Get insuranceId
   * @return insuranceId
   */
  @Valid 
  @Schema(name = "insurance_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("insurance_id")
  public @Nullable UUID getInsuranceId() {
    return insuranceId;
  }

  public void setInsuranceId(@Nullable UUID insuranceId) {
    this.insuranceId = insuranceId;
  }

  public Insurance plan(@Nullable PlanEnum plan) {
    this.plan = plan;
    return this;
  }

  /**
   * Get plan
   * @return plan
   */
  
  @Schema(name = "plan", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("plan")
  public @Nullable PlanEnum getPlan() {
    return plan;
  }

  public void setPlan(@Nullable PlanEnum plan) {
    this.plan = plan;
  }

  public Insurance coveragePercentage(@Nullable Integer coveragePercentage) {
    this.coveragePercentage = coveragePercentage;
    return this;
  }

  /**
   * Get coveragePercentage
   * @return coveragePercentage
   */
  
  @Schema(name = "coverage_percentage", example = "50", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("coverage_percentage")
  public @Nullable Integer getCoveragePercentage() {
    return coveragePercentage;
  }

  public void setCoveragePercentage(@Nullable Integer coveragePercentage) {
    this.coveragePercentage = coveragePercentage;
  }

  public Insurance maxCoverage(@Nullable Integer maxCoverage) {
    this.maxCoverage = maxCoverage;
    return this;
  }

  /**
   * Get maxCoverage
   * @return maxCoverage
   */
  
  @Schema(name = "max_coverage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_coverage")
  public @Nullable Integer getMaxCoverage() {
    return maxCoverage;
  }

  public void setMaxCoverage(@Nullable Integer maxCoverage) {
    this.maxCoverage = maxCoverage;
  }

  public Insurance cost(@Nullable Integer cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost")
  public @Nullable Integer getCost() {
    return cost;
  }

  public void setCost(@Nullable Integer cost) {
    this.cost = cost;
  }

  public Insurance shipmentId(@Nullable UUID shipmentId) {
    this.shipmentId = shipmentId;
    return this;
  }

  /**
   * Get shipmentId
   * @return shipmentId
   */
  @Valid 
  @Schema(name = "shipment_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shipment_id")
  public @Nullable UUID getShipmentId() {
    return shipmentId;
  }

  public void setShipmentId(@Nullable UUID shipmentId) {
    this.shipmentId = shipmentId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Insurance insurance = (Insurance) o;
    return Objects.equals(this.insuranceId, insurance.insuranceId) &&
        Objects.equals(this.plan, insurance.plan) &&
        Objects.equals(this.coveragePercentage, insurance.coveragePercentage) &&
        Objects.equals(this.maxCoverage, insurance.maxCoverage) &&
        Objects.equals(this.cost, insurance.cost) &&
        Objects.equals(this.shipmentId, insurance.shipmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(insuranceId, plan, coveragePercentage, maxCoverage, cost, shipmentId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Insurance {\n");
    sb.append("    insuranceId: ").append(toIndentedString(insuranceId)).append("\n");
    sb.append("    plan: ").append(toIndentedString(plan)).append("\n");
    sb.append("    coveragePercentage: ").append(toIndentedString(coveragePercentage)).append("\n");
    sb.append("    maxCoverage: ").append(toIndentedString(maxCoverage)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
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

