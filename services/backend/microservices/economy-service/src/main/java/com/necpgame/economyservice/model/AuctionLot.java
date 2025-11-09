package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AuctionLot
 */


public class AuctionLot {

  private @Nullable String lotId;

  private @Nullable String sellerId;

  private @Nullable String sellerName;

  private @Nullable String itemId;

  private @Nullable String itemName;

  private @Nullable Integer quantity;

  private @Nullable BigDecimal startingPrice;

  private @Nullable BigDecimal currentPrice;

  private @Nullable BigDecimal buyoutPrice;

  private @Nullable Integer bidsCount;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    SOLD("sold"),
    
    EXPIRED("expired"),
    
    CANCELLED("cancelled");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  public AuctionLot lotId(@Nullable String lotId) {
    this.lotId = lotId;
    return this;
  }

  /**
   * Get lotId
   * @return lotId
   */
  
  @Schema(name = "lot_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lot_id")
  public @Nullable String getLotId() {
    return lotId;
  }

  public void setLotId(@Nullable String lotId) {
    this.lotId = lotId;
  }

  public AuctionLot sellerId(@Nullable String sellerId) {
    this.sellerId = sellerId;
    return this;
  }

  /**
   * Get sellerId
   * @return sellerId
   */
  
  @Schema(name = "seller_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seller_id")
  public @Nullable String getSellerId() {
    return sellerId;
  }

  public void setSellerId(@Nullable String sellerId) {
    this.sellerId = sellerId;
  }

  public AuctionLot sellerName(@Nullable String sellerName) {
    this.sellerName = sellerName;
    return this;
  }

  /**
   * Get sellerName
   * @return sellerName
   */
  
  @Schema(name = "seller_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seller_name")
  public @Nullable String getSellerName() {
    return sellerName;
  }

  public void setSellerName(@Nullable String sellerName) {
    this.sellerName = sellerName;
  }

  public AuctionLot itemId(@Nullable String itemId) {
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

  public AuctionLot itemName(@Nullable String itemName) {
    this.itemName = itemName;
    return this;
  }

  /**
   * Get itemName
   * @return itemName
   */
  
  @Schema(name = "item_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_name")
  public @Nullable String getItemName() {
    return itemName;
  }

  public void setItemName(@Nullable String itemName) {
    this.itemName = itemName;
  }

  public AuctionLot quantity(@Nullable Integer quantity) {
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

  public AuctionLot startingPrice(@Nullable BigDecimal startingPrice) {
    this.startingPrice = startingPrice;
    return this;
  }

  /**
   * Get startingPrice
   * @return startingPrice
   */
  @Valid 
  @Schema(name = "starting_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starting_price")
  public @Nullable BigDecimal getStartingPrice() {
    return startingPrice;
  }

  public void setStartingPrice(@Nullable BigDecimal startingPrice) {
    this.startingPrice = startingPrice;
  }

  public AuctionLot currentPrice(@Nullable BigDecimal currentPrice) {
    this.currentPrice = currentPrice;
    return this;
  }

  /**
   * Get currentPrice
   * @return currentPrice
   */
  @Valid 
  @Schema(name = "current_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_price")
  public @Nullable BigDecimal getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(@Nullable BigDecimal currentPrice) {
    this.currentPrice = currentPrice;
  }

  public AuctionLot buyoutPrice(@Nullable BigDecimal buyoutPrice) {
    this.buyoutPrice = buyoutPrice;
    return this;
  }

  /**
   * Get buyoutPrice
   * @return buyoutPrice
   */
  @Valid 
  @Schema(name = "buyout_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buyout_price")
  public @Nullable BigDecimal getBuyoutPrice() {
    return buyoutPrice;
  }

  public void setBuyoutPrice(@Nullable BigDecimal buyoutPrice) {
    this.buyoutPrice = buyoutPrice;
  }

  public AuctionLot bidsCount(@Nullable Integer bidsCount) {
    this.bidsCount = bidsCount;
    return this;
  }

  /**
   * Get bidsCount
   * @return bidsCount
   */
  
  @Schema(name = "bids_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bids_count")
  public @Nullable Integer getBidsCount() {
    return bidsCount;
  }

  public void setBidsCount(@Nullable Integer bidsCount) {
    this.bidsCount = bidsCount;
  }

  public AuctionLot expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public AuctionLot status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuctionLot auctionLot = (AuctionLot) o;
    return Objects.equals(this.lotId, auctionLot.lotId) &&
        Objects.equals(this.sellerId, auctionLot.sellerId) &&
        Objects.equals(this.sellerName, auctionLot.sellerName) &&
        Objects.equals(this.itemId, auctionLot.itemId) &&
        Objects.equals(this.itemName, auctionLot.itemName) &&
        Objects.equals(this.quantity, auctionLot.quantity) &&
        Objects.equals(this.startingPrice, auctionLot.startingPrice) &&
        Objects.equals(this.currentPrice, auctionLot.currentPrice) &&
        Objects.equals(this.buyoutPrice, auctionLot.buyoutPrice) &&
        Objects.equals(this.bidsCount, auctionLot.bidsCount) &&
        Objects.equals(this.expiresAt, auctionLot.expiresAt) &&
        Objects.equals(this.status, auctionLot.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lotId, sellerId, sellerName, itemId, itemName, quantity, startingPrice, currentPrice, buyoutPrice, bidsCount, expiresAt, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuctionLot {\n");
    sb.append("    lotId: ").append(toIndentedString(lotId)).append("\n");
    sb.append("    sellerId: ").append(toIndentedString(sellerId)).append("\n");
    sb.append("    sellerName: ").append(toIndentedString(sellerName)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    itemName: ").append(toIndentedString(itemName)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    startingPrice: ").append(toIndentedString(startingPrice)).append("\n");
    sb.append("    currentPrice: ").append(toIndentedString(currentPrice)).append("\n");
    sb.append("    buyoutPrice: ").append(toIndentedString(buyoutPrice)).append("\n");
    sb.append("    bidsCount: ").append(toIndentedString(bidsCount)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

