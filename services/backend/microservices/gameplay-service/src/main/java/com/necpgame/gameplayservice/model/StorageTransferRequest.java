package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.StoredItem;
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
 * StorageTransferRequest
 */


public class StorageTransferRequest {

  /**
   * Gets or Sets direction
   */
  public enum DirectionEnum {
    TO_STORAGE("to_storage"),
    
    TO_INVENTORY("to_inventory");

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

  @Valid
  private List<@Valid StoredItem> items = new ArrayList<>();

  public StorageTransferRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StorageTransferRequest(DirectionEnum direction, List<@Valid StoredItem> items) {
    this.direction = direction;
    this.items = items;
  }

  public StorageTransferRequest direction(DirectionEnum direction) {
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

  public StorageTransferRequest items(List<@Valid StoredItem> items) {
    this.items = items;
    return this;
  }

  public StorageTransferRequest addItemsItem(StoredItem itemsItem) {
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
  public List<@Valid StoredItem> getItems() {
    return items;
  }

  public void setItems(List<@Valid StoredItem> items) {
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
    StorageTransferRequest storageTransferRequest = (StorageTransferRequest) o;
    return Objects.equals(this.direction, storageTransferRequest.direction) &&
        Objects.equals(this.items, storageTransferRequest.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(direction, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StorageTransferRequest {\n");
    sb.append("    direction: ").append(toIndentedString(direction)).append("\n");
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

