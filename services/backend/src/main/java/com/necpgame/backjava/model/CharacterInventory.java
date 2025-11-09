package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.InventoryItem;
import java.math.BigDecimal;
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
 * CharacterInventory
 */


public class CharacterInventory {

  private @Nullable String characterId;

  private @Nullable Integer slotsTotal;

  private @Nullable Integer slotsUsed;

  private @Nullable BigDecimal weightCurrent;

  private @Nullable BigDecimal weightMax;

  private @Nullable Boolean isOverencumbered;

  @Valid
  private List<@Valid InventoryItem> items = new ArrayList<>();

  public CharacterInventory characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public CharacterInventory slotsTotal(@Nullable Integer slotsTotal) {
    this.slotsTotal = slotsTotal;
    return this;
  }

  /**
   * Get slotsTotal
   * @return slotsTotal
   */
  
  @Schema(name = "slots_total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots_total")
  public @Nullable Integer getSlotsTotal() {
    return slotsTotal;
  }

  public void setSlotsTotal(@Nullable Integer slotsTotal) {
    this.slotsTotal = slotsTotal;
  }

  public CharacterInventory slotsUsed(@Nullable Integer slotsUsed) {
    this.slotsUsed = slotsUsed;
    return this;
  }

  /**
   * Get slotsUsed
   * @return slotsUsed
   */
  
  @Schema(name = "slots_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots_used")
  public @Nullable Integer getSlotsUsed() {
    return slotsUsed;
  }

  public void setSlotsUsed(@Nullable Integer slotsUsed) {
    this.slotsUsed = slotsUsed;
  }

  public CharacterInventory weightCurrent(@Nullable BigDecimal weightCurrent) {
    this.weightCurrent = weightCurrent;
    return this;
  }

  /**
   * Get weightCurrent
   * @return weightCurrent
   */
  @Valid 
  @Schema(name = "weight_current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight_current")
  public @Nullable BigDecimal getWeightCurrent() {
    return weightCurrent;
  }

  public void setWeightCurrent(@Nullable BigDecimal weightCurrent) {
    this.weightCurrent = weightCurrent;
  }

  public CharacterInventory weightMax(@Nullable BigDecimal weightMax) {
    this.weightMax = weightMax;
    return this;
  }

  /**
   * Get weightMax
   * @return weightMax
   */
  @Valid 
  @Schema(name = "weight_max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight_max")
  public @Nullable BigDecimal getWeightMax() {
    return weightMax;
  }

  public void setWeightMax(@Nullable BigDecimal weightMax) {
    this.weightMax = weightMax;
  }

  public CharacterInventory isOverencumbered(@Nullable Boolean isOverencumbered) {
    this.isOverencumbered = isOverencumbered;
    return this;
  }

  /**
   * Get isOverencumbered
   * @return isOverencumbered
   */
  
  @Schema(name = "is_overencumbered", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_overencumbered")
  public @Nullable Boolean getIsOverencumbered() {
    return isOverencumbered;
  }

  public void setIsOverencumbered(@Nullable Boolean isOverencumbered) {
    this.isOverencumbered = isOverencumbered;
  }

  public CharacterInventory items(List<@Valid InventoryItem> items) {
    this.items = items;
    return this;
  }

  public CharacterInventory addItemsItem(InventoryItem itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid InventoryItem> getItems() {
    return items;
  }

  public void setItems(List<@Valid InventoryItem> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterInventory characterInventory = (CharacterInventory) o;
    return Objects.equals(this.characterId, characterInventory.characterId) &&
        Objects.equals(this.slotsTotal, characterInventory.slotsTotal) &&
        Objects.equals(this.slotsUsed, characterInventory.slotsUsed) &&
        Objects.equals(this.weightCurrent, characterInventory.weightCurrent) &&
        Objects.equals(this.weightMax, characterInventory.weightMax) &&
        Objects.equals(this.isOverencumbered, characterInventory.isOverencumbered) &&
        Objects.equals(this.items, characterInventory.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, slotsTotal, slotsUsed, weightCurrent, weightMax, isOverencumbered, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterInventory {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    slotsTotal: ").append(toIndentedString(slotsTotal)).append("\n");
    sb.append("    slotsUsed: ").append(toIndentedString(slotsUsed)).append("\n");
    sb.append("    weightCurrent: ").append(toIndentedString(weightCurrent)).append("\n");
    sb.append("    weightMax: ").append(toIndentedString(weightMax)).append("\n");
    sb.append("    isOverencumbered: ").append(toIndentedString(isOverencumbered)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

