package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateSellOrderRequest
 */

@JsonTypeName("createSellOrder_request")

public class CreateSellOrderRequest {

  private String characterId;

  private String itemId;

  private BigDecimal minPrice;

  private Integer quantity;

  private @Nullable Integer expiresIn;

  public CreateSellOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateSellOrderRequest(String characterId, String itemId, BigDecimal minPrice, Integer quantity) {
    this.characterId = characterId;
    this.itemId = itemId;
    this.minPrice = minPrice;
    this.quantity = quantity;
  }

  public CreateSellOrderRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public CreateSellOrderRequest itemId(String itemId) {
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

  public CreateSellOrderRequest minPrice(BigDecimal minPrice) {
    this.minPrice = minPrice;
    return this;
  }

  /**
   * Get minPrice
   * @return minPrice
   */
  @NotNull @Valid 
  @Schema(name = "min_price", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("min_price")
  public BigDecimal getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(BigDecimal minPrice) {
    this.minPrice = minPrice;
  }

  public CreateSellOrderRequest quantity(Integer quantity) {
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

  public CreateSellOrderRequest expiresIn(@Nullable Integer expiresIn) {
    this.expiresIn = expiresIn;
    return this;
  }

  /**
   * Get expiresIn
   * @return expiresIn
   */
  
  @Schema(name = "expires_in", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_in")
  public @Nullable Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(@Nullable Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSellOrderRequest createSellOrderRequest = (CreateSellOrderRequest) o;
    return Objects.equals(this.characterId, createSellOrderRequest.characterId) &&
        Objects.equals(this.itemId, createSellOrderRequest.itemId) &&
        Objects.equals(this.minPrice, createSellOrderRequest.minPrice) &&
        Objects.equals(this.quantity, createSellOrderRequest.quantity) &&
        Objects.equals(this.expiresIn, createSellOrderRequest.expiresIn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, itemId, minPrice, quantity, expiresIn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateSellOrderRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    minPrice: ").append(toIndentedString(minPrice)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
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

