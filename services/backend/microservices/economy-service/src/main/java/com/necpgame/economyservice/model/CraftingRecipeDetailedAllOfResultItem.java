package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.CraftingRecipeDetailedAllOfResultItemQualityRange;
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
 * CraftingRecipeDetailedAllOfResultItem
 */

@JsonTypeName("CraftingRecipeDetailed_allOf_result_item")

public class CraftingRecipeDetailedAllOfResultItem {

  private @Nullable UUID itemId;

  private @Nullable String name;

  private @Nullable CraftingRecipeDetailedAllOfResultItemQualityRange qualityRange;

  public CraftingRecipeDetailedAllOfResultItem itemId(@Nullable UUID itemId) {
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

  public CraftingRecipeDetailedAllOfResultItem name(@Nullable String name) {
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

  public CraftingRecipeDetailedAllOfResultItem qualityRange(@Nullable CraftingRecipeDetailedAllOfResultItemQualityRange qualityRange) {
    this.qualityRange = qualityRange;
    return this;
  }

  /**
   * Get qualityRange
   * @return qualityRange
   */
  @Valid 
  @Schema(name = "quality_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality_range")
  public @Nullable CraftingRecipeDetailedAllOfResultItemQualityRange getQualityRange() {
    return qualityRange;
  }

  public void setQualityRange(@Nullable CraftingRecipeDetailedAllOfResultItemQualityRange qualityRange) {
    this.qualityRange = qualityRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingRecipeDetailedAllOfResultItem craftingRecipeDetailedAllOfResultItem = (CraftingRecipeDetailedAllOfResultItem) o;
    return Objects.equals(this.itemId, craftingRecipeDetailedAllOfResultItem.itemId) &&
        Objects.equals(this.name, craftingRecipeDetailedAllOfResultItem.name) &&
        Objects.equals(this.qualityRange, craftingRecipeDetailedAllOfResultItem.qualityRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, name, qualityRange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingRecipeDetailedAllOfResultItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    qualityRange: ").append(toIndentedString(qualityRange)).append("\n");
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

