package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * ExchangeOrderRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ExchangeOrderRequest {

  private UUID characterId;

  private String pair;

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

  private SideEnum side;

  private Float amount;

  /**
   * Gets or Sets orderType
   */
  public enum OrderTypeEnum {
    MARKET("MARKET"),
    
    LIMIT("LIMIT");

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

  private OrderTypeEnum orderType = OrderTypeEnum.MARKET;

  private JsonNullable<Float> limitPrice = JsonNullable.<Float>undefined();

  private Integer leverage = 1;

  private JsonNullable<Float> stopLoss = JsonNullable.<Float>undefined();

  private JsonNullable<Float> takeProfit = JsonNullable.<Float>undefined();

  public ExchangeOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExchangeOrderRequest(UUID characterId, String pair, SideEnum side, Float amount) {
    this.characterId = characterId;
    this.pair = pair;
    this.side = side;
    this.amount = amount;
  }

  public ExchangeOrderRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public ExchangeOrderRequest pair(String pair) {
    this.pair = pair;
    return this;
  }

  /**
   * Get pair
   * @return pair
   */
  @NotNull 
  @Schema(name = "pair", example = "NCRD/EURO", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pair")
  public String getPair() {
    return pair;
  }

  public void setPair(String pair) {
    this.pair = pair;
  }

  public ExchangeOrderRequest side(SideEnum side) {
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

  public ExchangeOrderRequest amount(Float amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  @NotNull 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public ExchangeOrderRequest orderType(OrderTypeEnum orderType) {
    this.orderType = orderType;
    return this;
  }

  /**
   * Get orderType
   * @return orderType
   */
  
  @Schema(name = "order_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_type")
  public OrderTypeEnum getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderTypeEnum orderType) {
    this.orderType = orderType;
  }

  public ExchangeOrderRequest limitPrice(Float limitPrice) {
    this.limitPrice = JsonNullable.of(limitPrice);
    return this;
  }

  /**
   * Get limitPrice
   * @return limitPrice
   */
  
  @Schema(name = "limit_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limit_price")
  public JsonNullable<Float> getLimitPrice() {
    return limitPrice;
  }

  public void setLimitPrice(JsonNullable<Float> limitPrice) {
    this.limitPrice = limitPrice;
  }

  public ExchangeOrderRequest leverage(Integer leverage) {
    this.leverage = leverage;
    return this;
  }

  /**
   * Get leverage
   * minimum: 1
   * maximum: 10
   * @return leverage
   */
  @Min(value = 1) @Max(value = 10) 
  @Schema(name = "leverage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leverage")
  public Integer getLeverage() {
    return leverage;
  }

  public void setLeverage(Integer leverage) {
    this.leverage = leverage;
  }

  public ExchangeOrderRequest stopLoss(Float stopLoss) {
    this.stopLoss = JsonNullable.of(stopLoss);
    return this;
  }

  /**
   * Get stopLoss
   * @return stopLoss
   */
  
  @Schema(name = "stop_loss", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stop_loss")
  public JsonNullable<Float> getStopLoss() {
    return stopLoss;
  }

  public void setStopLoss(JsonNullable<Float> stopLoss) {
    this.stopLoss = stopLoss;
  }

  public ExchangeOrderRequest takeProfit(Float takeProfit) {
    this.takeProfit = JsonNullable.of(takeProfit);
    return this;
  }

  /**
   * Get takeProfit
   * @return takeProfit
   */
  
  @Schema(name = "take_profit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("take_profit")
  public JsonNullable<Float> getTakeProfit() {
    return takeProfit;
  }

  public void setTakeProfit(JsonNullable<Float> takeProfit) {
    this.takeProfit = takeProfit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExchangeOrderRequest exchangeOrderRequest = (ExchangeOrderRequest) o;
    return Objects.equals(this.characterId, exchangeOrderRequest.characterId) &&
        Objects.equals(this.pair, exchangeOrderRequest.pair) &&
        Objects.equals(this.side, exchangeOrderRequest.side) &&
        Objects.equals(this.amount, exchangeOrderRequest.amount) &&
        Objects.equals(this.orderType, exchangeOrderRequest.orderType) &&
        equalsNullable(this.limitPrice, exchangeOrderRequest.limitPrice) &&
        Objects.equals(this.leverage, exchangeOrderRequest.leverage) &&
        equalsNullable(this.stopLoss, exchangeOrderRequest.stopLoss) &&
        equalsNullable(this.takeProfit, exchangeOrderRequest.takeProfit);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, pair, side, amount, orderType, hashCodeNullable(limitPrice), leverage, hashCodeNullable(stopLoss), hashCodeNullable(takeProfit));
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
    sb.append("class ExchangeOrderRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    pair: ").append(toIndentedString(pair)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    limitPrice: ").append(toIndentedString(limitPrice)).append("\n");
    sb.append("    leverage: ").append(toIndentedString(leverage)).append("\n");
    sb.append("    stopLoss: ").append(toIndentedString(stopLoss)).append("\n");
    sb.append("    takeProfit: ").append(toIndentedString(takeProfit)).append("\n");
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

