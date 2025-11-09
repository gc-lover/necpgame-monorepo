package com.necpgame.inventoryservice.model;

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
 * ItemSplitRequest
 */


public class ItemSplitRequest {

  private String itemInstanceId;

  private Integer quantity;

  private @Nullable String targetContainer;

  private @Nullable Integer targetSlotIndex;

  public ItemSplitRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ItemSplitRequest(String itemInstanceId, Integer quantity) {
    this.itemInstanceId = itemInstanceId;
    this.quantity = quantity;
  }

  public ItemSplitRequest itemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
    return this;
  }

  /**
   * Get itemInstanceId
   * @return itemInstanceId
   */
  @NotNull 
  @Schema(name = "itemInstanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemInstanceId")
  public String getItemInstanceId() {
    return itemInstanceId;
  }

  public void setItemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
  }

  public ItemSplitRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @NotNull 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public ItemSplitRequest targetContainer(@Nullable String targetContainer) {
    this.targetContainer = targetContainer;
    return this;
  }

  /**
   * Get targetContainer
   * @return targetContainer
   */
  
  @Schema(name = "targetContainer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetContainer")
  public @Nullable String getTargetContainer() {
    return targetContainer;
  }

  public void setTargetContainer(@Nullable String targetContainer) {
    this.targetContainer = targetContainer;
  }

  public ItemSplitRequest targetSlotIndex(@Nullable Integer targetSlotIndex) {
    this.targetSlotIndex = targetSlotIndex;
    return this;
  }

  /**
   * Get targetSlotIndex
   * @return targetSlotIndex
   */
  
  @Schema(name = "targetSlotIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetSlotIndex")
  public @Nullable Integer getTargetSlotIndex() {
    return targetSlotIndex;
  }

  public void setTargetSlotIndex(@Nullable Integer targetSlotIndex) {
    this.targetSlotIndex = targetSlotIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemSplitRequest itemSplitRequest = (ItemSplitRequest) o;
    return Objects.equals(this.itemInstanceId, itemSplitRequest.itemInstanceId) &&
        Objects.equals(this.quantity, itemSplitRequest.quantity) &&
        Objects.equals(this.targetContainer, itemSplitRequest.targetContainer) &&
        Objects.equals(this.targetSlotIndex, itemSplitRequest.targetSlotIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemInstanceId, quantity, targetContainer, targetSlotIndex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemSplitRequest {\n");
    sb.append("    itemInstanceId: ").append(toIndentedString(itemInstanceId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    targetContainer: ").append(toIndentedString(targetContainer)).append("\n");
    sb.append("    targetSlotIndex: ").append(toIndentedString(targetSlotIndex)).append("\n");
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

