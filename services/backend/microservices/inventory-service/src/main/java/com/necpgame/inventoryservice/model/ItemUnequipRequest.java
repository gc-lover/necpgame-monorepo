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
 * ItemUnequipRequest
 */


public class ItemUnequipRequest {

  private String slotType;

  private @Nullable String targetContainer;

  public ItemUnequipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ItemUnequipRequest(String slotType) {
    this.slotType = slotType;
  }

  public ItemUnequipRequest slotType(String slotType) {
    this.slotType = slotType;
    return this;
  }

  /**
   * Get slotType
   * @return slotType
   */
  @NotNull 
  @Schema(name = "slotType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slotType")
  public String getSlotType() {
    return slotType;
  }

  public void setSlotType(String slotType) {
    this.slotType = slotType;
  }

  public ItemUnequipRequest targetContainer(@Nullable String targetContainer) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemUnequipRequest itemUnequipRequest = (ItemUnequipRequest) o;
    return Objects.equals(this.slotType, itemUnequipRequest.slotType) &&
        Objects.equals(this.targetContainer, itemUnequipRequest.targetContainer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotType, targetContainer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemUnequipRequest {\n");
    sb.append("    slotType: ").append(toIndentedString(slotType)).append("\n");
    sb.append("    targetContainer: ").append(toIndentedString(targetContainer)).append("\n");
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

