package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.PlayerOrder;
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
 * GetMyOrders200Response
 */

@JsonTypeName("getMyOrders_200_response")

public class GetMyOrders200Response {

  @Valid
  private List<@Valid PlayerOrder> orders = new ArrayList<>();

  public GetMyOrders200Response orders(List<@Valid PlayerOrder> orders) {
    this.orders = orders;
    return this;
  }

  public GetMyOrders200Response addOrdersItem(PlayerOrder ordersItem) {
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
  public List<@Valid PlayerOrder> getOrders() {
    return orders;
  }

  public void setOrders(List<@Valid PlayerOrder> orders) {
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
    GetMyOrders200Response getMyOrders200Response = (GetMyOrders200Response) o;
    return Objects.equals(this.orders, getMyOrders200Response.orders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orders);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMyOrders200Response {\n");
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

