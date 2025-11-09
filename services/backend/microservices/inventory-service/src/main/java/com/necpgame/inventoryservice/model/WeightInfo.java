package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WeightInfo
 */


public class WeightInfo {

  private @Nullable BigDecimal current;

  private @Nullable BigDecimal capacity;

  private @Nullable BigDecimal encumbrancePercent;

  private @Nullable String penalty;

  @Valid
  private List<String> modifiers = new ArrayList<>();

  public WeightInfo current(@Nullable BigDecimal current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  @Valid 
  @Schema(name = "current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current")
  public @Nullable BigDecimal getCurrent() {
    return current;
  }

  public void setCurrent(@Nullable BigDecimal current) {
    this.current = current;
  }

  public WeightInfo capacity(@Nullable BigDecimal capacity) {
    this.capacity = capacity;
    return this;
  }

  /**
   * Get capacity
   * @return capacity
   */
  @Valid 
  @Schema(name = "capacity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacity")
  public @Nullable BigDecimal getCapacity() {
    return capacity;
  }

  public void setCapacity(@Nullable BigDecimal capacity) {
    this.capacity = capacity;
  }

  public WeightInfo encumbrancePercent(@Nullable BigDecimal encumbrancePercent) {
    this.encumbrancePercent = encumbrancePercent;
    return this;
  }

  /**
   * Get encumbrancePercent
   * @return encumbrancePercent
   */
  @Valid 
  @Schema(name = "encumbrancePercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("encumbrancePercent")
  public @Nullable BigDecimal getEncumbrancePercent() {
    return encumbrancePercent;
  }

  public void setEncumbrancePercent(@Nullable BigDecimal encumbrancePercent) {
    this.encumbrancePercent = encumbrancePercent;
  }

  public WeightInfo penalty(@Nullable String penalty) {
    this.penalty = penalty;
    return this;
  }

  /**
   * Get penalty
   * @return penalty
   */
  
  @Schema(name = "penalty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalty")
  public @Nullable String getPenalty() {
    return penalty;
  }

  public void setPenalty(@Nullable String penalty) {
    this.penalty = penalty;
  }

  public WeightInfo modifiers(List<String> modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  public WeightInfo addModifiersItem(String modifiersItem) {
    if (this.modifiers == null) {
      this.modifiers = new ArrayList<>();
    }
    this.modifiers.add(modifiersItem);
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public List<String> getModifiers() {
    return modifiers;
  }

  public void setModifiers(List<String> modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeightInfo weightInfo = (WeightInfo) o;
    return Objects.equals(this.current, weightInfo.current) &&
        Objects.equals(this.capacity, weightInfo.capacity) &&
        Objects.equals(this.encumbrancePercent, weightInfo.encumbrancePercent) &&
        Objects.equals(this.penalty, weightInfo.penalty) &&
        Objects.equals(this.modifiers, weightInfo.modifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(current, capacity, encumbrancePercent, penalty, modifiers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeightInfo {\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    encumbrancePercent: ").append(toIndentedString(encumbrancePercent)).append("\n");
    sb.append("    penalty: ").append(toIndentedString(penalty)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
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

