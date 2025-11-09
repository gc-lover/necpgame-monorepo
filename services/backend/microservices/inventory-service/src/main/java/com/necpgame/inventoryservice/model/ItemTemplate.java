package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ItemTemplate
 */


public class ItemTemplate {

  private @Nullable String itemId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String rarity;

  private @Nullable BigDecimal weight;

  @Valid
  private Map<String, Object> baseStats = new HashMap<>();

  @Valid
  private List<String> allowedSlots = new ArrayList<>();

  public ItemTemplate itemId(@Nullable String itemId) {
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

  public ItemTemplate name(@Nullable String name) {
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

  public ItemTemplate description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public ItemTemplate rarity(@Nullable String rarity) {
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

  public ItemTemplate weight(@Nullable BigDecimal weight) {
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
  public @Nullable BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(@Nullable BigDecimal weight) {
    this.weight = weight;
  }

  public ItemTemplate baseStats(Map<String, Object> baseStats) {
    this.baseStats = baseStats;
    return this;
  }

  public ItemTemplate putBaseStatsItem(String key, Object baseStatsItem) {
    if (this.baseStats == null) {
      this.baseStats = new HashMap<>();
    }
    this.baseStats.put(key, baseStatsItem);
    return this;
  }

  /**
   * Get baseStats
   * @return baseStats
   */
  
  @Schema(name = "baseStats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("baseStats")
  public Map<String, Object> getBaseStats() {
    return baseStats;
  }

  public void setBaseStats(Map<String, Object> baseStats) {
    this.baseStats = baseStats;
  }

  public ItemTemplate allowedSlots(List<String> allowedSlots) {
    this.allowedSlots = allowedSlots;
    return this;
  }

  public ItemTemplate addAllowedSlotsItem(String allowedSlotsItem) {
    if (this.allowedSlots == null) {
      this.allowedSlots = new ArrayList<>();
    }
    this.allowedSlots.add(allowedSlotsItem);
    return this;
  }

  /**
   * Get allowedSlots
   * @return allowedSlots
   */
  
  @Schema(name = "allowedSlots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowedSlots")
  public List<String> getAllowedSlots() {
    return allowedSlots;
  }

  public void setAllowedSlots(List<String> allowedSlots) {
    this.allowedSlots = allowedSlots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemTemplate itemTemplate = (ItemTemplate) o;
    return Objects.equals(this.itemId, itemTemplate.itemId) &&
        Objects.equals(this.name, itemTemplate.name) &&
        Objects.equals(this.description, itemTemplate.description) &&
        Objects.equals(this.rarity, itemTemplate.rarity) &&
        Objects.equals(this.weight, itemTemplate.weight) &&
        Objects.equals(this.baseStats, itemTemplate.baseStats) &&
        Objects.equals(this.allowedSlots, itemTemplate.allowedSlots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, name, description, rarity, weight, baseStats, allowedSlots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemTemplate {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    baseStats: ").append(toIndentedString(baseStats)).append("\n");
    sb.append("    allowedSlots: ").append(toIndentedString(allowedSlots)).append("\n");
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

