package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ItemDropRequest
 */


public class ItemDropRequest {

  private String itemInstanceId;

  private Integer quantity;

  @Valid
  private Map<String, Object> worldPosition = new HashMap<>();

  public ItemDropRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ItemDropRequest(String itemInstanceId, Integer quantity) {
    this.itemInstanceId = itemInstanceId;
    this.quantity = quantity;
  }

  public ItemDropRequest itemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
    return this;
  }

  /**
   * Get itemInstanceId
   * @return itemInstanceId
   */
  @NotNull 
  @Schema(name = "itemInstanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemInstanceId")
  public String getItemInstanceId() {
    return itemInstanceId;
  }

  public void setItemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
  }

  public ItemDropRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @NotNull 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public ItemDropRequest worldPosition(Map<String, Object> worldPosition) {
    this.worldPosition = worldPosition;
    return this;
  }

  public ItemDropRequest putWorldPositionItem(String key, Object worldPositionItem) {
    if (this.worldPosition == null) {
      this.worldPosition = new HashMap<>();
    }
    this.worldPosition.put(key, worldPositionItem);
    return this;
  }

  /**
   * Get worldPosition
   * @return worldPosition
   */
  
  @Schema(name = "worldPosition", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worldPosition")
  public Map<String, Object> getWorldPosition() {
    return worldPosition;
  }

  public void setWorldPosition(Map<String, Object> worldPosition) {
    this.worldPosition = worldPosition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemDropRequest itemDropRequest = (ItemDropRequest) o;
    return Objects.equals(this.itemInstanceId, itemDropRequest.itemInstanceId) &&
        Objects.equals(this.quantity, itemDropRequest.quantity) &&
        Objects.equals(this.worldPosition, itemDropRequest.worldPosition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemInstanceId, quantity, worldPosition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemDropRequest {\n");
    sb.append("    itemInstanceId: ").append(toIndentedString(itemInstanceId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    worldPosition: ").append(toIndentedString(worldPosition)).append("\n");
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

