package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.StockOrder;
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
 * GetActiveStockOrders200Response
 */

@JsonTypeName("getActiveStockOrders_200_response")

public class GetActiveStockOrders200Response {

  @Valid
  private List<@Valid StockOrder> orders = new ArrayList<>();

  public GetActiveStockOrders200Response orders(List<@Valid StockOrder> orders) {
    this.orders = orders;
    return this;
  }

  public GetActiveStockOrders200Response addOrdersItem(StockOrder ordersItem) {
    if (this.orders == null) {
      this.orders = new ArrayList<>();
    }
    this.orders.add(ordersItem);
    return this;
  }

  /**
   * Get orders
   * @return orders
   */
  @Valid 
  @Schema(name = "orders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orders")
  public List<@Valid StockOrder> getOrders() {
    return orders;
  }

  public void setOrders(List<@Valid StockOrder> orders) {
    this.orders = orders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetActiveStockOrders200Response getActiveStockOrders200Response = (GetActiveStockOrders200Response) o;
    return Objects.equals(this.orders, getActiveStockOrders200Response.orders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orders);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveStockOrders200Response {\n");
    sb.append("    orders: ").append(toIndentedString(orders)).append("\n");
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

