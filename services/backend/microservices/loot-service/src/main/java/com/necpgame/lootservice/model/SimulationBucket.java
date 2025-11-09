package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SimulationBucket
 */


public class SimulationBucket {

  private String key;

  private Float probability;

  private @Nullable Float averageQuantity;

  public SimulationBucket() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SimulationBucket(String key, Float probability) {
    this.key = key;
    this.probability = probability;
  }

  public SimulationBucket key(String key) {
    this.key = key;
    return this;
  }

  /**
   * Get key
   * @return key
   */
  @NotNull 
  @Schema(name = "key", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("key")
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public SimulationBucket probability(Float probability) {
    this.probability = probability;
    return this;
  }

  /**
   * Get probability
   * @return probability
   */
  @NotNull 
  @Schema(name = "probability", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("probability")
  public Float getProbability() {
    return probability;
  }

  public void setProbability(Float probability) {
    this.probability = probability;
  }

  public SimulationBucket averageQuantity(@Nullable Float averageQuantity) {
    this.averageQuantity = averageQuantity;
    return this;
  }

  /**
   * Get averageQuantity
   * @return averageQuantity
   */
  
  @Schema(name = "averageQuantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageQuantity")
  public @Nullable Float getAverageQuantity() {
    return averageQuantity;
  }

  public void setAverageQuantity(@Nullable Float averageQuantity) {
    this.averageQuantity = averageQuantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimulationBucket simulationBucket = (SimulationBucket) o;
    return Objects.equals(this.key, simulationBucket.key) &&
        Objects.equals(this.probability, simulationBucket.probability) &&
        Objects.equals(this.averageQuantity, simulationBucket.averageQuantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, probability, averageQuantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimulationBucket {\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    probability: ").append(toIndentedString(probability)).append("\n");
    sb.append("    averageQuantity: ").append(toIndentedString(averageQuantity)).append("\n");
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

