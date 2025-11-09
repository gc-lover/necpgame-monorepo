package com.necpgame.backjava.model;

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
 * Attribute
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Attribute {

  private @Nullable Integer value;

  private @Nullable Integer baseValue;

  private @Nullable Integer modifier;

  private @Nullable Integer maxValue;

  public Attribute value(@Nullable Integer value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "value", example = "12", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable Integer getValue() {
    return value;
  }

  public void setValue(@Nullable Integer value) {
    this.value = value;
  }

  public Attribute baseValue(@Nullable Integer baseValue) {
    this.baseValue = baseValue;
    return this;
  }

  /**
   * Get baseValue
   * @return baseValue
   */
  
  @Schema(name = "base_value", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_value")
  public @Nullable Integer getBaseValue() {
    return baseValue;
  }

  public void setBaseValue(@Nullable Integer baseValue) {
    this.baseValue = baseValue;
  }

  public Attribute modifier(@Nullable Integer modifier) {
    this.modifier = modifier;
    return this;
  }

  /**
   * Модификатор от экипировки/баффов
   * @return modifier
   */
  
  @Schema(name = "modifier", example = "2", description = "Модификатор от экипировки/баффов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifier")
  public @Nullable Integer getModifier() {
    return modifier;
  }

  public void setModifier(@Nullable Integer modifier) {
    this.modifier = modifier;
  }

  public Attribute maxValue(@Nullable Integer maxValue) {
    this.maxValue = maxValue;
    return this;
  }

  /**
   * Get maxValue
   * @return maxValue
   */
  
  @Schema(name = "max_value", example = "20", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_value")
  public @Nullable Integer getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(@Nullable Integer maxValue) {
    this.maxValue = maxValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Attribute attribute = (Attribute) o;
    return Objects.equals(this.value, attribute.value) &&
        Objects.equals(this.baseValue, attribute.baseValue) &&
        Objects.equals(this.modifier, attribute.modifier) &&
        Objects.equals(this.maxValue, attribute.maxValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, baseValue, modifier, maxValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Attribute {\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    baseValue: ").append(toIndentedString(baseValue)).append("\n");
    sb.append("    modifier: ").append(toIndentedString(modifier)).append("\n");
    sb.append("    maxValue: ").append(toIndentedString(maxValue)).append("\n");
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

