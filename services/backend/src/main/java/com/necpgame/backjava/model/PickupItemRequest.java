package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PickupItemRequest
 */

@JsonTypeName("pickupItem_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:55:07.487632800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class PickupItemRequest {

  private String itemId;

  private Integer quantity = 1;

  private @Nullable String worldItemId;

  public PickupItemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PickupItemRequest(String itemId) {
    this.itemId = itemId;
  }

  public PickupItemRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("item_id")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public PickupItemRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public PickupItemRequest worldItemId(@Nullable String worldItemId) {
    this.worldItemId = worldItemId;
    return this;
  }

  /**
   * ID предмета в мире (для удаления)
   * @return worldItemId
   */
  
  @Schema(name = "world_item_id", description = "ID предмета в мире (для удаления)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("world_item_id")
  public @Nullable String getWorldItemId() {
    return worldItemId;
  }

  public void setWorldItemId(@Nullable String worldItemId) {
    this.worldItemId = worldItemId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PickupItemRequest pickupItemRequest = (PickupItemRequest) o;
    return Objects.equals(this.itemId, pickupItemRequest.itemId) &&
        Objects.equals(this.quantity, pickupItemRequest.quantity) &&
        Objects.equals(this.worldItemId, pickupItemRequest.worldItemId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quantity, worldItemId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PickupItemRequest {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    worldItemId: ").append(toIndentedString(worldItemId)).append("\n");
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
