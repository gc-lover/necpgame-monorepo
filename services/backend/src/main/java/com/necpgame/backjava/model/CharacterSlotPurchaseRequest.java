package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
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
 * CharacterSlotPurchaseRequest
 */


public class CharacterSlotPurchaseRequest {

  /**
   * Gets or Sets paymentMethod
   */
  public enum PaymentMethodEnum {
    WALLET("wallet"),
    
    EXTERNAL("external"),
    
    VOUCHER("voucher");

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

  private String currency;

  private Integer amount;

  private JsonNullable<String> promoCode = JsonNullable.<String>undefined();

  public CharacterSlotPurchaseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSlotPurchaseRequest(PaymentMethodEnum paymentMethod, String currency, Integer amount) {
    this.paymentMethod = paymentMethod;
    this.currency = currency;
    this.amount = amount;
  }

  public CharacterSlotPurchaseRequest paymentMethod(PaymentMethodEnum paymentMethod) {
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

  public CharacterSlotPurchaseRequest currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @NotNull 
  @Schema(name = "currency", example = "eddies", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currency")
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public CharacterSlotPurchaseRequest amount(Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * minimum: 1
   * @return amount
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public CharacterSlotPurchaseRequest promoCode(String promoCode) {
    this.promoCode = JsonNullable.of(promoCode);
    return this;
  }

  /**
   * Get promoCode
   * @return promoCode
   */
  
  @Schema(name = "promoCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("promoCode")
  public JsonNullable<String> getPromoCode() {
    return promoCode;
  }

  public void setPromoCode(JsonNullable<String> promoCode) {
    this.promoCode = promoCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSlotPurchaseRequest characterSlotPurchaseRequest = (CharacterSlotPurchaseRequest) o;
    return Objects.equals(this.paymentMethod, characterSlotPurchaseRequest.paymentMethod) &&
        Objects.equals(this.currency, characterSlotPurchaseRequest.currency) &&
        Objects.equals(this.amount, characterSlotPurchaseRequest.amount) &&
        equalsNullable(this.promoCode, characterSlotPurchaseRequest.promoCode);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(paymentMethod, currency, amount, hashCodeNullable(promoCode));
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
    sb.append("class CharacterSlotPurchaseRequest {\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    promoCode: ").append(toIndentedString(promoCode)).append("\n");
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

