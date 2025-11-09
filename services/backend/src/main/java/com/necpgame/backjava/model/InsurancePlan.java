package com.necpgame.backjava.model;

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
 * InsurancePlan
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class InsurancePlan {

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

  private @Nullable Float costPercentage;

  private @Nullable String description;

  public InsurancePlan plan(@Nullable PlanEnum plan) {
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

  public InsurancePlan coveragePercentage(@Nullable Integer coveragePercentage) {
    this.coveragePercentage = coveragePercentage;
    return this;
  }

  /**
   * Get coveragePercentage
   * @return coveragePercentage
   */
  
  @Schema(name = "coverage_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("coverage_percentage")
  public @Nullable Integer getCoveragePercentage() {
    return coveragePercentage;
  }

  public void setCoveragePercentage(@Nullable Integer coveragePercentage) {
    this.coveragePercentage = coveragePercentage;
  }

  public InsurancePlan maxCoverage(@Nullable Integer maxCoverage) {
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

  public InsurancePlan costPercentage(@Nullable Float costPercentage) {
    this.costPercentage = costPercentage;
    return this;
  }

  /**
   * Процент от стоимости груза
   * @return costPercentage
   */
  
  @Schema(name = "cost_percentage", description = "Процент от стоимости груза", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_percentage")
  public @Nullable Float getCostPercentage() {
    return costPercentage;
  }

  public void setCostPercentage(@Nullable Float costPercentage) {
    this.costPercentage = costPercentage;
  }

  public InsurancePlan description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InsurancePlan insurancePlan = (InsurancePlan) o;
    return Objects.equals(this.plan, insurancePlan.plan) &&
        Objects.equals(this.coveragePercentage, insurancePlan.coveragePercentage) &&
        Objects.equals(this.maxCoverage, insurancePlan.maxCoverage) &&
        Objects.equals(this.costPercentage, insurancePlan.costPercentage) &&
        Objects.equals(this.description, insurancePlan.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(plan, coveragePercentage, maxCoverage, costPercentage, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsurancePlan {\n");
    sb.append("    plan: ").append(toIndentedString(plan)).append("\n");
    sb.append("    coveragePercentage: ").append(toIndentedString(coveragePercentage)).append("\n");
    sb.append("    maxCoverage: ").append(toIndentedString(maxCoverage)).append("\n");
    sb.append("    costPercentage: ").append(toIndentedString(costPercentage)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

