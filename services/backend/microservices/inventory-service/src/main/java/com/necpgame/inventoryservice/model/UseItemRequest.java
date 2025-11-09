package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * UseItemRequest
 */


public class UseItemRequest {

  private UUID characterId;

  private String itemId;

  private Integer quantity = 1;

  public UseItemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UseItemRequest(UUID characterId, String itemId) {
    this.characterId = characterId;
    this.itemId = itemId;
  }

  public UseItemRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID персонажа
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", example = "550e8400-e29b-41d4-a716-446655440000", description = "ID персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public UseItemRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * ID предмета для использования
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", example = "item_health_pack", description = "ID предмета для использования", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public UseItemRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Количество предметов для использования
   * minimum: 1
   * @return quantity
   */
  @Min(value = 1) 
  @Schema(name = "quantity", example = "1", description = "Количество предметов для использования", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    UseItemRequest useItemRequest = (UseItemRequest) o;
    return Objects.equals(this.characterId, useItemRequest.characterId) &&
        Objects.equals(this.itemId, useItemRequest.itemId) &&
        Objects.equals(this.quantity, useItemRequest.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, itemId, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UseItemRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
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

