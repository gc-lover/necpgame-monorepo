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
 * OrderDetails
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class OrderDetails {

  private @Nullable String orderId;

  private @Nullable String characterId;

  /**
   * Gets or Sets side
   */
  public enum SideEnum {
    BUY("buy"),
    
    SELL("sell");

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

  private @Nullable String itemId;

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
    
    CANCELLED("cancelled"),
    
    EXPIRED("expired");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  @Valid
  private List<Object> executionHistory = new ArrayList<>();

  private @Nullable BigDecimal timeRemaining;

  public OrderDetails orderId(@Nullable String orderId) {
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

  public OrderDetails characterId(@Nullable String characterId) {
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

  public OrderDetails side(@Nullable SideEnum side) {
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

  public OrderDetails itemId(@Nullable String itemId) {
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

  public OrderDetails quantity(@Nullable Integer quantity) {
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

  public OrderDetails filledQuantity(@Nullable Integer filledQuantity) {
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

  public OrderDetails orderType(@Nullable OrderTypeEnum orderType) {
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

  public OrderDetails limitPrice(@Nullable BigDecimal limitPrice) {
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

  public OrderDetails status(@Nullable StatusEnum status) {
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

  public OrderDetails createdAt(@Nullable OffsetDateTime createdAt) {
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

  public OrderDetails updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updated_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updated_at")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public OrderDetails executionHistory(List<Object> executionHistory) {
    this.executionHistory = executionHistory;
    return this;
  }

  public OrderDetails addExecutionHistoryItem(Object executionHistoryItem) {
    if (this.executionHistory == null) {
      this.executionHistory = new ArrayList<>();
    }
    this.executionHistory.add(executionHistoryItem);
    return this;
  }

  /**
   * Get executionHistory
   * @return executionHistory
   */
  
  @Schema(name = "execution_history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("execution_history")
  public List<Object> getExecutionHistory() {
    return executionHistory;
  }

  public void setExecutionHistory(List<Object> executionHistory) {
    this.executionHistory = executionHistory;
  }

  public OrderDetails timeRemaining(@Nullable BigDecimal timeRemaining) {
    this.timeRemaining = timeRemaining;
    return this;
  }

  /**
   * Оставшееся время (секунды, для GTC)
   * @return timeRemaining
   */
  @Valid 
  @Schema(name = "time_remaining", description = "Оставшееся время (секунды, для GTC)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    OrderDetails orderDetails = (OrderDetails) o;
    return Objects.equals(this.orderId, orderDetails.orderId) &&
        Objects.equals(this.characterId, orderDetails.characterId) &&
        Objects.equals(this.side, orderDetails.side) &&
        Objects.equals(this.itemId, orderDetails.itemId) &&
        Objects.equals(this.quantity, orderDetails.quantity) &&
        Objects.equals(this.filledQuantity, orderDetails.filledQuantity) &&
        Objects.equals(this.orderType, orderDetails.orderType) &&
        Objects.equals(this.limitPrice, orderDetails.limitPrice) &&
        Objects.equals(this.status, orderDetails.status) &&
        Objects.equals(this.createdAt, orderDetails.createdAt) &&
        Objects.equals(this.updatedAt, orderDetails.updatedAt) &&
        Objects.equals(this.executionHistory, orderDetails.executionHistory) &&
        Objects.equals(this.timeRemaining, orderDetails.timeRemaining);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, characterId, side, itemId, quantity, filledQuantity, orderType, limitPrice, status, createdAt, updatedAt, executionHistory, timeRemaining);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderDetails {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    filledQuantity: ").append(toIndentedString(filledQuantity)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    limitPrice: ").append(toIndentedString(limitPrice)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    executionHistory: ").append(toIndentedString(executionHistory)).append("\n");
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

