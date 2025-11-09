package com.necpgame.gameplayservice.model;

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
 * GameStartingItem
 */


public class GameStartingItem {

  private String itemId;

  private Integer quantity;

  public GameStartingItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameStartingItem(String itemId, Integer quantity) {
    this.itemId = itemId;
    this.quantity = quantity;
  }

  public GameStartingItem itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * ID предмета из базы данных
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", example = "item-pistol-liberty", description = "ID предмета из базы данных", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public GameStartingItem quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Количество предметов
   * minimum: 1
   * @return quantity
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "quantity", example = "1", description = "Количество предметов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
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
    GameStartingItem gameStartingItem = (GameStartingItem) o;
    return Objects.equals(this.itemId, gameStartingItem.itemId) &&
        Objects.equals(this.quantity, gameStartingItem.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameStartingItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
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

