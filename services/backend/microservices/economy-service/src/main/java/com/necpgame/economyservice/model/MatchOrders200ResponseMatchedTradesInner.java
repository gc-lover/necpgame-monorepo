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
 * MatchOrders200ResponseMatchedTradesInner
 */

@JsonTypeName("matchOrders_200_response_matched_trades_inner")

public class MatchOrders200ResponseMatchedTradesInner {

  private @Nullable String tradeId;

  private @Nullable String buyOrderId;

  private @Nullable String sellOrderId;

  private @Nullable BigDecimal price;

  private @Nullable Integer quantity;

  public MatchOrders200ResponseMatchedTradesInner tradeId(@Nullable String tradeId) {
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

  public MatchOrders200ResponseMatchedTradesInner buyOrderId(@Nullable String buyOrderId) {
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

  public MatchOrders200ResponseMatchedTradesInner sellOrderId(@Nullable String sellOrderId) {
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

  public MatchOrders200ResponseMatchedTradesInner price(@Nullable BigDecimal price) {
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

  public MatchOrders200ResponseMatchedTradesInner quantity(@Nullable Integer quantity) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchOrders200ResponseMatchedTradesInner matchOrders200ResponseMatchedTradesInner = (MatchOrders200ResponseMatchedTradesInner) o;
    return Objects.equals(this.tradeId, matchOrders200ResponseMatchedTradesInner.tradeId) &&
        Objects.equals(this.buyOrderId, matchOrders200ResponseMatchedTradesInner.buyOrderId) &&
        Objects.equals(this.sellOrderId, matchOrders200ResponseMatchedTradesInner.sellOrderId) &&
        Objects.equals(this.price, matchOrders200ResponseMatchedTradesInner.price) &&
        Objects.equals(this.quantity, matchOrders200ResponseMatchedTradesInner.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tradeId, buyOrderId, sellOrderId, price, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchOrders200ResponseMatchedTradesInner {\n");
    sb.append("    tradeId: ").append(toIndentedString(tradeId)).append("\n");
    sb.append("    buyOrderId: ").append(toIndentedString(buyOrderId)).append("\n");
    sb.append("    sellOrderId: ").append(toIndentedString(sellOrderId)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
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

