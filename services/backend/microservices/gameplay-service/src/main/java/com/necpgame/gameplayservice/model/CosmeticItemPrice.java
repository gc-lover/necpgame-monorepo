package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CosmeticItemPrice
 */

@JsonTypeName("CosmeticItem_price")

public class CosmeticItemPrice {

  private @Nullable Integer amount;

  /**
   * Gets or Sets currency
   */
  public enum CurrencyEnum {
    EDDIES("eddies"),
    
    SHARDS("shards"),
    
    PREMIUM_TOKENS("premium_tokens");

    private final String value;

    CurrencyEnum(String value) {
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
    public static CurrencyEnum fromValue(String value) {
      for (CurrencyEnum b : CurrencyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CurrencyEnum currency;

  private @Nullable Integer discountedAmount;

  public CosmeticItemPrice amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * minimum: 0
   * @return amount
   */
  @Min(value = 0) 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  public CosmeticItemPrice currency(@Nullable CurrencyEnum currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable CurrencyEnum getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable CurrencyEnum currency) {
    this.currency = currency;
  }

  public CosmeticItemPrice discountedAmount(@Nullable Integer discountedAmount) {
    this.discountedAmount = discountedAmount;
    return this;
  }

  /**
   * Get discountedAmount
   * minimum: 0
   * @return discountedAmount
   */
  @Min(value = 0) 
  @Schema(name = "discountedAmount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("discountedAmount")
  public @Nullable Integer getDiscountedAmount() {
    return discountedAmount;
  }

  public void setDiscountedAmount(@Nullable Integer discountedAmount) {
    this.discountedAmount = discountedAmount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticItemPrice cosmeticItemPrice = (CosmeticItemPrice) o;
    return Objects.equals(this.amount, cosmeticItemPrice.amount) &&
        Objects.equals(this.currency, cosmeticItemPrice.currency) &&
        Objects.equals(this.discountedAmount, cosmeticItemPrice.discountedAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency, discountedAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticItemPrice {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    discountedAmount: ").append(toIndentedString(discountedAmount)).append("\n");
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

