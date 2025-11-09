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
 * OrderBookBuyOrdersInner
 */

@JsonTypeName("OrderBook_buy_orders_inner")

public class OrderBookBuyOrdersInner {

  private @Nullable BigDecimal price;

  private @Nullable Integer quantity;

  private @Nullable Integer totalOrders;

  public OrderBookBuyOrdersInner price(@Nullable BigDecimal price) {
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

  public OrderBookBuyOrdersInner quantity(@Nullable Integer quantity) {
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

  public OrderBookBuyOrdersInner totalOrders(@Nullable Integer totalOrders) {
    this.totalOrders = totalOrders;
    return this;
  }

  /**
   * Get totalOrders
   * @return totalOrders
   */
  
  @Schema(name = "total_orders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_orders")
  public @Nullable Integer getTotalOrders() {
    return totalOrders;
  }

  public void setTotalOrders(@Nullable Integer totalOrders) {
    this.totalOrders = totalOrders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderBookBuyOrdersInner orderBookBuyOrdersInner = (OrderBookBuyOrdersInner) o;
    return Objects.equals(this.price, orderBookBuyOrdersInner.price) &&
        Objects.equals(this.quantity, orderBookBuyOrdersInner.quantity) &&
        Objects.equals(this.totalOrders, orderBookBuyOrdersInner.totalOrders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(price, quantity, totalOrders);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderBookBuyOrdersInner {\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    totalOrders: ").append(toIndentedString(totalOrders)).append("\n");
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

