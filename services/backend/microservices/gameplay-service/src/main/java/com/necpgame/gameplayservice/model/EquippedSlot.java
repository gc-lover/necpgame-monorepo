package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EquippedSlot
 */


public class EquippedSlot {

  private @Nullable String slotType;

  private @Nullable Integer slotIndex;

  private @Nullable String itemId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime equippedAt;

  public EquippedSlot slotType(@Nullable String slotType) {
    this.slotType = slotType;
    return this;
  }

  /**
   * Get slotType
   * @return slotType
   */
  
  @Schema(name = "slotType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slotType")
  public @Nullable String getSlotType() {
    return slotType;
  }

  public void setSlotType(@Nullable String slotType) {
    this.slotType = slotType;
  }

  public EquippedSlot slotIndex(@Nullable Integer slotIndex) {
    this.slotIndex = slotIndex;
    return this;
  }

  /**
   * Get slotIndex
   * @return slotIndex
   */
  
  @Schema(name = "slotIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slotIndex")
  public @Nullable Integer getSlotIndex() {
    return slotIndex;
  }

  public void setSlotIndex(@Nullable Integer slotIndex) {
    this.slotIndex = slotIndex;
  }

  public EquippedSlot itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public EquippedSlot equippedAt(@Nullable OffsetDateTime equippedAt) {
    this.equippedAt = equippedAt;
    return this;
  }

  /**
   * Get equippedAt
   * @return equippedAt
   */
  @Valid 
  @Schema(name = "equippedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("equippedAt")
  public @Nullable OffsetDateTime getEquippedAt() {
    return equippedAt;
  }

  public void setEquippedAt(@Nullable OffsetDateTime equippedAt) {
    this.equippedAt = equippedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EquippedSlot equippedSlot = (EquippedSlot) o;
    return Objects.equals(this.slotType, equippedSlot.slotType) &&
        Objects.equals(this.slotIndex, equippedSlot.slotIndex) &&
        Objects.equals(this.itemId, equippedSlot.itemId) &&
        Objects.equals(this.equippedAt, equippedSlot.equippedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotType, slotIndex, itemId, equippedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EquippedSlot {\n");
    sb.append("    slotType: ").append(toIndentedString(slotType)).append("\n");
    sb.append("    slotIndex: ").append(toIndentedString(slotIndex)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    equippedAt: ").append(toIndentedString(equippedAt)).append("\n");
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

