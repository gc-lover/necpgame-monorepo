package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ResonanceDimension;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ResonanceSourceBreakdown
 */


public class ResonanceSourceBreakdown {

  private ResonanceDimension dimension;

  private @Nullable String title;

  private Float weight;

  private Float value;

  private Float delta24h;

  public ResonanceSourceBreakdown() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResonanceSourceBreakdown(ResonanceDimension dimension, Float weight, Float value, Float delta24h) {
    this.dimension = dimension;
    this.weight = weight;
    this.value = value;
    this.delta24h = delta24h;
  }

  public ResonanceSourceBreakdown dimension(ResonanceDimension dimension) {
    this.dimension = dimension;
    return this;
  }

  /**
   * Get dimension
   * @return dimension
   */
  @NotNull @Valid 
  @Schema(name = "dimension", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dimension")
  public ResonanceDimension getDimension() {
    return dimension;
  }

  public void setDimension(ResonanceDimension dimension) {
    this.dimension = dimension;
  }

  public ResonanceSourceBreakdown title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", example = "City Reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public ResonanceSourceBreakdown weight(Float weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * minimum: 0
   * maximum: 1
   * @return weight
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "weight", example = "0.4", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weight")
  public Float getWeight() {
    return weight;
  }

  public void setWeight(Float weight) {
    this.weight = weight;
  }

  public ResonanceSourceBreakdown value(Float value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * minimum: 0
   * maximum: 100
   * @return value
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "value", example = "25.5", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  public ResonanceSourceBreakdown delta24h(Float delta24h) {
    this.delta24h = delta24h;
    return this;
  }

  /**
   * Get delta24h
   * @return delta24h
   */
  @NotNull 
  @Schema(name = "delta24h", example = "4.2", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta24h")
  public Float getDelta24h() {
    return delta24h;
  }

  public void setDelta24h(Float delta24h) {
    this.delta24h = delta24h;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResonanceSourceBreakdown resonanceSourceBreakdown = (ResonanceSourceBreakdown) o;
    return Objects.equals(this.dimension, resonanceSourceBreakdown.dimension) &&
        Objects.equals(this.title, resonanceSourceBreakdown.title) &&
        Objects.equals(this.weight, resonanceSourceBreakdown.weight) &&
        Objects.equals(this.value, resonanceSourceBreakdown.value) &&
        Objects.equals(this.delta24h, resonanceSourceBreakdown.delta24h);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dimension, title, weight, value, delta24h);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResonanceSourceBreakdown {\n");
    sb.append("    dimension: ").append(toIndentedString(dimension)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    delta24h: ").append(toIndentedString(delta24h)).append("\n");
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

