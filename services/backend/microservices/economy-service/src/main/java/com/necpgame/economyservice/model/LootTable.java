package com.necpgame.economyservice.model;

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
 * LootTable
 */


public class LootTable {

  private @Nullable String tableId;

  private @Nullable String name;

  private @Nullable String sourceType;

  private @Nullable String tier;

  private @Nullable Integer itemsCount;

  public LootTable tableId(@Nullable String tableId) {
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

  public LootTable name(@Nullable String name) {
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

  public LootTable sourceType(@Nullable String sourceType) {
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

  public LootTable tier(@Nullable String tier) {
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

  public LootTable itemsCount(@Nullable Integer itemsCount) {
    this.itemsCount = itemsCount;
    return this;
  }

  /**
   * Get itemsCount
   * @return itemsCount
   */
  
  @Schema(name = "items_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items_count")
  public @Nullable Integer getItemsCount() {
    return itemsCount;
  }

  public void setItemsCount(@Nullable Integer itemsCount) {
    this.itemsCount = itemsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootTable lootTable = (LootTable) o;
    return Objects.equals(this.tableId, lootTable.tableId) &&
        Objects.equals(this.name, lootTable.name) &&
        Objects.equals(this.sourceType, lootTable.sourceType) &&
        Objects.equals(this.tier, lootTable.tier) &&
        Objects.equals(this.itemsCount, lootTable.itemsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tableId, name, sourceType, tier, itemsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootTable {\n");
    sb.append("    tableId: ").append(toIndentedString(tableId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    sourceType: ").append(toIndentedString(sourceType)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    itemsCount: ").append(toIndentedString(itemsCount)).append("\n");
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

