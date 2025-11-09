package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.lootservice.model.LootEntryDefinition;
import com.necpgame.lootservice.model.LootModifier;
import com.necpgame.lootservice.model.LootTableUpsertRequestCurrencyRange;
import com.necpgame.lootservice.model.PityTimerConfig;
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
 * LootTableUpsertRequest
 */


public class LootTableUpsertRequest {

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

  private @Nullable Integer minItems;

  private @Nullable Integer maxItems;

  private @Nullable LootTableUpsertRequestCurrencyRange currencyRange;

  private @Nullable String rarityCurve;

  @Valid
  private List<@Valid LootModifier> modifiers = new ArrayList<>();

  private @Nullable PityTimerConfig pityConfig;

  @Valid
  private List<@Valid LootEntryDefinition> entries = new ArrayList<>();

  public LootTableUpsertRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootTableUpsertRequest(String tableId, String name, TableTypeEnum tableType, List<@Valid LootEntryDefinition> entries) {
    this.tableId = tableId;
    this.name = name;
    this.tableType = tableType;
    this.entries = entries;
  }

  public LootTableUpsertRequest tableId(String tableId) {
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

  public LootTableUpsertRequest name(String name) {
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

  public LootTableUpsertRequest tableType(TableTypeEnum tableType) {
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

  public LootTableUpsertRequest sourceId(@Nullable String sourceId) {
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

  public LootTableUpsertRequest minItems(@Nullable Integer minItems) {
    this.minItems = minItems;
    return this;
  }

  /**
   * Get minItems
   * @return minItems
   */
  
  @Schema(name = "minItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minItems")
  public @Nullable Integer getMinItems() {
    return minItems;
  }

  public void setMinItems(@Nullable Integer minItems) {
    this.minItems = minItems;
  }

  public LootTableUpsertRequest maxItems(@Nullable Integer maxItems) {
    this.maxItems = maxItems;
    return this;
  }

  /**
   * Get maxItems
   * @return maxItems
   */
  
  @Schema(name = "maxItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxItems")
  public @Nullable Integer getMaxItems() {
    return maxItems;
  }

  public void setMaxItems(@Nullable Integer maxItems) {
    this.maxItems = maxItems;
  }

  public LootTableUpsertRequest currencyRange(@Nullable LootTableUpsertRequestCurrencyRange currencyRange) {
    this.currencyRange = currencyRange;
    return this;
  }

  /**
   * Get currencyRange
   * @return currencyRange
   */
  @Valid 
  @Schema(name = "currencyRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currencyRange")
  public @Nullable LootTableUpsertRequestCurrencyRange getCurrencyRange() {
    return currencyRange;
  }

  public void setCurrencyRange(@Nullable LootTableUpsertRequestCurrencyRange currencyRange) {
    this.currencyRange = currencyRange;
  }

  public LootTableUpsertRequest rarityCurve(@Nullable String rarityCurve) {
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

  public LootTableUpsertRequest modifiers(List<@Valid LootModifier> modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  public LootTableUpsertRequest addModifiersItem(LootModifier modifiersItem) {
    if (this.modifiers == null) {
      this.modifiers = new ArrayList<>();
    }
    this.modifiers.add(modifiersItem);
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  @Valid 
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public List<@Valid LootModifier> getModifiers() {
    return modifiers;
  }

  public void setModifiers(List<@Valid LootModifier> modifiers) {
    this.modifiers = modifiers;
  }

  public LootTableUpsertRequest pityConfig(@Nullable PityTimerConfig pityConfig) {
    this.pityConfig = pityConfig;
    return this;
  }

  /**
   * Get pityConfig
   * @return pityConfig
   */
  @Valid 
  @Schema(name = "pityConfig", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pityConfig")
  public @Nullable PityTimerConfig getPityConfig() {
    return pityConfig;
  }

  public void setPityConfig(@Nullable PityTimerConfig pityConfig) {
    this.pityConfig = pityConfig;
  }

  public LootTableUpsertRequest entries(List<@Valid LootEntryDefinition> entries) {
    this.entries = entries;
    return this;
  }

  public LootTableUpsertRequest addEntriesItem(LootEntryDefinition entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

  /**
   * Get entries
   * @return entries
   */
  @NotNull @Valid 
  @Schema(name = "entries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entries")
  public List<@Valid LootEntryDefinition> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid LootEntryDefinition> entries) {
    this.entries = entries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootTableUpsertRequest lootTableUpsertRequest = (LootTableUpsertRequest) o;
    return Objects.equals(this.tableId, lootTableUpsertRequest.tableId) &&
        Objects.equals(this.name, lootTableUpsertRequest.name) &&
        Objects.equals(this.tableType, lootTableUpsertRequest.tableType) &&
        Objects.equals(this.sourceId, lootTableUpsertRequest.sourceId) &&
        Objects.equals(this.minItems, lootTableUpsertRequest.minItems) &&
        Objects.equals(this.maxItems, lootTableUpsertRequest.maxItems) &&
        Objects.equals(this.currencyRange, lootTableUpsertRequest.currencyRange) &&
        Objects.equals(this.rarityCurve, lootTableUpsertRequest.rarityCurve) &&
        Objects.equals(this.modifiers, lootTableUpsertRequest.modifiers) &&
        Objects.equals(this.pityConfig, lootTableUpsertRequest.pityConfig) &&
        Objects.equals(this.entries, lootTableUpsertRequest.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tableId, name, tableType, sourceId, minItems, maxItems, currencyRange, rarityCurve, modifiers, pityConfig, entries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootTableUpsertRequest {\n");
    sb.append("    tableId: ").append(toIndentedString(tableId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tableType: ").append(toIndentedString(tableType)).append("\n");
    sb.append("    sourceId: ").append(toIndentedString(sourceId)).append("\n");
    sb.append("    minItems: ").append(toIndentedString(minItems)).append("\n");
    sb.append("    maxItems: ").append(toIndentedString(maxItems)).append("\n");
    sb.append("    currencyRange: ").append(toIndentedString(currencyRange)).append("\n");
    sb.append("    rarityCurve: ").append(toIndentedString(rarityCurve)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
    sb.append("    pityConfig: ").append(toIndentedString(pityConfig)).append("\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
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

