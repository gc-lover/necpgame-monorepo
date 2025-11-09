package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * AdjustmentActionExpectedImpact
 */

@JsonTypeName("AdjustmentAction_expectedImpact")

public class AdjustmentActionExpectedImpact {

  private @Nullable String metricId;

  /**
   * Gets or Sets direction
   */
  public enum DirectionEnum {
    INCREASE("increase"),
    
    DECREASE("decrease"),
    
    STABILIZE("stabilize");

    private final String value;

    DirectionEnum(String value) {
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
    public static DirectionEnum fromValue(String value) {
      for (DirectionEnum b : DirectionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DirectionEnum direction;

  private @Nullable Float magnitude;

  public AdjustmentActionExpectedImpact metricId(@Nullable String metricId) {
    this.metricId = metricId;
    return this;
  }

  /**
   * Get metricId
   * @return metricId
   */
  
  @Schema(name = "metricId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metricId")
  public @Nullable String getMetricId() {
    return metricId;
  }

  public void setMetricId(@Nullable String metricId) {
    this.metricId = metricId;
  }

  public AdjustmentActionExpectedImpact direction(@Nullable DirectionEnum direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Get direction
   * @return direction
   */
  
  @Schema(name = "direction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("direction")
  public @Nullable DirectionEnum getDirection() {
    return direction;
  }

  public void setDirection(@Nullable DirectionEnum direction) {
    this.direction = direction;
  }

  public AdjustmentActionExpectedImpact magnitude(@Nullable Float magnitude) {
    this.magnitude = magnitude;
    return this;
  }

  /**
   * Get magnitude
   * @return magnitude
   */
  
  @Schema(name = "magnitude", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("magnitude")
  public @Nullable Float getMagnitude() {
    return magnitude;
  }

  public void setMagnitude(@Nullable Float magnitude) {
    this.magnitude = magnitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdjustmentActionExpectedImpact adjustmentActionExpectedImpact = (AdjustmentActionExpectedImpact) o;
    return Objects.equals(this.metricId, adjustmentActionExpectedImpact.metricId) &&
        Objects.equals(this.direction, adjustmentActionExpectedImpact.direction) &&
        Objects.equals(this.magnitude, adjustmentActionExpectedImpact.magnitude);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metricId, direction, magnitude);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdjustmentActionExpectedImpact {\n");
    sb.append("    metricId: ").append(toIndentedString(metricId)).append("\n");
    sb.append("    direction: ").append(toIndentedString(direction)).append("\n");
    sb.append("    magnitude: ").append(toIndentedString(magnitude)).append("\n");
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

