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
 * ProductionResultOutputsInner
 */

@JsonTypeName("ProductionResult_outputs_inner")

public class ProductionResultOutputsInner {

  private @Nullable UUID itemId;

  private @Nullable Integer quantity;

  private @Nullable String quality;

  public ProductionResultOutputsInner itemId(@Nullable UUID itemId) {
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

  public ProductionResultOutputsInner quantity(@Nullable Integer quantity) {
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

  public ProductionResultOutputsInner quality(@Nullable String quality) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductionResultOutputsInner productionResultOutputsInner = (ProductionResultOutputsInner) o;
    return Objects.equals(this.itemId, productionResultOutputsInner.itemId) &&
        Objects.equals(this.quantity, productionResultOutputsInner.quantity) &&
        Objects.equals(this.quality, productionResultOutputsInner.quality);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quantity, quality);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductionResultOutputsInner {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
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

