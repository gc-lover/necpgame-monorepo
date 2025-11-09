package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.OrderBookBuyOrdersInner;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OrderBook
 */


public class OrderBook {

  private @Nullable String itemId;

  @Valid
  private List<@Valid OrderBookBuyOrdersInner> buyOrders = new ArrayList<>();

  @Valid
  private List<@Valid OrderBookBuyOrdersInner> sellOrders = new ArrayList<>();

  private @Nullable BigDecimal spread;

  private @Nullable BigDecimal lastTradePrice;

  public OrderBook itemId(@Nullable String itemId) {
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

  public OrderBook buyOrders(List<@Valid OrderBookBuyOrdersInner> buyOrders) {
    this.buyOrders = buyOrders;
    return this;
  }

  public OrderBook addBuyOrdersItem(OrderBookBuyOrdersInner buyOrdersItem) {
    if (this.buyOrders == null) {
      this.buyOrders = new ArrayList<>();
    }
    this.buyOrders.add(buyOrdersItem);
    return this;
  }

  /**
   * Get buyOrders
   * @return buyOrders
   */
  @Valid 
  @Schema(name = "buy_orders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buy_orders")
  public List<@Valid OrderBookBuyOrdersInner> getBuyOrders() {
    return buyOrders;
  }

  public void setBuyOrders(List<@Valid OrderBookBuyOrdersInner> buyOrders) {
    this.buyOrders = buyOrders;
  }

  public OrderBook sellOrders(List<@Valid OrderBookBuyOrdersInner> sellOrders) {
    this.sellOrders = sellOrders;
    return this;
  }

  public OrderBook addSellOrdersItem(OrderBookBuyOrdersInner sellOrdersItem) {
    if (this.sellOrders == null) {
      this.sellOrders = new ArrayList<>();
    }
    this.sellOrders.add(sellOrdersItem);
    return this;
  }

  /**
   * Get sellOrders
   * @return sellOrders
   */
  @Valid 
  @Schema(name = "sell_orders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sell_orders")
  public List<@Valid OrderBookBuyOrdersInner> getSellOrders() {
    return sellOrders;
  }

  public void setSellOrders(List<@Valid OrderBookBuyOrdersInner> sellOrders) {
    this.sellOrders = sellOrders;
  }

  public OrderBook spread(@Nullable BigDecimal spread) {
    this.spread = spread;
    return this;
  }

  /**
   * Разница между лучшей покупкой и продажей
   * @return spread
   */
  @Valid 
  @Schema(name = "spread", description = "Разница между лучшей покупкой и продажей", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spread")
  public @Nullable BigDecimal getSpread() {
    return spread;
  }

  public void setSpread(@Nullable BigDecimal spread) {
    this.spread = spread;
  }

  public OrderBook lastTradePrice(@Nullable BigDecimal lastTradePrice) {
    this.lastTradePrice = lastTradePrice;
    return this;
  }

  /**
   * Get lastTradePrice
   * @return lastTradePrice
   */
  @Valid 
  @Schema(name = "last_trade_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_trade_price")
  public @Nullable BigDecimal getLastTradePrice() {
    return lastTradePrice;
  }

  public void setLastTradePrice(@Nullable BigDecimal lastTradePrice) {
    this.lastTradePrice = lastTradePrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderBook orderBook = (OrderBook) o;
    return Objects.equals(this.itemId, orderBook.itemId) &&
        Objects.equals(this.buyOrders, orderBook.buyOrders) &&
        Objects.equals(this.sellOrders, orderBook.sellOrders) &&
        Objects.equals(this.spread, orderBook.spread) &&
        Objects.equals(this.lastTradePrice, orderBook.lastTradePrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, buyOrders, sellOrders, spread, lastTradePrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderBook {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    buyOrders: ").append(toIndentedString(buyOrders)).append("\n");
    sb.append("    sellOrders: ").append(toIndentedString(sellOrders)).append("\n");
    sb.append("    spread: ").append(toIndentedString(spread)).append("\n");
    sb.append("    lastTradePrice: ").append(toIndentedString(lastTradePrice)).append("\n");
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

