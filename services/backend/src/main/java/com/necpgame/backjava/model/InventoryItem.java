package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InventoryItem
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:55:07.487632800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class InventoryItem {

  private @Nullable String itemId;

  private @Nullable String name;

  private @Nullable Integer slot;

  private @Nullable Integer stackSize;

  private @Nullable BigDecimal durability;

  private @Nullable Boolean isEquipped;

  private @Nullable Boolean isBound;

  public InventoryItem itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public InventoryItem name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public InventoryItem slot(@Nullable Integer slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Get slot
   * @return slot
   */
  
  @Schema(name = "slot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slot")
  public @Nullable Integer getSlot() {
    return slot;
  }

  public void setSlot(@Nullable Integer slot) {
    this.slot = slot;
  }

  public InventoryItem stackSize(@Nullable Integer stackSize) {
    this.stackSize = stackSize;
    return this;
  }

  /**
   * Get stackSize
   * @return stackSize
   */
  
  @Schema(name = "stack_size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stack_size")
  public @Nullable Integer getStackSize() {
    return stackSize;
  }

  public void setStackSize(@Nullable Integer stackSize) {
    this.stackSize = stackSize;
  }

  public InventoryItem durability(@Nullable BigDecimal durability) {
    this.durability = durability;
    return this;
  }

  /**
   * Get durability
   * @return durability
   */
  @Valid 
  @Schema(name = "durability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durability")
  public @Nullable BigDecimal getDurability() {
    return durability;
  }

  public void setDurability(@Nullable BigDecimal durability) {
    this.durability = durability;
  }

  public InventoryItem isEquipped(@Nullable Boolean isEquipped) {
    this.isEquipped = isEquipped;
    return this;
  }

  /**
   * Get isEquipped
   * @return isEquipped
   */
  
  @Schema(name = "is_equipped", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_equipped")
  public @Nullable Boolean getIsEquipped() {
    return isEquipped;
  }

  public void setIsEquipped(@Nullable Boolean isEquipped) {
    this.isEquipped = isEquipped;
  }

  public InventoryItem isBound(@Nullable Boolean isBound) {
    this.isBound = isBound;
    return this;
  }

  /**
   * Get isBound
   * @return isBound
   */
  
  @Schema(name = "is_bound", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_bound")
  public @Nullable Boolean getIsBound() {
    return isBound;
  }

  public void setIsBound(@Nullable Boolean isBound) {
    this.isBound = isBound;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryItem inventoryItem = (InventoryItem) o;
    return Objects.equals(this.itemId, inventoryItem.itemId) &&
        Objects.equals(this.name, inventoryItem.name) &&
        Objects.equals(this.slot, inventoryItem.slot) &&
        Objects.equals(this.stackSize, inventoryItem.stackSize) &&
        Objects.equals(this.durability, inventoryItem.durability) &&
        Objects.equals(this.isEquipped, inventoryItem.isEquipped) &&
        Objects.equals(this.isBound, inventoryItem.isBound);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, name, slot, stackSize, durability, isEquipped, isBound);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventoryItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    slot: ").append(toIndentedString(slot)).append("\n");
    sb.append("    stackSize: ").append(toIndentedString(stackSize)).append("\n");
    sb.append("    durability: ").append(toIndentedString(durability)).append("\n");
    sb.append("    isEquipped: ").append(toIndentedString(isEquipped)).append("\n");
    sb.append("    isBound: ").append(toIndentedString(isBound)).append("\n");
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


