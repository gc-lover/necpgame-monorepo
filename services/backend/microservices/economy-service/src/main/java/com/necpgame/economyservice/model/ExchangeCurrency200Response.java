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
 * ExchangeCurrency200Response
 */

@JsonTypeName("exchangeCurrency_200_response")

public class ExchangeCurrency200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal amountSent;

  private @Nullable BigDecimal amountReceived;

  private @Nullable BigDecimal exchangeRate;

  private @Nullable BigDecimal fee;

  public ExchangeCurrency200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public ExchangeCurrency200Response amountSent(@Nullable BigDecimal amountSent) {
    this.amountSent = amountSent;
    return this;
  }

  /**
   * Get amountSent
   * @return amountSent
   */
  @Valid 
  @Schema(name = "amount_sent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount_sent")
  public @Nullable BigDecimal getAmountSent() {
    return amountSent;
  }

  public void setAmountSent(@Nullable BigDecimal amountSent) {
    this.amountSent = amountSent;
  }

  public ExchangeCurrency200Response amountReceived(@Nullable BigDecimal amountReceived) {
    this.amountReceived = amountReceived;
    return this;
  }

  /**
   * Get amountReceived
   * @return amountReceived
   */
  @Valid 
  @Schema(name = "amount_received", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount_received")
  public @Nullable BigDecimal getAmountReceived() {
    return amountReceived;
  }

  public void setAmountReceived(@Nullable BigDecimal amountReceived) {
    this.amountReceived = amountReceived;
  }

  public ExchangeCurrency200Response exchangeRate(@Nullable BigDecimal exchangeRate) {
    this.exchangeRate = exchangeRate;
    return this;
  }

  /**
   * Get exchangeRate
   * @return exchangeRate
   */
  @Valid 
  @Schema(name = "exchange_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exchange_rate")
  public @Nullable BigDecimal getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(@Nullable BigDecimal exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

  public ExchangeCurrency200Response fee(@Nullable BigDecimal fee) {
    this.fee = fee;
    return this;
  }

  /**
   * Get fee
   * @return fee
   */
  @Valid 
  @Schema(name = "fee", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fee")
  public @Nullable BigDecimal getFee() {
    return fee;
  }

  public void setFee(@Nullable BigDecimal fee) {
    this.fee = fee;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExchangeCurrency200Response exchangeCurrency200Response = (ExchangeCurrency200Response) o;
    return Objects.equals(this.success, exchangeCurrency200Response.success) &&
        Objects.equals(this.amountSent, exchangeCurrency200Response.amountSent) &&
        Objects.equals(this.amountReceived, exchangeCurrency200Response.amountReceived) &&
        Objects.equals(this.exchangeRate, exchangeCurrency200Response.exchangeRate) &&
        Objects.equals(this.fee, exchangeCurrency200Response.fee);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, amountSent, amountReceived, exchangeRate, fee);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExchangeCurrency200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    amountSent: ").append(toIndentedString(amountSent)).append("\n");
    sb.append("    amountReceived: ").append(toIndentedString(amountReceived)).append("\n");
    sb.append("    exchangeRate: ").append(toIndentedString(exchangeRate)).append("\n");
    sb.append("    fee: ").append(toIndentedString(fee)).append("\n");
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

