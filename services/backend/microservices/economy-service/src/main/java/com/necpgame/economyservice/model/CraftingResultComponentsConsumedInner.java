package com.necpgame.economyservice.model;

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
 * CraftingResultComponentsConsumedInner
 */

@JsonTypeName("CraftingResult_components_consumed_inner")

public class CraftingResultComponentsConsumedInner {

  private @Nullable UUID componentId;

  private @Nullable Integer quantity;

  public CraftingResultComponentsConsumedInner componentId(@Nullable UUID componentId) {
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

  public CraftingResultComponentsConsumedInner quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingResultComponentsConsumedInner craftingResultComponentsConsumedInner = (CraftingResultComponentsConsumedInner) o;
    return Objects.equals(this.componentId, craftingResultComponentsConsumedInner.componentId) &&
        Objects.equals(this.quantity, craftingResultComponentsConsumedInner.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentId, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingResultComponentsConsumedInner {\n");
    sb.append("    componentId: ").append(toIndentedString(componentId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
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

