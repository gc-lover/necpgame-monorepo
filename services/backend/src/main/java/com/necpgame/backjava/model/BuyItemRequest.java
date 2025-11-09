package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * BuyItemRequest
 */

@JsonTypeName("buyItem_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:08.934689200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class BuyItemRequest {

  private UUID characterId;

  private String vendorId;

  private String itemId;

  private Integer quantity;

  public BuyItemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BuyItemRequest(UUID characterId, String vendorId, String itemId, Integer quantity) {
    this.characterId = characterId;
    this.vendorId = vendorId;
    this.itemId = itemId;
    this.quantity = quantity;
  }

  public BuyItemRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
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
  @Schema(name = "vendorId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("vendorId")
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
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
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
        Objects.equals(this.quantity, buyItemRequest.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, vendorId, itemId, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyItemRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    vendorId: ").append(toIndentedString(vendorId)).append("\n");
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

