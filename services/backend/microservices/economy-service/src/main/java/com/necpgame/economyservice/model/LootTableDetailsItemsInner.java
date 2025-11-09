package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * LootTableDetailsItemsInner
 */

@JsonTypeName("LootTableDetails_items_inner")

public class LootTableDetailsItemsInner {

  private @Nullable String itemId;

  private @Nullable BigDecimal dropChance;

  private @Nullable Integer quantityMin;

  private @Nullable Integer quantityMax;

  private @Nullable String rarity;

  public LootTableDetailsItemsInner itemId(@Nullable String itemId) {
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

  public LootTableDetailsItemsInner dropChance(@Nullable BigDecimal dropChance) {
    this.dropChance = dropChance;
    return this;
  }

  /**
   * Шанс выпадения (%)
   * @return dropChance
   */
  @Valid 
  @Schema(name = "drop_chance", description = "Шанс выпадения (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("drop_chance")
  public @Nullable BigDecimal getDropChance() {
    return dropChance;
  }

  public void setDropChance(@Nullable BigDecimal dropChance) {
    this.dropChance = dropChance;
  }

  public LootTableDetailsItemsInner quantityMin(@Nullable Integer quantityMin) {
    this.quantityMin = quantityMin;
    return this;
  }

  /**
   * Get quantityMin
   * @return quantityMin
   */
  
  @Schema(name = "quantity_min", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity_min")
  public @Nullable Integer getQuantityMin() {
    return quantityMin;
  }

  public void setQuantityMin(@Nullable Integer quantityMin) {
    this.quantityMin = quantityMin;
  }

  public LootTableDetailsItemsInner quantityMax(@Nullable Integer quantityMax) {
    this.quantityMax = quantityMax;
    return this;
  }

  /**
   * Get quantityMax
   * @return quantityMax
   */
  
  @Schema(name = "quantity_max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity_max")
  public @Nullable Integer getQuantityMax() {
    return quantityMax;
  }

  public void setQuantityMax(@Nullable Integer quantityMax) {
    this.quantityMax = quantityMax;
  }

  public LootTableDetailsItemsInner rarity(@Nullable String rarity) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootTableDetailsItemsInner lootTableDetailsItemsInner = (LootTableDetailsItemsInner) o;
    return Objects.equals(this.itemId, lootTableDetailsItemsInner.itemId) &&
        Objects.equals(this.dropChance, lootTableDetailsItemsInner.dropChance) &&
        Objects.equals(this.quantityMin, lootTableDetailsItemsInner.quantityMin) &&
        Objects.equals(this.quantityMax, lootTableDetailsItemsInner.quantityMax) &&
        Objects.equals(this.rarity, lootTableDetailsItemsInner.rarity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, dropChance, quantityMin, quantityMax, rarity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootTableDetailsItemsInner {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    dropChance: ").append(toIndentedString(dropChance)).append("\n");
    sb.append("    quantityMin: ").append(toIndentedString(quantityMin)).append("\n");
    sb.append("    quantityMax: ").append(toIndentedString(quantityMax)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
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

