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
 * PlayerOrderBudgetEstimateResponseRecommendedBudgetRange
 */

@JsonTypeName("PlayerOrderBudgetEstimateResponse_recommendedBudgetRange")

public class PlayerOrderBudgetEstimateResponseRecommendedBudgetRange {

  private Float min;

  private Float max;

  public PlayerOrderBudgetEstimateResponseRecommendedBudgetRange() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderBudgetEstimateResponseRecommendedBudgetRange(Float min, Float max) {
    this.min = min;
    this.max = max;
  }

  public PlayerOrderBudgetEstimateResponseRecommendedBudgetRange min(Float min) {
    this.min = min;
    return this;
  }

  /**
   * Get min
   * @return min
   */
  @NotNull 
  @Schema(name = "min", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("min")
  public Float getMin() {
    return min;
  }

  public void setMin(Float min) {
    this.min = min;
  }

  public PlayerOrderBudgetEstimateResponseRecommendedBudgetRange max(Float max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * @return max
   */
  @NotNull 
  @Schema(name = "max", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("max")
  public Float getMax() {
    return max;
  }

  public void setMax(Float max) {
    this.max = max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderBudgetEstimateResponseRecommendedBudgetRange playerOrderBudgetEstimateResponseRecommendedBudgetRange = (PlayerOrderBudgetEstimateResponseRecommendedBudgetRange) o;
    return Objects.equals(this.min, playerOrderBudgetEstimateResponseRecommendedBudgetRange.min) &&
        Objects.equals(this.max, playerOrderBudgetEstimateResponseRecommendedBudgetRange.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderBudgetEstimateResponseRecommendedBudgetRange {\n");
    sb.append("    min: ").append(toIndentedString(min)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
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

