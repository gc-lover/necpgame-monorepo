package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TradeDetails
 */


public class TradeDetails {

  private @Nullable String tradeId;

  private @Nullable String buyOrderId;

  private @Nullable String sellOrderId;

  private @Nullable String itemId;

  private @Nullable BigDecimal price;

  private @Nullable Integer quantity;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime executedAt;

  private @Nullable String buyerId;

  private @Nullable String sellerId;

  public TradeDetails tradeId(@Nullable String tradeId) {
    this.tradeId = tradeId;
    return this;
  }

  /**
   * Get tradeId
   * @return tradeId
   */
  
  @Schema(name = "trade_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trade_id")
  public @Nullable String getTradeId() {
    return tradeId;
  }

  public void setTradeId(@Nullable String tradeId) {
    this.tradeId = tradeId;
  }

  public TradeDetails buyOrderId(@Nullable String buyOrderId) {
    this.buyOrderId = buyOrderId;
    return this;
  }

  /**
   * Get buyOrderId
   * @return buyOrderId
   */
  
  @Schema(name = "buy_order_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buy_order_id")
  public @Nullable String getBuyOrderId() {
    return buyOrderId;
  }

  public void setBuyOrderId(@Nullable String buyOrderId) {
    this.buyOrderId = buyOrderId;
  }

  public TradeDetails sellOrderId(@Nullable String sellOrderId) {
    this.sellOrderId = sellOrderId;
    return this;
  }

  /**
   * Get sellOrderId
   * @return sellOrderId
   */
  
  @Schema(name = "sell_order_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sell_order_id")
  public @Nullable String getSellOrderId() {
    return sellOrderId;
  }

  public void setSellOrderId(@Nullable String sellOrderId) {
    this.sellOrderId = sellOrderId;
  }

  public TradeDetails itemId(@Nullable String itemId) {
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

  public TradeDetails price(@Nullable BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
   */
  @Valid 
  @Schema(name = "price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public @Nullable BigDecimal getPrice() {
    return price;
  }

  public void setPrice(@Nullable BigDecimal price) {
    this.price = price;
  }

  public TradeDetails quantity(@Nullable Integer quantity) {
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

  public TradeDetails executedAt(@Nullable OffsetDateTime executedAt) {
    this.executedAt = executedAt;
    return this;
  }

  /**
   * Get executedAt
   * @return executedAt
   */
  @Valid 
  @Schema(name = "executed_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("executed_at")
  public @Nullable OffsetDateTime getExecutedAt() {
    return executedAt;
  }

  public void setExecutedAt(@Nullable OffsetDateTime executedAt) {
    this.executedAt = executedAt;
  }

  public TradeDetails buyerId(@Nullable String buyerId) {
    this.buyerId = buyerId;
    return this;
  }

  /**
   * Get buyerId
   * @return buyerId
   */
  
  @Schema(name = "buyer_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buyer_id")
  public @Nullable String getBuyerId() {
    return buyerId;
  }

  public void setBuyerId(@Nullable String buyerId) {
    this.buyerId = buyerId;
  }

  public TradeDetails sellerId(@Nullable String sellerId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradeDetails tradeDetails = (TradeDetails) o;
    return Objects.equals(this.tradeId, tradeDetails.tradeId) &&
        Objects.equals(this.buyOrderId, tradeDetails.buyOrderId) &&
        Objects.equals(this.sellOrderId, tradeDetails.sellOrderId) &&
        Objects.equals(this.itemId, tradeDetails.itemId) &&
        Objects.equals(this.price, tradeDetails.price) &&
        Objects.equals(this.quantity, tradeDetails.quantity) &&
        Objects.equals(this.executedAt, tradeDetails.executedAt) &&
        Objects.equals(this.buyerId, tradeDetails.buyerId) &&
        Objects.equals(this.sellerId, tradeDetails.sellerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tradeId, buyOrderId, sellOrderId, itemId, price, quantity, executedAt, buyerId, sellerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradeDetails {\n");
    sb.append("    tradeId: ").append(toIndentedString(tradeId)).append("\n");
    sb.append("    buyOrderId: ").append(toIndentedString(buyOrderId)).append("\n");
    sb.append("    sellOrderId: ").append(toIndentedString(sellOrderId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    executedAt: ").append(toIndentedString(executedAt)).append("\n");
    sb.append("    buyerId: ").append(toIndentedString(buyerId)).append("\n");
    sb.append("    sellerId: ").append(toIndentedString(sellerId)).append("\n");
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

