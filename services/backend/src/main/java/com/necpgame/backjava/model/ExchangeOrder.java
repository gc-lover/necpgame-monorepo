package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExchangeOrder
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ExchangeOrder {

  private @Nullable UUID orderId;

  private @Nullable UUID characterId;

  private @Nullable String pair;

  /**
   * Gets or Sets side
   */
  public enum SideEnum {
    BUY("BUY"),
    
    SELL("SELL");

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

  private @Nullable BigDecimal amount;

  private @Nullable String orderType;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    FILLED("FILLED"),
    
    PARTIALLY_FILLED("PARTIALLY_FILLED"),
    
    CANCELLED("CANCELLED"),
    
    EXPIRED("EXPIRED");

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

  private @Nullable BigDecimal filledAmount;

  private @Nullable BigDecimal averagePrice;

  private @Nullable Integer leverage;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> filledAt = JsonNullable.<OffsetDateTime>undefined();

  public ExchangeOrder orderId(@Nullable UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @Valid 
  @Schema(name = "order_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_id")
  public @Nullable UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(@Nullable UUID orderId) {
    this.orderId = orderId;
  }

  public ExchangeOrder characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public ExchangeOrder pair(@Nullable String pair) {
    this.pair = pair;
    return this;
  }

  /**
   * Get pair
   * @return pair
   */
  
  @Schema(name = "pair", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pair")
  public @Nullable String getPair() {
    return pair;
  }

  public void setPair(@Nullable String pair) {
    this.pair = pair;
  }

  public ExchangeOrder side(@Nullable SideEnum side) {
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

  public ExchangeOrder amount(@Nullable BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  @Valid 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(@Nullable BigDecimal amount) {
    this.amount = amount;
  }

  public ExchangeOrder orderType(@Nullable String orderType) {
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

  public ExchangeOrder status(@Nullable StatusEnum status) {
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

  public ExchangeOrder filledAmount(@Nullable BigDecimal filledAmount) {
    this.filledAmount = filledAmount;
    return this;
  }

  /**
   * Get filledAmount
   * @return filledAmount
   */
  @Valid 
  @Schema(name = "filled_amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filled_amount")
  public @Nullable BigDecimal getFilledAmount() {
    return filledAmount;
  }

  public void setFilledAmount(@Nullable BigDecimal filledAmount) {
    this.filledAmount = filledAmount;
  }

  public ExchangeOrder averagePrice(@Nullable BigDecimal averagePrice) {
    this.averagePrice = averagePrice;
    return this;
  }

  /**
   * Get averagePrice
   * @return averagePrice
   */
  @Valid 
  @Schema(name = "average_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_price")
  public @Nullable BigDecimal getAveragePrice() {
    return averagePrice;
  }

  public void setAveragePrice(@Nullable BigDecimal averagePrice) {
    this.averagePrice = averagePrice;
  }

  public ExchangeOrder leverage(@Nullable Integer leverage) {
    this.leverage = leverage;
    return this;
  }

  /**
   * Get leverage
   * @return leverage
   */
  
  @Schema(name = "leverage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leverage")
  public @Nullable Integer getLeverage() {
    return leverage;
  }

  public void setLeverage(@Nullable Integer leverage) {
    this.leverage = leverage;
  }

  public ExchangeOrder createdAt(@Nullable OffsetDateTime createdAt) {
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

  public ExchangeOrder filledAt(OffsetDateTime filledAt) {
    this.filledAt = JsonNullable.of(filledAt);
    return this;
  }

  /**
   * Get filledAt
   * @return filledAt
   */
  @Valid 
  @Schema(name = "filled_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filled_at")
  public JsonNullable<OffsetDateTime> getFilledAt() {
    return filledAt;
  }

  public void setFilledAt(JsonNullable<OffsetDateTime> filledAt) {
    this.filledAt = filledAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExchangeOrder exchangeOrder = (ExchangeOrder) o;
    return Objects.equals(this.orderId, exchangeOrder.orderId) &&
        Objects.equals(this.characterId, exchangeOrder.characterId) &&
        Objects.equals(this.pair, exchangeOrder.pair) &&
        Objects.equals(this.side, exchangeOrder.side) &&
        Objects.equals(this.amount, exchangeOrder.amount) &&
        Objects.equals(this.orderType, exchangeOrder.orderType) &&
        Objects.equals(this.status, exchangeOrder.status) &&
        Objects.equals(this.filledAmount, exchangeOrder.filledAmount) &&
        Objects.equals(this.averagePrice, exchangeOrder.averagePrice) &&
        Objects.equals(this.leverage, exchangeOrder.leverage) &&
        Objects.equals(this.createdAt, exchangeOrder.createdAt) &&
        equalsNullable(this.filledAt, exchangeOrder.filledAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, characterId, pair, side, amount, orderType, status, filledAmount, averagePrice, leverage, createdAt, hashCodeNullable(filledAt));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExchangeOrder {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    pair: ").append(toIndentedString(pair)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    filledAmount: ").append(toIndentedString(filledAmount)).append("\n");
    sb.append("    averagePrice: ").append(toIndentedString(averagePrice)).append("\n");
    sb.append("    leverage: ").append(toIndentedString(leverage)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    filledAt: ").append(toIndentedString(filledAt)).append("\n");
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

