package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CraftingCalculationMissingComponentsInner
 */

@JsonTypeName("CraftingCalculation_missing_components_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CraftingCalculationMissingComponentsInner {

  private @Nullable UUID componentId;

  private @Nullable Integer required;

  private @Nullable Integer available;

  public CraftingCalculationMissingComponentsInner componentId(@Nullable UUID componentId) {
    this.componentId = componentId;
    return this;
  }

  /**
   * Get componentId
   * @return componentId
   */
  @Valid 
  @Schema(name = "component_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("component_id")
  public @Nullable UUID getComponentId() {
    return componentId;
  }

  public void setComponentId(@Nullable UUID componentId) {
    this.componentId = componentId;
  }

  public CraftingCalculationMissingComponentsInner required(@Nullable Integer required) {
    this.required = required;
    return this;
  }

  /**
   * Get required
   * @return required
   */
  
  @Schema(name = "required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required")
  public @Nullable Integer getRequired() {
    return required;
  }

  public void setRequired(@Nullable Integer required) {
    this.required = required;
  }

  public CraftingCalculationMissingComponentsInner available(@Nullable Integer available) {
    this.available = available;
    return this;
  }

  /**
   * Get available
   * @return available
   */
  
  @Schema(name = "available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available")
  public @Nullable Integer getAvailable() {
    return available;
  }

  public void setAvailable(@Nullable Integer available) {
    this.available = available;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingCalculationMissingComponentsInner craftingCalculationMissingComponentsInner = (CraftingCalculationMissingComponentsInner) o;
    return Objects.equals(this.componentId, craftingCalculationMissingComponentsInner.componentId) &&
        Objects.equals(this.required, craftingCalculationMissingComponentsInner.required) &&
        Objects.equals(this.available, craftingCalculationMissingComponentsInner.available);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentId, required, available);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingCalculationMissingComponentsInner {\n");
    sb.append("    componentId: ").append(toIndentedString(componentId)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
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

