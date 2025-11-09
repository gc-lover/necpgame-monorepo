package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RatingMetricValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Набор метрик и весов для конкретной роли.
 */

@Schema(name = "RatingMetricSet", description = "Набор метрик и весов для конкретной роли.")

public class RatingMetricSet {

  @Valid
  private Map<String, Float> weights = new HashMap<>();

  @Valid
  private List<@Valid RatingMetricValue> values = new ArrayList<>();

  public RatingMetricSet() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingMetricSet(Map<String, Float> weights, List<@Valid RatingMetricValue> values) {
    this.weights = weights;
    this.values = values;
  }

  public RatingMetricSet weights(Map<String, Float> weights) {
    this.weights = weights;
    return this;
  }

  public RatingMetricSet putWeightsItem(String key, Float weightsItem) {
    if (this.weights == null) {
      this.weights = new HashMap<>();
    }
    this.weights.put(key, weightsItem);
    return this;
  }

  /**
   * Get weights
   * @return weights
   */
  @NotNull 
  @Schema(name = "weights", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weights")
  public Map<String, Float> getWeights() {
    return weights;
  }

  public void setWeights(Map<String, Float> weights) {
    this.weights = weights;
  }

  public RatingMetricSet values(List<@Valid RatingMetricValue> values) {
    this.values = values;
    return this;
  }

  public RatingMetricSet addValuesItem(RatingMetricValue valuesItem) {
    if (this.values == null) {
      this.values = new ArrayList<>();
    }
    this.values.add(valuesItem);
    return this;
  }

  /**
   * Get values
   * @return values
   */
  @NotNull @Valid 
  @Schema(name = "values", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("values")
  public List<@Valid RatingMetricValue> getValues() {
    return values;
  }

  public void setValues(List<@Valid RatingMetricValue> values) {
    this.values = values;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingMetricSet ratingMetricSet = (RatingMetricSet) o;
    return Objects.equals(this.weights, ratingMetricSet.weights) &&
        Objects.equals(this.values, ratingMetricSet.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weights, values);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingMetricSet {\n");
    sb.append("    weights: ").append(toIndentedString(weights)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
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

