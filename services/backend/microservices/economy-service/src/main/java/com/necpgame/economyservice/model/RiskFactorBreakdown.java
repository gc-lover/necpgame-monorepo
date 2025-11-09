package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RiskFactorBreakdown
 */


public class RiskFactorBreakdown {

  /**
   * Gets or Sets factor
   */
  public enum FactorEnum {
    REGION_THREAT("regionThreat"),
    
    FACTION_CONFLICT("factionConflict"),
    
    PLAYER_RATING("playerRating"),
    
    DISPUTE_HISTORY("disputeHistory"),
    
    ESCROW_HISTORY("escrowHistory"),
    
    ORDER_COMPLEXITY("orderComplexity"),
    
    DEADLINE_PRESSURE("deadlinePressure");

    private final String value;

    FactorEnum(String value) {
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
    public static FactorEnum fromValue(String value) {
      for (FactorEnum b : FactorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private FactorEnum factor;

  private Float weight;

  private Float contribution;

  public RiskFactorBreakdown() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskFactorBreakdown(FactorEnum factor, Float weight, Float contribution) {
    this.factor = factor;
    this.weight = weight;
    this.contribution = contribution;
  }

  public RiskFactorBreakdown factor(FactorEnum factor) {
    this.factor = factor;
    return this;
  }

  /**
   * Get factor
   * @return factor
   */
  @NotNull 
  @Schema(name = "factor", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factor")
  public FactorEnum getFactor() {
    return factor;
  }

  public void setFactor(FactorEnum factor) {
    this.factor = factor;
  }

  public RiskFactorBreakdown weight(Float weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * @return weight
   */
  @NotNull 
  @Schema(name = "weight", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weight")
  public Float getWeight() {
    return weight;
  }

  public void setWeight(Float weight) {
    this.weight = weight;
  }

  public RiskFactorBreakdown contribution(Float contribution) {
    this.contribution = contribution;
    return this;
  }

  /**
   * Get contribution
   * @return contribution
   */
  @NotNull 
  @Schema(name = "contribution", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contribution")
  public Float getContribution() {
    return contribution;
  }

  public void setContribution(Float contribution) {
    this.contribution = contribution;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskFactorBreakdown riskFactorBreakdown = (RiskFactorBreakdown) o;
    return Objects.equals(this.factor, riskFactorBreakdown.factor) &&
        Objects.equals(this.weight, riskFactorBreakdown.weight) &&
        Objects.equals(this.contribution, riskFactorBreakdown.contribution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factor, weight, contribution);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskFactorBreakdown {\n");
    sb.append("    factor: ").append(toIndentedString(factor)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    contribution: ").append(toIndentedString(contribution)).append("\n");
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

