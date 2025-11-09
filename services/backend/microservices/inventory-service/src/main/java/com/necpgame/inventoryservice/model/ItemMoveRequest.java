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
 * ItemMoveRequest
 */


public class ItemMoveRequest {

  private String itemInstanceId;

  private @Nullable String sourceContainer;

  private String targetContainer;

  private @Nullable Integer targetSlotIndex;

  private @Nullable Boolean allowSwap;

  public ItemMoveRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ItemMoveRequest(String itemInstanceId, String targetContainer) {
    this.itemInstanceId = itemInstanceId;
    this.targetContainer = targetContainer;
  }

  public ItemMoveRequest itemInstanceId(String itemInstanceId) {
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

  public ItemMoveRequest sourceContainer(@Nullable String sourceContainer) {
    this.sourceContainer = sourceContainer;
    return this;
  }

  /**
   * Get sourceContainer
   * @return sourceContainer
   */
  
  @Schema(name = "sourceContainer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceContainer")
  public @Nullable String getSourceContainer() {
    return sourceContainer;
  }

  public void setSourceContainer(@Nullable String sourceContainer) {
    this.sourceContainer = sourceContainer;
  }

  public ItemMoveRequest targetContainer(String targetContainer) {
    this.targetContainer = targetContainer;
    return this;
  }

  /**
   * Get targetContainer
   * @return targetContainer
   */
  @NotNull 
  @Schema(name = "targetContainer", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetContainer")
  public String getTargetContainer() {
    return targetContainer;
  }

  public void setTargetContainer(String targetContainer) {
    this.targetContainer = targetContainer;
  }

  public ItemMoveRequest targetSlotIndex(@Nullable Integer targetSlotIndex) {
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

  public ItemMoveRequest allowSwap(@Nullable Boolean allowSwap) {
    this.allowSwap = allowSwap;
    return this;
  }

  /**
   * Get allowSwap
   * @return allowSwap
   */
  
  @Schema(name = "allowSwap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowSwap")
  public @Nullable Boolean getAllowSwap() {
    return allowSwap;
  }

  public void setAllowSwap(@Nullable Boolean allowSwap) {
    this.allowSwap = allowSwap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemMoveRequest itemMoveRequest = (ItemMoveRequest) o;
    return Objects.equals(this.itemInstanceId, itemMoveRequest.itemInstanceId) &&
        Objects.equals(this.sourceContainer, itemMoveRequest.sourceContainer) &&
        Objects.equals(this.targetContainer, itemMoveRequest.targetContainer) &&
        Objects.equals(this.targetSlotIndex, itemMoveRequest.targetSlotIndex) &&
        Objects.equals(this.allowSwap, itemMoveRequest.allowSwap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemInstanceId, sourceContainer, targetContainer, targetSlotIndex, allowSwap);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemMoveRequest {\n");
    sb.append("    itemInstanceId: ").append(toIndentedString(itemInstanceId)).append("\n");
    sb.append("    sourceContainer: ").append(toIndentedString(sourceContainer)).append("\n");
    sb.append("    targetContainer: ").append(toIndentedString(targetContainer)).append("\n");
    sb.append("    targetSlotIndex: ").append(toIndentedString(targetSlotIndex)).append("\n");
    sb.append("    allowSwap: ").append(toIndentedString(allowSwap)).append("\n");
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

