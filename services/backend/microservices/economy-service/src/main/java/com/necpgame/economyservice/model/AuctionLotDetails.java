package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * AuctionLotDetails
 */


public class AuctionLotDetails {

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

  private @Nullable Object itemDetails;

  @Valid
  private List<Object> bidHistory = new ArrayList<>();

  private @Nullable BigDecimal timeRemaining;

  public AuctionLotDetails lotId(@Nullable String lotId) {
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

  public AuctionLotDetails sellerId(@Nullable String sellerId) {
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

  public AuctionLotDetails sellerName(@Nullable String sellerName) {
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

  public AuctionLotDetails itemId(@Nullable String itemId) {
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

  public AuctionLotDetails itemName(@Nullable String itemName) {
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

  public AuctionLotDetails quantity(@Nullable Integer quantity) {
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

  public AuctionLotDetails startingPrice(@Nullable BigDecimal startingPrice) {
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

  public AuctionLotDetails currentPrice(@Nullable BigDecimal currentPrice) {
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

  public AuctionLotDetails buyoutPrice(@Nullable BigDecimal buyoutPrice) {
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

  public AuctionLotDetails bidsCount(@Nullable Integer bidsCount) {
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

  public AuctionLotDetails expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public AuctionLotDetails status(@Nullable StatusEnum status) {
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

  public AuctionLotDetails itemDetails(@Nullable Object itemDetails) {
    this.itemDetails = itemDetails;
    return this;
  }

  /**
   * Get itemDetails
   * @return itemDetails
   */
  
  @Schema(name = "item_details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_details")
  public @Nullable Object getItemDetails() {
    return itemDetails;
  }

  public void setItemDetails(@Nullable Object itemDetails) {
    this.itemDetails = itemDetails;
  }

  public AuctionLotDetails bidHistory(List<Object> bidHistory) {
    this.bidHistory = bidHistory;
    return this;
  }

  public AuctionLotDetails addBidHistoryItem(Object bidHistoryItem) {
    if (this.bidHistory == null) {
      this.bidHistory = new ArrayList<>();
    }
    this.bidHistory.add(bidHistoryItem);
    return this;
  }

  /**
   * Get bidHistory
   * @return bidHistory
   */
  
  @Schema(name = "bid_history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bid_history")
  public List<Object> getBidHistory() {
    return bidHistory;
  }

  public void setBidHistory(List<Object> bidHistory) {
    this.bidHistory = bidHistory;
  }

  public AuctionLotDetails timeRemaining(@Nullable BigDecimal timeRemaining) {
    this.timeRemaining = timeRemaining;
    return this;
  }

  /**
   * Оставшееся время (секунды)
   * @return timeRemaining
   */
  @Valid 
  @Schema(name = "time_remaining", description = "Оставшееся время (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_remaining")
  public @Nullable BigDecimal getTimeRemaining() {
    return timeRemaining;
  }

  public void setTimeRemaining(@Nullable BigDecimal timeRemaining) {
    this.timeRemaining = timeRemaining;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuctionLotDetails auctionLotDetails = (AuctionLotDetails) o;
    return Objects.equals(this.lotId, auctionLotDetails.lotId) &&
        Objects.equals(this.sellerId, auctionLotDetails.sellerId) &&
        Objects.equals(this.sellerName, auctionLotDetails.sellerName) &&
        Objects.equals(this.itemId, auctionLotDetails.itemId) &&
        Objects.equals(this.itemName, auctionLotDetails.itemName) &&
        Objects.equals(this.quantity, auctionLotDetails.quantity) &&
        Objects.equals(this.startingPrice, auctionLotDetails.startingPrice) &&
        Objects.equals(this.currentPrice, auctionLotDetails.currentPrice) &&
        Objects.equals(this.buyoutPrice, auctionLotDetails.buyoutPrice) &&
        Objects.equals(this.bidsCount, auctionLotDetails.bidsCount) &&
        Objects.equals(this.expiresAt, auctionLotDetails.expiresAt) &&
        Objects.equals(this.status, auctionLotDetails.status) &&
        Objects.equals(this.itemDetails, auctionLotDetails.itemDetails) &&
        Objects.equals(this.bidHistory, auctionLotDetails.bidHistory) &&
        Objects.equals(this.timeRemaining, auctionLotDetails.timeRemaining);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lotId, sellerId, sellerName, itemId, itemName, quantity, startingPrice, currentPrice, buyoutPrice, bidsCount, expiresAt, status, itemDetails, bidHistory, timeRemaining);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuctionLotDetails {\n");
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
    sb.append("    itemDetails: ").append(toIndentedString(itemDetails)).append("\n");
    sb.append("    bidHistory: ").append(toIndentedString(bidHistory)).append("\n");
    sb.append("    timeRemaining: ").append(toIndentedString(timeRemaining)).append("\n");
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

