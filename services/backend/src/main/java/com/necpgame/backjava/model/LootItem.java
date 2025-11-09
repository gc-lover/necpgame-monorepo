package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootItem
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootItem {

  private @Nullable String itemId;

  private @Nullable String rarity;

  private @Nullable Integer quantity;

  @Valid
  private List<String> smartLootTags = new ArrayList<>();

  private @Nullable Boolean guaranteed;

  public LootItem itemId(@Nullable String itemId) {
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

  public LootItem rarity(@Nullable String rarity) {
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

  public LootItem quantity(@Nullable Integer quantity) {
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

  public LootItem smartLootTags(List<String> smartLootTags) {
    this.smartLootTags = smartLootTags;
    return this;
  }

  public LootItem addSmartLootTagsItem(String smartLootTagsItem) {
    if (this.smartLootTags == null) {
      this.smartLootTags = new ArrayList<>();
    }
    this.smartLootTags.add(smartLootTagsItem);
    return this;
  }

  /**
   * Get smartLootTags
   * @return smartLootTags
   */
  
  @Schema(name = "smartLootTags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("smartLootTags")
  public List<String> getSmartLootTags() {
    return smartLootTags;
  }

  public void setSmartLootTags(List<String> smartLootTags) {
    this.smartLootTags = smartLootTags;
  }

  public LootItem guaranteed(@Nullable Boolean guaranteed) {
    this.guaranteed = guaranteed;
    return this;
  }

  /**
   * Get guaranteed
   * @return guaranteed
   */
  
  @Schema(name = "guaranteed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guaranteed")
  public @Nullable Boolean getGuaranteed() {
    return guaranteed;
  }

  public void setGuaranteed(@Nullable Boolean guaranteed) {
    this.guaranteed = guaranteed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootItem lootItem = (LootItem) o;
    return Objects.equals(this.itemId, lootItem.itemId) &&
        Objects.equals(this.rarity, lootItem.rarity) &&
        Objects.equals(this.quantity, lootItem.quantity) &&
        Objects.equals(this.smartLootTags, lootItem.smartLootTags) &&
        Objects.equals(this.guaranteed, lootItem.guaranteed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, rarity, quantity, smartLootTags, guaranteed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    smartLootTags: ").append(toIndentedString(smartLootTags)).append("\n");
    sb.append("    guaranteed: ").append(toIndentedString(guaranteed)).append("\n");
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

