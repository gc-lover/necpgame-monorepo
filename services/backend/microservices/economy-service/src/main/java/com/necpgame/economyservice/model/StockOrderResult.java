package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * StockOrderResult
 */


public class StockOrderResult {

  private @Nullable String orderId;

  private @Nullable String ticker;

  private @Nullable Integer quantity;

  private @Nullable String orderType;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    FILLED("filled"),
    
    PARTIALLY_FILLED("partially_filled"),
    
    PENDING("pending");

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

  private @Nullable Integer filledQuantity;

  private @Nullable BigDecimal avgFillPrice;

  private @Nullable BigDecimal totalCost;

  private @Nullable BigDecimal commission;

  public StockOrderResult orderId(@Nullable String orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  
  @Schema(name = "order_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_id")
  public @Nullable String getOrderId() {
    return orderId;
  }

  public void setOrderId(@Nullable String orderId) {
    this.orderId = orderId;
  }

  public StockOrderResult ticker(@Nullable String ticker) {
    this.ticker = ticker;
    return this;
  }

  /**
   * Get ticker
   * @return ticker
   */
  
  @Schema(name = "ticker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticker")
  public @Nullable String getTicker() {
    return ticker;
  }

  public void setTicker(@Nullable String ticker) {
    this.ticker = ticker;
  }

  public StockOrderResult quantity(@Nullable Integer quantity) {
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

  public StockOrderResult orderType(@Nullable String orderType) {
    this.orderType = orderType;
    return this;
  }

  /**
   * Get orderType
   * @return orderType
   */
  
  @Schema(name = "order_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_type")
  public @Nullable String getOrderType() {
    return orderType;
  }

  public void setOrderType(@Nullable String orderType) {
    this.orderType = orderType;
  }

  public StockOrderResult status(@Nullable StatusEnum status) {
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

  public StockOrderResult filledQuantity(@Nullable Integer filledQuantity) {
    this.filledQuantity = filledQuantity;
    return this;
  }

  /**
   * Get filledQuantity
   * @return filledQuantity
   */
  
  @Schema(name = "filled_quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filled_quantity")
  public @Nullable Integer getFilledQuantity() {
    return filledQuantity;
  }

  public void setFilledQuantity(@Nullable Integer filledQuantity) {
    this.filledQuantity = filledQuantity;
  }

  public StockOrderResult avgFillPrice(@Nullable BigDecimal avgFillPrice) {
    this.avgFillPrice = avgFillPrice;
    return this;
  }

  /**
   * Get avgFillPrice
   * @return avgFillPrice
   */
  @Valid 
  @Schema(name = "avg_fill_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avg_fill_price")
  public @Nullable BigDecimal getAvgFillPrice() {
    return avgFillPrice;
  }

  public void setAvgFillPrice(@Nullable BigDecimal avgFillPrice) {
    this.avgFillPrice = avgFillPrice;
  }

  public StockOrderResult totalCost(@Nullable BigDecimal totalCost) {
    this.totalCost = totalCost;
    return this;
  }

  /**
   * Get totalCost
   * @return totalCost
   */
  @Valid 
  @Schema(name = "total_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_cost")
  public @Nullable BigDecimal getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(@Nullable BigDecimal totalCost) {
    this.totalCost = totalCost;
  }

  public StockOrderResult commission(@Nullable BigDecimal commission) {
    this.commission = commission;
    return this;
  }

  /**
   * Get commission
   * @return commission
   */
  @Valid 
  @Schema(name = "commission", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commission")
  public @Nullable BigDecimal getCommission() {
    return commission;
  }

  public void setCommission(@Nullable BigDecimal commission) {
    this.commission = commission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockOrderResult stockOrderResult = (StockOrderResult) o;
    return Objects.equals(this.orderId, stockOrderResult.orderId) &&
        Objects.equals(this.ticker, stockOrderResult.ticker) &&
        Objects.equals(this.quantity, stockOrderResult.quantity) &&
        Objects.equals(this.orderType, stockOrderResult.orderType) &&
        Objects.equals(this.status, stockOrderResult.status) &&
        Objects.equals(this.filledQuantity, stockOrderResult.filledQuantity) &&
        Objects.equals(this.avgFillPrice, stockOrderResult.avgFillPrice) &&
        Objects.equals(this.totalCost, stockOrderResult.totalCost) &&
        Objects.equals(this.commission, stockOrderResult.commission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, ticker, quantity, orderType, status, filledQuantity, avgFillPrice, totalCost, commission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StockOrderResult {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    filledQuantity: ").append(toIndentedString(filledQuantity)).append("\n");
    sb.append("    avgFillPrice: ").append(toIndentedString(avgFillPrice)).append("\n");
    sb.append("    totalCost: ").append(toIndentedString(totalCost)).append("\n");
    sb.append("    commission: ").append(toIndentedString(commission)).append("\n");
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

