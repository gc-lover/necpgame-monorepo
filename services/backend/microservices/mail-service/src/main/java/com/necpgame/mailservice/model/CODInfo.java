package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CODInfo
 */


public class CODInfo {

  private @Nullable Integer amount;

  private @Nullable String currencyId;

  private @Nullable Boolean paid;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime paidAt;

  private @Nullable String payerId;

  private @Nullable Boolean autoAccept;

  public CODInfo amount(@Nullable Integer amount) {
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

  public CODInfo currencyId(@Nullable String currencyId) {
    this.currencyId = currencyId;
    return this;
  }

  /**
   * Get currencyId
   * @return currencyId
   */
  
  @Schema(name = "currencyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currencyId")
  public @Nullable String getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(@Nullable String currencyId) {
    this.currencyId = currencyId;
  }

  public CODInfo paid(@Nullable Boolean paid) {
    this.paid = paid;
    return this;
  }

  /**
   * Get paid
   * @return paid
   */
  
  @Schema(name = "paid", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paid")
  public @Nullable Boolean getPaid() {
    return paid;
  }

  public void setPaid(@Nullable Boolean paid) {
    this.paid = paid;
  }

  public CODInfo paidAt(@Nullable OffsetDateTime paidAt) {
    this.paidAt = paidAt;
    return this;
  }

  /**
   * Get paidAt
   * @return paidAt
   */
  @Valid 
  @Schema(name = "paidAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paidAt")
  public @Nullable OffsetDateTime getPaidAt() {
    return paidAt;
  }

  public void setPaidAt(@Nullable OffsetDateTime paidAt) {
    this.paidAt = paidAt;
  }

  public CODInfo payerId(@Nullable String payerId) {
    this.payerId = payerId;
    return this;
  }

  /**
   * Get payerId
   * @return payerId
   */
  
  @Schema(name = "payerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payerId")
  public @Nullable String getPayerId() {
    return payerId;
  }

  public void setPayerId(@Nullable String payerId) {
    this.payerId = payerId;
  }

  public CODInfo autoAccept(@Nullable Boolean autoAccept) {
    this.autoAccept = autoAccept;
    return this;
  }

  /**
   * Get autoAccept
   * @return autoAccept
   */
  
  @Schema(name = "autoAccept", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoAccept")
  public @Nullable Boolean getAutoAccept() {
    return autoAccept;
  }

  public void setAutoAccept(@Nullable Boolean autoAccept) {
    this.autoAccept = autoAccept;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CODInfo coDInfo = (CODInfo) o;
    return Objects.equals(this.amount, coDInfo.amount) &&
        Objects.equals(this.currencyId, coDInfo.currencyId) &&
        Objects.equals(this.paid, coDInfo.paid) &&
        Objects.equals(this.paidAt, coDInfo.paidAt) &&
        Objects.equals(this.payerId, coDInfo.payerId) &&
        Objects.equals(this.autoAccept, coDInfo.autoAccept);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currencyId, paid, paidAt, payerId, autoAccept);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CODInfo {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    currencyId: ").append(toIndentedString(currencyId)).append("\n");
    sb.append("    paid: ").append(toIndentedString(paid)).append("\n");
    sb.append("    paidAt: ").append(toIndentedString(paidAt)).append("\n");
    sb.append("    payerId: ").append(toIndentedString(payerId)).append("\n");
    sb.append("    autoAccept: ").append(toIndentedString(autoAccept)).append("\n");
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

