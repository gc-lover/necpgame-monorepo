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
 * BuyStockRequest
 */

@JsonTypeName("buyStock_request")

public class BuyStockRequest {

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

  public BuyStockRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BuyStockRequest(String characterId, String ticker, Integer quantity, OrderTypeEnum orderType) {
    this.characterId = characterId;
    this.ticker = ticker;
    this.quantity = quantity;
    this.orderType = orderType;
  }

  public BuyStockRequest characterId(String characterId) {
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

  public BuyStockRequest ticker(String ticker) {
    this.ticker = ticker;
    return this;
  }

  /**
   * Тикер корпорации (ARSK, MILT, etc.)
   * @return ticker
   */
  @NotNull 
  @Schema(name = "ticker", description = "Тикер корпорации (ARSK, MILT, etc.)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ticker")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public BuyStockRequest quantity(Integer quantity) {
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

  public BuyStockRequest orderType(OrderTypeEnum orderType) {
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

  public BuyStockRequest limitPrice(@Nullable BigDecimal limitPrice) {
    this.limitPrice = limitPrice;
    return this;
  }

  /**
   * Цена для limit order (обязательно для limit)
   * @return limitPrice
   */
  @Valid 
  @Schema(name = "limit_price", description = "Цена для limit order (обязательно для limit)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    BuyStockRequest buyStockRequest = (BuyStockRequest) o;
    return Objects.equals(this.characterId, buyStockRequest.characterId) &&
        Objects.equals(this.ticker, buyStockRequest.ticker) &&
        Objects.equals(this.quantity, buyStockRequest.quantity) &&
        Objects.equals(this.orderType, buyStockRequest.orderType) &&
        Objects.equals(this.limitPrice, buyStockRequest.limitPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, ticker, quantity, orderType, limitPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyStockRequest {\n");
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

