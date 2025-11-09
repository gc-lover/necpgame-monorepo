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
 * CreateMarketOrderRequest
 */

@JsonTypeName("createMarketOrder_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CreateMarketOrderRequest {

  private String characterId;

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

  private @Nullable BigDecimal limitPrice;

  /**
   * GTC=Good Till Cancel, IOC=Immediate Or Cancel, FOK=Fill Or Kill
   */
  public enum TimeInForceEnum {
    GTC("GTC"),
    
    IOC("IOC"),
    
    FOK("FOK");

    private final String value;

    TimeInForceEnum(String value) {
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
    public static TimeInForceEnum fromValue(String value) {
      for (TimeInForceEnum b : TimeInForceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TimeInForceEnum timeInForce = TimeInForceEnum.GTC;

  public CreateMarketOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateMarketOrderRequest(String characterId, SideEnum side, String itemId, Integer quantity, OrderTypeEnum orderType) {
    this.characterId = characterId;
    this.side = side;
    this.itemId = itemId;
    this.quantity = quantity;
    this.orderType = orderType;
  }

  public CreateMarketOrderRequest characterId(String characterId) {
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

  public CreateMarketOrderRequest side(SideEnum side) {
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

  public CreateMarketOrderRequest itemId(String itemId) {
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

  public CreateMarketOrderRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * minimum: 1
   * @return quantity
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public CreateMarketOrderRequest orderType(OrderTypeEnum orderType) {
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

  public CreateMarketOrderRequest limitPrice(@Nullable BigDecimal limitPrice) {
    this.limitPrice = limitPrice;
    return this;
  }

  /**
   * Для limit orders
   * @return limitPrice
   */
  @Valid 
  @Schema(name = "limit_price", description = "Для limit orders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limit_price")
  public @Nullable BigDecimal getLimitPrice() {
    return limitPrice;
  }

  public void setLimitPrice(@Nullable BigDecimal limitPrice) {
    this.limitPrice = limitPrice;
  }

  public CreateMarketOrderRequest timeInForce(TimeInForceEnum timeInForce) {
    this.timeInForce = timeInForce;
    return this;
  }

  /**
   * GTC=Good Till Cancel, IOC=Immediate Or Cancel, FOK=Fill Or Kill
   * @return timeInForce
   */
  
  @Schema(name = "time_in_force", description = "GTC=Good Till Cancel, IOC=Immediate Or Cancel, FOK=Fill Or Kill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_in_force")
  public TimeInForceEnum getTimeInForce() {
    return timeInForce;
  }

  public void setTimeInForce(TimeInForceEnum timeInForce) {
    this.timeInForce = timeInForce;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateMarketOrderRequest createMarketOrderRequest = (CreateMarketOrderRequest) o;
    return Objects.equals(this.characterId, createMarketOrderRequest.characterId) &&
        Objects.equals(this.side, createMarketOrderRequest.side) &&
        Objects.equals(this.itemId, createMarketOrderRequest.itemId) &&
        Objects.equals(this.quantity, createMarketOrderRequest.quantity) &&
        Objects.equals(this.orderType, createMarketOrderRequest.orderType) &&
        Objects.equals(this.limitPrice, createMarketOrderRequest.limitPrice) &&
        Objects.equals(this.timeInForce, createMarketOrderRequest.timeInForce);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, side, itemId, quantity, orderType, limitPrice, timeInForce);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateMarketOrderRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    limitPrice: ").append(toIndentedString(limitPrice)).append("\n");
    sb.append("    timeInForce: ").append(toIndentedString(timeInForce)).append("\n");
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

