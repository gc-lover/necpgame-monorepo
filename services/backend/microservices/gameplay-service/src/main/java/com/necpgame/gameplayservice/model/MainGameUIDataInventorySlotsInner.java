package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MainGameUIDataInventorySlotsInner
 */

@JsonTypeName("MainGameUIData_inventory_slots_inner")

public class MainGameUIDataInventorySlotsInner {

  private @Nullable String slotId;

  private @Nullable String itemName;

  private @Nullable String rarity;

  private @Nullable Integer quantity;

  public MainGameUIDataInventorySlotsInner slotId(@Nullable String slotId) {
    this.slotId = slotId;
    return this;
  }

  /**
   * Get slotId
   * @return slotId
   */
  
  @Schema(name = "slot_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slot_id")
  public @Nullable String getSlotId() {
    return slotId;
  }

  public void setSlotId(@Nullable String slotId) {
    this.slotId = slotId;
  }

  public MainGameUIDataInventorySlotsInner itemName(@Nullable String itemName) {
    this.itemName = itemName;
    return this;
  }

  /**
   * Get itemName
   * @return itemName
   */
  
  @Schema(name = "item_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_name")
  public @Nullable String getItemName() {
    return itemName;
  }

  public void setItemName(@Nullable String itemName) {
    this.itemName = itemName;
  }

  public MainGameUIDataInventorySlotsInner rarity(@Nullable String rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable String getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable String rarity) {
    this.rarity = rarity;
  }

  public MainGameUIDataInventorySlotsInner quantity(@Nullable Integer quantity) {
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
    MainGameUIDataInventorySlotsInner mainGameUIDataInventorySlotsInner = (MainGameUIDataInventorySlotsInner) o;
    return Objects.equals(this.slotId, mainGameUIDataInventorySlotsInner.slotId) &&
        Objects.equals(this.itemName, mainGameUIDataInventorySlotsInner.itemName) &&
        Objects.equals(this.rarity, mainGameUIDataInventorySlotsInner.rarity) &&
        Objects.equals(this.quantity, mainGameUIDataInventorySlotsInner.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotId, itemName, rarity, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MainGameUIDataInventorySlotsInner {\n");
    sb.append("    slotId: ").append(toIndentedString(slotId)).append("\n");
    sb.append("    itemName: ").append(toIndentedString(itemName)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
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

