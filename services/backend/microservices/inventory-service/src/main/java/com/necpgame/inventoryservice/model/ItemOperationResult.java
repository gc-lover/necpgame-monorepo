package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.inventoryservice.model.Item;
import com.necpgame.inventoryservice.model.WeightInfo;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ItemOperationResult
 */


public class ItemOperationResult {

  private @Nullable Item item;

  private @Nullable String containerId;

  private @Nullable String slotId;

  private @Nullable WeightInfo weight;

  public ItemOperationResult item(@Nullable Item item) {
    this.item = item;
    return this;
  }

  /**
   * Get item
   * @return item
   */
  @Valid 
  @Schema(name = "item", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item")
  public @Nullable Item getItem() {
    return item;
  }

  public void setItem(@Nullable Item item) {
    this.item = item;
  }

  public ItemOperationResult containerId(@Nullable String containerId) {
    this.containerId = containerId;
    return this;
  }

  /**
   * Get containerId
   * @return containerId
   */
  
  @Schema(name = "containerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("containerId")
  public @Nullable String getContainerId() {
    return containerId;
  }

  public void setContainerId(@Nullable String containerId) {
    this.containerId = containerId;
  }

  public ItemOperationResult slotId(@Nullable String slotId) {
    this.slotId = slotId;
    return this;
  }

  /**
   * Get slotId
   * @return slotId
   */
  
  @Schema(name = "slotId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slotId")
  public @Nullable String getSlotId() {
    return slotId;
  }

  public void setSlotId(@Nullable String slotId) {
    this.slotId = slotId;
  }

  public ItemOperationResult weight(@Nullable WeightInfo weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * @return weight
   */
  @Valid 
  @Schema(name = "weight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public @Nullable WeightInfo getWeight() {
    return weight;
  }

  public void setWeight(@Nullable WeightInfo weight) {
    this.weight = weight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemOperationResult itemOperationResult = (ItemOperationResult) o;
    return Objects.equals(this.item, itemOperationResult.item) &&
        Objects.equals(this.containerId, itemOperationResult.containerId) &&
        Objects.equals(this.slotId, itemOperationResult.slotId) &&
        Objects.equals(this.weight, itemOperationResult.weight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(item, containerId, slotId, weight);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemOperationResult {\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    containerId: ").append(toIndentedString(containerId)).append("\n");
    sb.append("    slotId: ").append(toIndentedString(slotId)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
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

