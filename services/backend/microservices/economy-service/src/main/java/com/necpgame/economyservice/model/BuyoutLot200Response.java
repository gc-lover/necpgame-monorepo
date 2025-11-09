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
 * BuyoutLot200Response
 */

@JsonTypeName("buyoutLot_200_response")

public class BuyoutLot200Response {

  private @Nullable Boolean success;

  private @Nullable String lotId;

  private @Nullable BigDecimal pricePaid;

  private @Nullable BigDecimal exchangeFee;

  public BuyoutLot200Response success(@Nullable Boolean success) {
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

  public BuyoutLot200Response lotId(@Nullable String lotId) {
    this.lotId = lotId;
    return this;
  }

  /**
   * Get lotId
   * @return lotId
   */
  
  @Schema(name = "lot_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lot_id")
  public @Nullable String getLotId() {
    return lotId;
  }

  public void setLotId(@Nullable String lotId) {
    this.lotId = lotId;
  }

  public BuyoutLot200Response pricePaid(@Nullable BigDecimal pricePaid) {
    this.pricePaid = pricePaid;
    return this;
  }

  /**
   * Get pricePaid
   * @return pricePaid
   */
  @Valid 
  @Schema(name = "price_paid", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_paid")
  public @Nullable BigDecimal getPricePaid() {
    return pricePaid;
  }

  public void setPricePaid(@Nullable BigDecimal pricePaid) {
    this.pricePaid = pricePaid;
  }

  public BuyoutLot200Response exchangeFee(@Nullable BigDecimal exchangeFee) {
    this.exchangeFee = exchangeFee;
    return this;
  }

  /**
   * Get exchangeFee
   * @return exchangeFee
   */
  @Valid 
  @Schema(name = "exchange_fee", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exchange_fee")
  public @Nullable BigDecimal getExchangeFee() {
    return exchangeFee;
  }

  public void setExchangeFee(@Nullable BigDecimal exchangeFee) {
    this.exchangeFee = exchangeFee;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuyoutLot200Response buyoutLot200Response = (BuyoutLot200Response) o;
    return Objects.equals(this.success, buyoutLot200Response.success) &&
        Objects.equals(this.lotId, buyoutLot200Response.lotId) &&
        Objects.equals(this.pricePaid, buyoutLot200Response.pricePaid) &&
        Objects.equals(this.exchangeFee, buyoutLot200Response.exchangeFee);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, lotId, pricePaid, exchangeFee);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyoutLot200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    lotId: ").append(toIndentedString(lotId)).append("\n");
    sb.append("    pricePaid: ").append(toIndentedString(pricePaid)).append("\n");
    sb.append("    exchangeFee: ").append(toIndentedString(exchangeFee)).append("\n");
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

