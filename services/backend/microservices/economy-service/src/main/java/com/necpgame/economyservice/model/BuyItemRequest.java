package com.necpgame.economyservice.model;

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
 * BuyItemRequest
 */

@JsonTypeName("buyItem_request")

public class BuyItemRequest {

  private String characterId;

  private String vendorId;

  private String itemId;

  private Integer quantity;

  private String currency = "eurodollar";

  public BuyItemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BuyItemRequest(String characterId, String vendorId, String itemId, Integer quantity) {
    this.characterId = characterId;
    this.vendorId = vendorId;
    this.itemId = itemId;
    this.quantity = quantity;
  }

  public BuyItemRequest characterId(String characterId) {
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

  public BuyItemRequest vendorId(String vendorId) {
    this.vendorId = vendorId;
    return this;
  }

  /**
   * Get vendorId
   * @return vendorId
   */
  @NotNull 
  @Schema(name = "vendor_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("vendor_id")
  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public BuyItemRequest itemId(String itemId) {
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

  public BuyItemRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * minimum: 1
   * @return quantity
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BuyItemRequest currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Валюта оплаты
   * @return currency
   */
  
  @Schema(name = "currency", description = "Валюта оплаты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuyItemRequest buyItemRequest = (BuyItemRequest) o;
    return Objects.equals(this.characterId, buyItemRequest.characterId) &&
        Objects.equals(this.vendorId, buyItemRequest.vendorId) &&
        Objects.equals(this.itemId, buyItemRequest.itemId) &&
        Objects.equals(this.quantity, buyItemRequest.quantity) &&
        Objects.equals(this.currency, buyItemRequest.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, vendorId, itemId, quantity, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyItemRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    vendorId: ").append(toIndentedString(vendorId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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

