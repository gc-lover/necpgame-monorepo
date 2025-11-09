package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.QuestLootTableRandomLootInnerQuantityRange;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestLootTableRandomLootInner
 */

@JsonTypeName("QuestLootTable_random_loot_inner")

public class QuestLootTableRandomLootInner {

  private @Nullable UUID itemId;

  private @Nullable Float dropChance;

  private @Nullable QuestLootTableRandomLootInnerQuantityRange quantityRange;

  public QuestLootTableRandomLootInner itemId(@Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @Valid 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable UUID getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable UUID itemId) {
    this.itemId = itemId;
  }

  public QuestLootTableRandomLootInner dropChance(@Nullable Float dropChance) {
    this.dropChance = dropChance;
    return this;
  }

  /**
   * Get dropChance
   * @return dropChance
   */
  
  @Schema(name = "drop_chance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("drop_chance")
  public @Nullable Float getDropChance() {
    return dropChance;
  }

  public void setDropChance(@Nullable Float dropChance) {
    this.dropChance = dropChance;
  }

  public QuestLootTableRandomLootInner quantityRange(@Nullable QuestLootTableRandomLootInnerQuantityRange quantityRange) {
    this.quantityRange = quantityRange;
    return this;
  }

  /**
   * Get quantityRange
   * @return quantityRange
   */
  @Valid 
  @Schema(name = "quantity_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity_range")
  public @Nullable QuestLootTableRandomLootInnerQuantityRange getQuantityRange() {
    return quantityRange;
  }

  public void setQuantityRange(@Nullable QuestLootTableRandomLootInnerQuantityRange quantityRange) {
    this.quantityRange = quantityRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestLootTableRandomLootInner questLootTableRandomLootInner = (QuestLootTableRandomLootInner) o;
    return Objects.equals(this.itemId, questLootTableRandomLootInner.itemId) &&
        Objects.equals(this.dropChance, questLootTableRandomLootInner.dropChance) &&
        Objects.equals(this.quantityRange, questLootTableRandomLootInner.quantityRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, dropChance, quantityRange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestLootTableRandomLootInner {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    dropChance: ").append(toIndentedString(dropChance)).append("\n");
    sb.append("    quantityRange: ").append(toIndentedString(quantityRange)).append("\n");
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

