package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootTableSummary
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootTableSummary {

  private String tableId;

  private String name;

  /**
   * Gets or Sets tableType
   */
  public enum TableTypeEnum {
    NPC_LOOT("NPC_LOOT"),
    
    CONTAINER_LOOT("CONTAINER_LOOT"),
    
    BOSS_LOOT("BOSS_LOOT"),
    
    QUEST_REWARD("QUEST_REWARD"),
    
    EVENT_LOOT("EVENT_LOOT");

    private final String value;

    TableTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TableTypeEnum fromValue(String value) {
      for (TableTypeEnum b : TableTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TableTypeEnum tableType;

  private @Nullable String sourceId;

  private @Nullable String levelRange;

  private @Nullable String rarityCurve;

  private @Nullable Boolean active;

  private @Nullable String version;

  public LootTableSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootTableSummary(String tableId, String name, TableTypeEnum tableType) {
    this.tableId = tableId;
    this.name = name;
    this.tableType = tableType;
  }

  public LootTableSummary tableId(String tableId) {
    this.tableId = tableId;
    return this;
  }

  /**
   * Get tableId
   * @return tableId
   */
  @NotNull 
  @Schema(name = "tableId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tableId")
  public String getTableId() {
    return tableId;
  }

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  public LootTableSummary name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LootTableSummary tableType(TableTypeEnum tableType) {
    this.tableType = tableType;
    return this;
  }

  /**
   * Get tableType
   * @return tableType
   */
  @NotNull 
  @Schema(name = "tableType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tableType")
  public TableTypeEnum getTableType() {
    return tableType;
  }

  public void setTableType(TableTypeEnum tableType) {
    this.tableType = tableType;
  }

  public LootTableSummary sourceId(@Nullable String sourceId) {
    this.sourceId = sourceId;
    return this;
  }

  /**
   * Get sourceId
   * @return sourceId
   */
  
  @Schema(name = "sourceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceId")
  public @Nullable String getSourceId() {
    return sourceId;
  }

  public void setSourceId(@Nullable String sourceId) {
    this.sourceId = sourceId;
  }

  public LootTableSummary levelRange(@Nullable String levelRange) {
    this.levelRange = levelRange;
    return this;
  }

  /**
   * Get levelRange
   * @return levelRange
   */
  
  @Schema(name = "levelRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("levelRange")
  public @Nullable String getLevelRange() {
    return levelRange;
  }

  public void setLevelRange(@Nullable String levelRange) {
    this.levelRange = levelRange;
  }

  public LootTableSummary rarityCurve(@Nullable String rarityCurve) {
    this.rarityCurve = rarityCurve;
    return this;
  }

  /**
   * Get rarityCurve
   * @return rarityCurve
   */
  
  @Schema(name = "rarityCurve", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarityCurve")
  public @Nullable String getRarityCurve() {
    return rarityCurve;
  }

  public void setRarityCurve(@Nullable String rarityCurve) {
    this.rarityCurve = rarityCurve;
  }

  public LootTableSummary active(@Nullable Boolean active) {
    this.active = active;
    return this;
  }

  /**
   * Get active
   * @return active
   */
  
  @Schema(name = "active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active")
  public @Nullable Boolean getActive() {
    return active;
  }

  public void setActive(@Nullable Boolean active) {
    this.active = active;
  }

  public LootTableSummary version(@Nullable String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  
  @Schema(name = "version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable String getVersion() {
    return version;
  }

  public void setVersion(@Nullable String version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootTableSummary lootTableSummary = (LootTableSummary) o;
    return Objects.equals(this.tableId, lootTableSummary.tableId) &&
        Objects.equals(this.name, lootTableSummary.name) &&
        Objects.equals(this.tableType, lootTableSummary.tableType) &&
        Objects.equals(this.sourceId, lootTableSummary.sourceId) &&
        Objects.equals(this.levelRange, lootTableSummary.levelRange) &&
        Objects.equals(this.rarityCurve, lootTableSummary.rarityCurve) &&
        Objects.equals(this.active, lootTableSummary.active) &&
        Objects.equals(this.version, lootTableSummary.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tableId, name, tableType, sourceId, levelRange, rarityCurve, active, version);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootTableSummary {\n");
    sb.append("    tableId: ").append(toIndentedString(tableId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tableType: ").append(toIndentedString(tableType)).append("\n");
    sb.append("    sourceId: ").append(toIndentedString(sourceId)).append("\n");
    sb.append("    levelRange: ").append(toIndentedString(levelRange)).append("\n");
    sb.append("    rarityCurve: ").append(toIndentedString(rarityCurve)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

