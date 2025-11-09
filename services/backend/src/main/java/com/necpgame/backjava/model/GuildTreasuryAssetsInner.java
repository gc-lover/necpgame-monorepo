package com.necpgame.backjava.model;

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
 * GuildTreasuryAssetsInner
 */

@JsonTypeName("GuildTreasury_assets_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildTreasuryAssetsInner {

  private @Nullable UUID itemId;

  private @Nullable Integer quantity;

  private @Nullable Integer estimatedValue;

  public GuildTreasuryAssetsInner itemId(@Nullable UUID itemId) {
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

  public GuildTreasuryAssetsInner quantity(@Nullable Integer quantity) {
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

  public GuildTreasuryAssetsInner estimatedValue(@Nullable Integer estimatedValue) {
    this.estimatedValue = estimatedValue;
    return this;
  }

  /**
   * Get estimatedValue
   * @return estimatedValue
   */
  
  @Schema(name = "estimated_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_value")
  public @Nullable Integer getEstimatedValue() {
    return estimatedValue;
  }

  public void setEstimatedValue(@Nullable Integer estimatedValue) {
    this.estimatedValue = estimatedValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildTreasuryAssetsInner guildTreasuryAssetsInner = (GuildTreasuryAssetsInner) o;
    return Objects.equals(this.itemId, guildTreasuryAssetsInner.itemId) &&
        Objects.equals(this.quantity, guildTreasuryAssetsInner.quantity) &&
        Objects.equals(this.estimatedValue, guildTreasuryAssetsInner.estimatedValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quantity, estimatedValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildTreasuryAssetsInner {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    estimatedValue: ").append(toIndentedString(estimatedValue)).append("\n");
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

