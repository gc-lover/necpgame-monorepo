package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CraftingResultItemsCraftedInner
 */

@JsonTypeName("CraftingResult_items_crafted_inner")

public class CraftingResultItemsCraftedInner {

  private @Nullable UUID itemId;

  private @Nullable String quality;

  private @Nullable Integer quantity;

  public CraftingResultItemsCraftedInner itemId(@Nullable UUID itemId) {
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

  public CraftingResultItemsCraftedInner quality(@Nullable String quality) {
    this.quality = quality;
    return this;
  }

  /**
   * Get quality
   * @return quality
   */
  
  @Schema(name = "quality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality")
  public @Nullable String getQuality() {
    return quality;
  }

  public void setQuality(@Nullable String quality) {
    this.quality = quality;
  }

  public CraftingResultItemsCraftedInner quantity(@Nullable Integer quantity) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingResultItemsCraftedInner craftingResultItemsCraftedInner = (CraftingResultItemsCraftedInner) o;
    return Objects.equals(this.itemId, craftingResultItemsCraftedInner.itemId) &&
        Objects.equals(this.quality, craftingResultItemsCraftedInner.quality) &&
        Objects.equals(this.quantity, craftingResultItemsCraftedInner.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quality, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingResultItemsCraftedInner {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
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

