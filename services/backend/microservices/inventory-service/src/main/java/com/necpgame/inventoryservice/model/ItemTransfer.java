package com.necpgame.inventoryservice.model;

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
 * ItemTransfer
 */


public class ItemTransfer {

  private @Nullable String itemInstanceId;

  private @Nullable Integer quantity;

  public ItemTransfer itemInstanceId(@Nullable String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
    return this;
  }

  /**
   * Get itemInstanceId
   * @return itemInstanceId
   */
  
  @Schema(name = "itemInstanceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemInstanceId")
  public @Nullable String getItemInstanceId() {
    return itemInstanceId;
  }

  public void setItemInstanceId(@Nullable String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
  }

  public ItemTransfer quantity(@Nullable Integer quantity) {
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
    ItemTransfer itemTransfer = (ItemTransfer) o;
    return Objects.equals(this.itemInstanceId, itemTransfer.itemInstanceId) &&
        Objects.equals(this.quantity, itemTransfer.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemInstanceId, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemTransfer {\n");
    sb.append("    itemInstanceId: ").append(toIndentedString(itemInstanceId)).append("\n");
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

