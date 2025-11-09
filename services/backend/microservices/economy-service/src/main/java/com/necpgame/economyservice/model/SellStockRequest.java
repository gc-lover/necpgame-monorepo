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
 * SellStockRequest
 */

@JsonTypeName("sellStock_request")

public class SellStockRequest {

  private String characterId;

  private String ticker;

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

  public SellStockRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SellStockRequest(String characterId, String ticker, Integer quantity, OrderTypeEnum orderType) {
    this.characterId = characterId;
    this.ticker = ticker;
    this.quantity = quantity;
    this.orderType = orderType;
  }

  public SellStockRequest characterId(String characterId) {
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

  public SellStockRequest ticker(String ticker) {
    this.ticker = ticker;
    return this;
  }

  /**
   * Get ticker
   * @return ticker
   */
  @NotNull 
  @Schema(name = "ticker", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ticker")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public SellStockRequest quantity(Integer quantity) {
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

  public SellStockRequest orderType(OrderTypeEnum orderType) {
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

  public SellStockRequest limitPrice(@Nullable BigDecimal limitPrice) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SellStockRequest sellStockRequest = (SellStockRequest) o;
    return Objects.equals(this.characterId, sellStockRequest.characterId) &&
        Objects.equals(this.ticker, sellStockRequest.ticker) &&
        Objects.equals(this.quantity, sellStockRequest.quantity) &&
        Objects.equals(this.orderType, sellStockRequest.orderType) &&
        Objects.equals(this.limitPrice, sellStockRequest.limitPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, ticker, quantity, orderType, limitPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SellStockRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    limitPrice: ").append(toIndentedString(limitPrice)).append("\n");
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

