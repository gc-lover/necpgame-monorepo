package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * VendorItem
 */


public class VendorItem {

  private @Nullable String itemId;

  private @Nullable String name;

  private @Nullable Integer quantity;

  private @Nullable BigDecimal basePrice;

  private @Nullable BigDecimal finalPrice;

  private @Nullable String currency;

  private @Nullable String rarity;

  public VendorItem itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public VendorItem name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public VendorItem quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public VendorItem basePrice(@Nullable BigDecimal basePrice) {
    this.basePrice = basePrice;
    return this;
  }

  /**
   * Get basePrice
   * @return basePrice
   */
  @Valid 
  @Schema(name = "base_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_price")
  public @Nullable BigDecimal getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(@Nullable BigDecimal basePrice) {
    this.basePrice = basePrice;
  }

  public VendorItem finalPrice(@Nullable BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
    return this;
  }

  /**
   * С учетом репутации и модификаторов
   * @return finalPrice
   */
  @Valid 
  @Schema(name = "final_price", description = "С учетом репутации и модификаторов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("final_price")
  public @Nullable BigDecimal getFinalPrice() {
    return finalPrice;
  }

  public void setFinalPrice(@Nullable BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public VendorItem currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  public VendorItem rarity(@Nullable String rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable String getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable String rarity) {
    this.rarity = rarity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VendorItem vendorItem = (VendorItem) o;
    return Objects.equals(this.itemId, vendorItem.itemId) &&
        Objects.equals(this.name, vendorItem.name) &&
        Objects.equals(this.quantity, vendorItem.quantity) &&
        Objects.equals(this.basePrice, vendorItem.basePrice) &&
        Objects.equals(this.finalPrice, vendorItem.finalPrice) &&
        Objects.equals(this.currency, vendorItem.currency) &&
        Objects.equals(this.rarity, vendorItem.rarity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, name, quantity, basePrice, finalPrice, currency, rarity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VendorItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    basePrice: ").append(toIndentedString(basePrice)).append("\n");
    sb.append("    finalPrice: ").append(toIndentedString(finalPrice)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
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

