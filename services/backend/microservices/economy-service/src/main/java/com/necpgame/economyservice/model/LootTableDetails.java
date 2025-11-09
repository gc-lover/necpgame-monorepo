package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.LootTableDetailsItemsInner;
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
 * LootTableDetails
 */


public class LootTableDetails {

  private @Nullable String tableId;

  private @Nullable String name;

  private @Nullable String sourceType;

  private @Nullable String tier;

  @Valid
  private List<@Valid LootTableDetailsItemsInner> items = new ArrayList<>();

  private @Nullable Object modifiers;

  public LootTableDetails tableId(@Nullable String tableId) {
    this.tableId = tableId;
    return this;
  }

  /**
   * Get tableId
   * @return tableId
   */
  
  @Schema(name = "table_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("table_id")
  public @Nullable String getTableId() {
    return tableId;
  }

  public void setTableId(@Nullable String tableId) {
    this.tableId = tableId;
  }

  public LootTableDetails name(@Nullable String name) {
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

  public LootTableDetails sourceType(@Nullable String sourceType) {
    this.sourceType = sourceType;
    return this;
  }

  /**
   * Get sourceType
   * @return sourceType
   */
  
  @Schema(name = "source_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source_type")
  public @Nullable String getSourceType() {
    return sourceType;
  }

  public void setSourceType(@Nullable String sourceType) {
    this.sourceType = sourceType;
  }

  public LootTableDetails tier(@Nullable String tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable String getTier() {
    return tier;
  }

  public void setTier(@Nullable String tier) {
    this.tier = tier;
  }

  public LootTableDetails items(List<@Valid LootTableDetailsItemsInner> items) {
    this.items = items;
    return this;
  }

  public LootTableDetails addItemsItem(LootTableDetailsItemsInner itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Возможные предметы в таблице
   * @return items
   */
  @Valid 
  @Schema(name = "items", description = "Возможные предметы в таблице", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid LootTableDetailsItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid LootTableDetailsItemsInner> items) {
    this.items = items;
  }

  public LootTableDetails modifiers(@Nullable Object modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  /**
   * Формулы модификаторов
   * @return modifiers
   */
  
  @Schema(name = "modifiers", description = "Формулы модификаторов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public @Nullable Object getModifiers() {
    return modifiers;
  }

  public void setModifiers(@Nullable Object modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootTableDetails lootTableDetails = (LootTableDetails) o;
    return Objects.equals(this.tableId, lootTableDetails.tableId) &&
        Objects.equals(this.name, lootTableDetails.name) &&
        Objects.equals(this.sourceType, lootTableDetails.sourceType) &&
        Objects.equals(this.tier, lootTableDetails.tier) &&
        Objects.equals(this.items, lootTableDetails.items) &&
        Objects.equals(this.modifiers, lootTableDetails.modifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tableId, name, sourceType, tier, items, modifiers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootTableDetails {\n");
    sb.append("    tableId: ").append(toIndentedString(tableId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    sourceType: ").append(toIndentedString(sourceType)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
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

