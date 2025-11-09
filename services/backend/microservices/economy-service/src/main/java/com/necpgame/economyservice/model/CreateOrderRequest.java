package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CreateOrderRequest
 */

@JsonTypeName("createOrder_request")

public class CreateOrderRequest {

  private String characterId;

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

  private OrderTypeEnum orderType;

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

  private SideEnum side;

  private String itemId;

  private Integer quantity;

  private @Nullable BigDecimal price;

  public CreateOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateOrderRequest(String characterId, OrderTypeEnum orderType, SideEnum side, String itemId, Integer quantity) {
    this.characterId = characterId;
    this.orderType = orderType;
    this.side = side;
    this.itemId = itemId;
    this.quantity = quantity;
  }

  public CreateOrderRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public CreateOrderRequest orderType(OrderTypeEnum orderType) {
    this.orderType = orderType;
    return this;
  }

  /**
   * Get orderType
   * @return orderType
   */
  @NotNull 
  @Schema(name = "order_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("order_type")
  public OrderTypeEnum getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderTypeEnum orderType) {
    this.orderType = orderType;
  }

  public CreateOrderRequest side(SideEnum side) {
    this.side = side;
    return this;
  }

  /**
   * Get side
   * @return side
   */
  @NotNull 
  @Schema(name = "side", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("side")
  public SideEnum getSide() {
    return side;
  }

  public void setSide(SideEnum side) {
    this.side = side;
  }

  public CreateOrderRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("item_id")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public CreateOrderRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @NotNull 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public CreateOrderRequest price(@Nullable BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Для limit orders
   * @return price
   */
  @Valid 
  @Schema(name = "price", description = "Для limit orders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public @Nullable BigDecimal getPrice() {
    return price;
  }

  public void setPrice(@Nullable BigDecimal price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateOrderRequest createOrderRequest = (CreateOrderRequest) o;
    return Objects.equals(this.characterId, createOrderRequest.characterId) &&
        Objects.equals(this.orderType, createOrderRequest.orderType) &&
        Objects.equals(this.side, createOrderRequest.side) &&
        Objects.equals(this.itemId, createOrderRequest.itemId) &&
        Objects.equals(this.quantity, createOrderRequest.quantity) &&
        Objects.equals(this.price, createOrderRequest.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, orderType, side, itemId, quantity, price);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateOrderRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
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

