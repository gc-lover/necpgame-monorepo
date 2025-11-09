package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OptimizationResultExpectedImprovement
 */

@JsonTypeName("OptimizationResult_expected_improvement")

public class OptimizationResultExpectedImprovement {

  private @Nullable BigDecimal profitIncrease;

  private @Nullable BigDecimal timeReduction;

  public OptimizationResultExpectedImprovement profitIncrease(@Nullable BigDecimal profitIncrease) {
    this.profitIncrease = profitIncrease;
    return this;
  }

  /**
   * Get profitIncrease
   * @return profitIncrease
   */
  @Valid 
  @Schema(name = "profit_increase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_increase")
  public @Nullable BigDecimal getProfitIncrease() {
    return profitIncrease;
  }

  public void setProfitIncrease(@Nullable BigDecimal profitIncrease) {
    this.profitIncrease = profitIncrease;
  }

  public OptimizationResultExpectedImprovement timeReduction(@Nullable BigDecimal timeReduction) {
    this.timeReduction = timeReduction;
    return this;
  }

  /**
   * Get timeReduction
   * @return timeReduction
   */
  @Valid 
  @Schema(name = "time_reduction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_reduction")
  public @Nullable BigDecimal getTimeReduction() {
    return timeReduction;
  }

  public void setTimeReduction(@Nullable BigDecimal timeReduction) {
    this.timeReduction = timeReduction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OptimizationResultExpectedImprovement optimizationResultExpectedImprovement = (OptimizationResultExpectedImprovement) o;
    return Objects.equals(this.profitIncrease, optimizationResultExpectedImprovement.profitIncrease) &&
        Objects.equals(this.timeReduction, optimizationResultExpectedImprovement.timeReduction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(profitIncrease, timeReduction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OptimizationResultExpectedImprovement {\n");
    sb.append("    profitIncrease: ").append(toIndentedString(profitIncrease)).append("\n");
    sb.append("    timeReduction: ").append(toIndentedString(timeReduction)).append("\n");
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

