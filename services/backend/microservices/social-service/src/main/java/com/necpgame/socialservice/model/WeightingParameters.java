package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.WeightingParametersWeights;
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
 * WeightingParameters
 */


public class WeightingParameters {

  private @Nullable WeightingParametersWeights weights;

  private @Nullable BigDecimal totalWeight;

  public WeightingParameters weights(@Nullable WeightingParametersWeights weights) {
    this.weights = weights;
    return this;
  }

  /**
   * Get weights
   * @return weights
   */
  @Valid 
  @Schema(name = "weights", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weights")
  public @Nullable WeightingParametersWeights getWeights() {
    return weights;
  }

  public void setWeights(@Nullable WeightingParametersWeights weights) {
    this.weights = weights;
  }

  public WeightingParameters totalWeight(@Nullable BigDecimal totalWeight) {
    this.totalWeight = totalWeight;
    return this;
  }

  /**
   * Сумма весов (должна быть 1.0)
   * @return totalWeight
   */
  @Valid 
  @Schema(name = "total_weight", example = "1.0", description = "Сумма весов (должна быть 1.0)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_weight")
  public @Nullable BigDecimal getTotalWeight() {
    return totalWeight;
  }

  public void setTotalWeight(@Nullable BigDecimal totalWeight) {
    this.totalWeight = totalWeight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeightingParameters weightingParameters = (WeightingParameters) o;
    return Objects.equals(this.weights, weightingParameters.weights) &&
        Objects.equals(this.totalWeight, weightingParameters.totalWeight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weights, totalWeight);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeightingParameters {\n");
    sb.append("    weights: ").append(toIndentedString(weights)).append("\n");
    sb.append("    totalWeight: ").append(toIndentedString(totalWeight)).append("\n");
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

