package com.necpgame.gameplayservice.model;

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
 * EnemyType
 */


public class EnemyType {

  private @Nullable String typeId;

  private @Nullable String name;

  private @Nullable String faction;

  private @Nullable Integer tier;

  private @Nullable Object stats;

  @Valid
  private List<String> tactics = new ArrayList<>();

  private @Nullable String lootTable;

  public EnemyType typeId(@Nullable String typeId) {
    this.typeId = typeId;
    return this;
  }

  /**
   * Get typeId
   * @return typeId
   */
  
  @Schema(name = "type_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type_id")
  public @Nullable String getTypeId() {
    return typeId;
  }

  public void setTypeId(@Nullable String typeId) {
    this.typeId = typeId;
  }

  public EnemyType name(@Nullable String name) {
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

  public EnemyType faction(@Nullable String faction) {
    this.faction = faction;
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public @Nullable String getFaction() {
    return faction;
  }

  public void setFaction(@Nullable String faction) {
    this.faction = faction;
  }

  public EnemyType tier(@Nullable Integer tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable Integer getTier() {
    return tier;
  }

  public void setTier(@Nullable Integer tier) {
    this.tier = tier;
  }

  public EnemyType stats(@Nullable Object stats) {
    this.stats = stats;
    return this;
  }

  /**
   * Get stats
   * @return stats
   */
  
  @Schema(name = "stats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stats")
  public @Nullable Object getStats() {
    return stats;
  }

  public void setStats(@Nullable Object stats) {
    this.stats = stats;
  }

  public EnemyType tactics(List<String> tactics) {
    this.tactics = tactics;
    return this;
  }

  public EnemyType addTacticsItem(String tacticsItem) {
    if (this.tactics == null) {
      this.tactics = new ArrayList<>();
    }
    this.tactics.add(tacticsItem);
    return this;
  }

  /**
   * Get tactics
   * @return tactics
   */
  
  @Schema(name = "tactics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tactics")
  public List<String> getTactics() {
    return tactics;
  }

  public void setTactics(List<String> tactics) {
    this.tactics = tactics;
  }

  public EnemyType lootTable(@Nullable String lootTable) {
    this.lootTable = lootTable;
    return this;
  }

  /**
   * Get lootTable
   * @return lootTable
   */
  
  @Schema(name = "loot_table", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loot_table")
  public @Nullable String getLootTable() {
    return lootTable;
  }

  public void setLootTable(@Nullable String lootTable) {
    this.lootTable = lootTable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnemyType enemyType = (EnemyType) o;
    return Objects.equals(this.typeId, enemyType.typeId) &&
        Objects.equals(this.name, enemyType.name) &&
        Objects.equals(this.faction, enemyType.faction) &&
        Objects.equals(this.tier, enemyType.tier) &&
        Objects.equals(this.stats, enemyType.stats) &&
        Objects.equals(this.tactics, enemyType.tactics) &&
        Objects.equals(this.lootTable, enemyType.lootTable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeId, name, faction, tier, stats, tactics, lootTable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnemyType {\n");
    sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
    sb.append("    tactics: ").append(toIndentedString(tactics)).append("\n");
    sb.append("    lootTable: ").append(toIndentedString(lootTable)).append("\n");
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

