package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetCurrencyBalance200ResponseBalancesInner
 */

@JsonTypeName("getCurrencyBalance_200_response_balances_inner")

public class GetCurrencyBalance200ResponseBalancesInner {

  private @Nullable String currencyId;

  private @Nullable BigDecimal amount;

  public GetCurrencyBalance200ResponseBalancesInner currencyId(@Nullable String currencyId) {
    this.currencyId = currencyId;
    return this;
  }

  /**
   * Get currencyId
   * @return currencyId
   */
  
  @Schema(name = "currency_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency_id")
  public @Nullable String getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(@Nullable String currencyId) {
    this.currencyId = currencyId;
  }

  public GetCurrencyBalance200ResponseBalancesInner amount(@Nullable BigDecimal amount) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCurrencyBalance200ResponseBalancesInner getCurrencyBalance200ResponseBalancesInner = (GetCurrencyBalance200ResponseBalancesInner) o;
    return Objects.equals(this.currencyId, getCurrencyBalance200ResponseBalancesInner.currencyId) &&
        Objects.equals(this.amount, getCurrencyBalance200ResponseBalancesInner.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currencyId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCurrencyBalance200ResponseBalancesInner {\n");
    sb.append("    currencyId: ").append(toIndentedString(currencyId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

