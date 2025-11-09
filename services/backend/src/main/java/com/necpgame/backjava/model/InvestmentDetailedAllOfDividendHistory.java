package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * InvestmentDetailedAllOfDividendHistory
 */

@JsonTypeName("InvestmentDetailed_allOf_dividend_history")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class InvestmentDetailedAllOfDividendHistory {

  private @Nullable Integer amount;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime paidAt;

  public InvestmentDetailedAllOfDividendHistory amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  public InvestmentDetailedAllOfDividendHistory paidAt(@Nullable OffsetDateTime paidAt) {
    this.paidAt = paidAt;
    return this;
  }

  /**
   * Get paidAt
   * @return paidAt
   */
  @Valid 
  @Schema(name = "paid_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paid_at")
  public @Nullable OffsetDateTime getPaidAt() {
    return paidAt;
  }

  public void setPaidAt(@Nullable OffsetDateTime paidAt) {
    this.paidAt = paidAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvestmentDetailedAllOfDividendHistory investmentDetailedAllOfDividendHistory = (InvestmentDetailedAllOfDividendHistory) o;
    return Objects.equals(this.amount, investmentDetailedAllOfDividendHistory.amount) &&
        Objects.equals(this.paidAt, investmentDetailedAllOfDividendHistory.paidAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, paidAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InvestmentDetailedAllOfDividendHistory {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    paidAt: ").append(toIndentedString(paidAt)).append("\n");
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

