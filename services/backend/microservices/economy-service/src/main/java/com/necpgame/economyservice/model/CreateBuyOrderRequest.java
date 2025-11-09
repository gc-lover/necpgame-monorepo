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
 * CreateBuyOrderRequest
 */

@JsonTypeName("createBuyOrder_request")

public class CreateBuyOrderRequest {

  private String characterId;

  private String itemId;

  private BigDecimal maxPrice;

  private Integer quantity;

  private Integer expiresIn = 24;

  public CreateBuyOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateBuyOrderRequest(String characterId, String itemId, BigDecimal maxPrice, Integer quantity) {
    this.characterId = characterId;
    this.itemId = itemId;
    this.maxPrice = maxPrice;
    this.quantity = quantity;
  }

  public CreateBuyOrderRequest characterId(String characterId) {
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

  public CreateBuyOrderRequest itemId(String itemId) {
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

  public CreateBuyOrderRequest maxPrice(BigDecimal maxPrice) {
    this.maxPrice = maxPrice;
    return this;
  }

  /**
   * Get maxPrice
   * @return maxPrice
   */
  @NotNull @Valid 
  @Schema(name = "max_price", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("max_price")
  public BigDecimal getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(BigDecimal maxPrice) {
    this.maxPrice = maxPrice;
  }

  public CreateBuyOrderRequest quantity(Integer quantity) {
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

  public CreateBuyOrderRequest expiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
    return this;
  }

  /**
   * Время жизни ордера (часы)
   * @return expiresIn
   */
  
  @Schema(name = "expires_in", description = "Время жизни ордера (часы)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_in")
  public Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Integer expiresIn) {
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
    CreateBuyOrderRequest createBuyOrderRequest = (CreateBuyOrderRequest) o;
    return Objects.equals(this.characterId, createBuyOrderRequest.characterId) &&
        Objects.equals(this.itemId, createBuyOrderRequest.itemId) &&
        Objects.equals(this.maxPrice, createBuyOrderRequest.maxPrice) &&
        Objects.equals(this.quantity, createBuyOrderRequest.quantity) &&
        Objects.equals(this.expiresIn, createBuyOrderRequest.expiresIn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, itemId, maxPrice, quantity, expiresIn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateBuyOrderRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    maxPrice: ").append(toIndentedString(maxPrice)).append("\n");
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

