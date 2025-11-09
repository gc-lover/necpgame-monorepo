package com.necpgame.economyservice.model;

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
 * GetVendorPrices200ResponseItemsInner
 */

@JsonTypeName("getVendorPrices_200_response_items_inner")

public class GetVendorPrices200ResponseItemsInner {

  private @Nullable UUID itemId;

  private @Nullable Integer basePrice;

  private @Nullable Integer sellPrice;

  private @Nullable Integer buyPrice;

  public GetVendorPrices200ResponseItemsInner itemId(@Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @Valid 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable UUID getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable UUID itemId) {
    this.itemId = itemId;
  }

  public GetVendorPrices200ResponseItemsInner basePrice(@Nullable Integer basePrice) {
    this.basePrice = basePrice;
    return this;
  }

  /**
   * Get basePrice
   * @return basePrice
   */
  
  @Schema(name = "base_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_price")
  public @Nullable Integer getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(@Nullable Integer basePrice) {
    this.basePrice = basePrice;
  }

  public GetVendorPrices200ResponseItemsInner sellPrice(@Nullable Integer sellPrice) {
    this.sellPrice = sellPrice;
    return this;
  }

  /**
   * Get sellPrice
   * @return sellPrice
   */
  
  @Schema(name = "sell_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sell_price")
  public @Nullable Integer getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(@Nullable Integer sellPrice) {
    this.sellPrice = sellPrice;
  }

  public GetVendorPrices200ResponseItemsInner buyPrice(@Nullable Integer buyPrice) {
    this.buyPrice = buyPrice;
    return this;
  }

  /**
   * Get buyPrice
   * @return buyPrice
   */
  
  @Schema(name = "buy_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buy_price")
  public @Nullable Integer getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(@Nullable Integer buyPrice) {
    this.buyPrice = buyPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetVendorPrices200ResponseItemsInner getVendorPrices200ResponseItemsInner = (GetVendorPrices200ResponseItemsInner) o;
    return Objects.equals(this.itemId, getVendorPrices200ResponseItemsInner.itemId) &&
        Objects.equals(this.basePrice, getVendorPrices200ResponseItemsInner.basePrice) &&
        Objects.equals(this.sellPrice, getVendorPrices200ResponseItemsInner.sellPrice) &&
        Objects.equals(this.buyPrice, getVendorPrices200ResponseItemsInner.buyPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, basePrice, sellPrice, buyPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetVendorPrices200ResponseItemsInner {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    basePrice: ").append(toIndentedString(basePrice)).append("\n");
    sb.append("    sellPrice: ").append(toIndentedString(sellPrice)).append("\n");
    sb.append("    buyPrice: ").append(toIndentedString(buyPrice)).append("\n");
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

