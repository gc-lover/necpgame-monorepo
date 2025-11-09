package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.PurchaseRequestExpectedPrice;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PurchaseRequest
 */


public class PurchaseRequest {

  private String playerId;

  private String itemId;

  /**
   * Gets or Sets paymentMethod
   */
  public enum PaymentMethodEnum {
    WALLET("wallet"),
    
    PREMIUM_CURRENCY("premium_currency"),
    
    COUPON("coupon");

    private final String value;

    PaymentMethodEnum(String value) {
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
    public static PaymentMethodEnum fromValue(String value) {
      for (PaymentMethodEnum b : PaymentMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PaymentMethodEnum paymentMethod;

  private @Nullable PurchaseRequestExpectedPrice expectedPrice;

  private @Nullable String region;

  private @Nullable String doubleSpendToken;

  public PurchaseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PurchaseRequest(String playerId, String itemId, PaymentMethodEnum paymentMethod) {
    this.playerId = playerId;
    this.itemId = itemId;
    this.paymentMethod = paymentMethod;
  }

  public PurchaseRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public PurchaseRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public PurchaseRequest paymentMethod(PaymentMethodEnum paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  /**
   * Get paymentMethod
   * @return paymentMethod
   */
  @NotNull 
  @Schema(name = "paymentMethod", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("paymentMethod")
  public PaymentMethodEnum getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public PurchaseRequest expectedPrice(@Nullable PurchaseRequestExpectedPrice expectedPrice) {
    this.expectedPrice = expectedPrice;
    return this;
  }

  /**
   * Get expectedPrice
   * @return expectedPrice
   */
  @Valid 
  @Schema(name = "expectedPrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expectedPrice")
  public @Nullable PurchaseRequestExpectedPrice getExpectedPrice() {
    return expectedPrice;
  }

  public void setExpectedPrice(@Nullable PurchaseRequestExpectedPrice expectedPrice) {
    this.expectedPrice = expectedPrice;
  }

  public PurchaseRequest region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public PurchaseRequest doubleSpendToken(@Nullable String doubleSpendToken) {
    this.doubleSpendToken = doubleSpendToken;
    return this;
  }

  /**
   * Idempotency token
   * @return doubleSpendToken
   */
  
  @Schema(name = "doubleSpendToken", description = "Idempotency token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("doubleSpendToken")
  public @Nullable String getDoubleSpendToken() {
    return doubleSpendToken;
  }

  public void setDoubleSpendToken(@Nullable String doubleSpendToken) {
    this.doubleSpendToken = doubleSpendToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurchaseRequest purchaseRequest = (PurchaseRequest) o;
    return Objects.equals(this.playerId, purchaseRequest.playerId) &&
        Objects.equals(this.itemId, purchaseRequest.itemId) &&
        Objects.equals(this.paymentMethod, purchaseRequest.paymentMethod) &&
        Objects.equals(this.expectedPrice, purchaseRequest.expectedPrice) &&
        Objects.equals(this.region, purchaseRequest.region) &&
        Objects.equals(this.doubleSpendToken, purchaseRequest.doubleSpendToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, itemId, paymentMethod, expectedPrice, region, doubleSpendToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
    sb.append("    expectedPrice: ").append(toIndentedString(expectedPrice)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    doubleSpendToken: ").append(toIndentedString(doubleSpendToken)).append("\n");
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

