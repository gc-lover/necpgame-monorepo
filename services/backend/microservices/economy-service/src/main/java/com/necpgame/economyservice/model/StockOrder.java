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
 * StockOrder
 */


public class StockOrder {

  private @Nullable String orderId;

  private @Nullable String characterId;

  private @Nullable String ticker;

  /**
   * Gets or Sets side
   */
  public enum SideEnum {
    BUY("buy"),
    
    SELL("sell"),
    
    SHORT("short");

    private final String value;

    SideEnum(String value) {
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
    public static SideEnum fromValue(String value) {
      for (SideEnum b : SideEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SideEnum side;

  private @Nullable Integer quantity;

  private @Nullable Integer filledQuantity;

  /**
   * Gets or Sets orderType
   */
  public enum OrderTypeEnum {
    MARKET("market"),
    
    LIMIT("limit");

    private final String value;

    OrderTypeEnum(String value) {
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
    public static OrderTypeEnum fromValue(String value) {
      for (OrderTypeEnum b : OrderTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OrderTypeEnum orderType;

  private @Nullable BigDecimal limitPrice;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    PARTIALLY_FILLED("partially_filled"),
    
    FILLED("filled"),
    
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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public StockOrder orderId(@Nullable String orderId) {
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

  public StockOrder characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public StockOrder ticker(@Nullable String ticker) {
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

  public StockOrder side(@Nullable SideEnum side) {
    this.side = side;
    return this;
  }

  /**
   * Get side
   * @return side
   */
  
  @Schema(name = "side", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("side")
  public @Nullable SideEnum getSide() {
    return side;
  }

  public void setSide(@Nullable SideEnum side) {
    this.side = side;
  }

  public StockOrder quantity(@Nullable Integer quantity) {
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

  public StockOrder filledQuantity(@Nullable Integer filledQuantity) {
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

  public StockOrder orderType(@Nullable OrderTypeEnum orderType) {
    this.orderType = orderType;
    return this;
  }

  /**
   * Get orderType
   * @return orderType
   */
  
  @Schema(name = "order_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_type")
  public @Nullable OrderTypeEnum getOrderType() {
    return orderType;
  }

  public void setOrderType(@Nullable OrderTypeEnum orderType) {
    this.orderType = orderType;
  }

  public StockOrder limitPrice(@Nullable BigDecimal limitPrice) {
    this.limitPrice = limitPrice;
    return this;
  }

  /**
   * Get limitPrice
   * @return limitPrice
   */
  @Valid 
  @Schema(name = "limit_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limit_price")
  public @Nullable BigDecimal getLimitPrice() {
    return limitPrice;
  }

  public void setLimitPrice(@Nullable BigDecimal limitPrice) {
    this.limitPrice = limitPrice;
  }

  public StockOrder status(@Nullable StatusEnum status) {
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

  public StockOrder createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockOrder stockOrder = (StockOrder) o;
    return Objects.equals(this.orderId, stockOrder.orderId) &&
        Objects.equals(this.characterId, stockOrder.characterId) &&
        Objects.equals(this.ticker, stockOrder.ticker) &&
        Objects.equals(this.side, stockOrder.side) &&
        Objects.equals(this.quantity, stockOrder.quantity) &&
        Objects.equals(this.filledQuantity, stockOrder.filledQuantity) &&
        Objects.equals(this.orderType, stockOrder.orderType) &&
        Objects.equals(this.limitPrice, stockOrder.limitPrice) &&
        Objects.equals(this.status, stockOrder.status) &&
        Objects.equals(this.createdAt, stockOrder.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, characterId, ticker, side, quantity, filledQuantity, orderType, limitPrice, status, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StockOrder {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    filledQuantity: ").append(toIndentedString(filledQuantity)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    limitPrice: ").append(toIndentedString(limitPrice)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

