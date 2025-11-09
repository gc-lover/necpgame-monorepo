package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.inventoryservice.model.ItemTransfer;
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
 * StashTransferRequest
 */


public class StashTransferRequest {

  @Valid
  private List<@Valid ItemTransfer> items = new ArrayList<>();

  /**
   * Gets or Sets direction
   */
  public enum DirectionEnum {
    TO_STASH("TO_STASH"),
    
    TO_BACKPACK("TO_BACKPACK");

    private final String value;

    DirectionEnum(String value) {
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
    public static DirectionEnum fromValue(String value) {
      for (DirectionEnum b : DirectionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DirectionEnum direction;

  private @Nullable String idempotencyKey;

  public StashTransferRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StashTransferRequest(List<@Valid ItemTransfer> items, DirectionEnum direction) {
    this.items = items;
    this.direction = direction;
  }

  public StashTransferRequest items(List<@Valid ItemTransfer> items) {
    this.items = items;
    return this;
  }

  public StashTransferRequest addItemsItem(ItemTransfer itemsItem) {
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
  @NotNull @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid ItemTransfer> getItems() {
    return items;
  }

  public void setItems(List<@Valid ItemTransfer> items) {
    this.items = items;
  }

  public StashTransferRequest direction(DirectionEnum direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Get direction
   * @return direction
   */
  @NotNull 
  @Schema(name = "direction", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("direction")
  public DirectionEnum getDirection() {
    return direction;
  }

  public void setDirection(DirectionEnum direction) {
    this.direction = direction;
  }

  public StashTransferRequest idempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
    return this;
  }

  /**
   * Get idempotencyKey
   * @return idempotencyKey
   */
  
  @Schema(name = "idempotencyKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idempotencyKey")
  public @Nullable String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StashTransferRequest stashTransferRequest = (StashTransferRequest) o;
    return Objects.equals(this.items, stashTransferRequest.items) &&
        Objects.equals(this.direction, stashTransferRequest.direction) &&
        Objects.equals(this.idempotencyKey, stashTransferRequest.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, direction, idempotencyKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StashTransferRequest {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    direction: ").append(toIndentedString(direction)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
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

