package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * BuyFuturesContract200Response
 */

@JsonTypeName("buyFuturesContract_200_response")

public class BuyFuturesContract200Response {

  private @Nullable String positionId;

  private @Nullable String contractId;

  private @Nullable Integer quantity;

  private @Nullable BigDecimal entryPrice;

  private @Nullable BigDecimal marginRequired;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expirationDate;

  public BuyFuturesContract200Response positionId(@Nullable String positionId) {
    this.positionId = positionId;
    return this;
  }

  /**
   * Get positionId
   * @return positionId
   */
  
  @Schema(name = "position_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("position_id")
  public @Nullable String getPositionId() {
    return positionId;
  }

  public void setPositionId(@Nullable String positionId) {
    this.positionId = positionId;
  }

  public BuyFuturesContract200Response contractId(@Nullable String contractId) {
    this.contractId = contractId;
    return this;
  }

  /**
   * Get contractId
   * @return contractId
   */
  
  @Schema(name = "contract_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contract_id")
  public @Nullable String getContractId() {
    return contractId;
  }

  public void setContractId(@Nullable String contractId) {
    this.contractId = contractId;
  }

  public BuyFuturesContract200Response quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public BuyFuturesContract200Response entryPrice(@Nullable BigDecimal entryPrice) {
    this.entryPrice = entryPrice;
    return this;
  }

  /**
   * Get entryPrice
   * @return entryPrice
   */
  @Valid 
  @Schema(name = "entry_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entry_price")
  public @Nullable BigDecimal getEntryPrice() {
    return entryPrice;
  }

  public void setEntryPrice(@Nullable BigDecimal entryPrice) {
    this.entryPrice = entryPrice;
  }

  public BuyFuturesContract200Response marginRequired(@Nullable BigDecimal marginRequired) {
    this.marginRequired = marginRequired;
    return this;
  }

  /**
   * Get marginRequired
   * @return marginRequired
   */
  @Valid 
  @Schema(name = "margin_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("margin_required")
  public @Nullable BigDecimal getMarginRequired() {
    return marginRequired;
  }

  public void setMarginRequired(@Nullable BigDecimal marginRequired) {
    this.marginRequired = marginRequired;
  }

  public BuyFuturesContract200Response expirationDate(@Nullable OffsetDateTime expirationDate) {
    this.expirationDate = expirationDate;
    return this;
  }

  /**
   * Get expirationDate
   * @return expirationDate
   */
  @Valid 
  @Schema(name = "expiration_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiration_date")
  public @Nullable OffsetDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(@Nullable OffsetDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuyFuturesContract200Response buyFuturesContract200Response = (BuyFuturesContract200Response) o;
    return Objects.equals(this.positionId, buyFuturesContract200Response.positionId) &&
        Objects.equals(this.contractId, buyFuturesContract200Response.contractId) &&
        Objects.equals(this.quantity, buyFuturesContract200Response.quantity) &&
        Objects.equals(this.entryPrice, buyFuturesContract200Response.entryPrice) &&
        Objects.equals(this.marginRequired, buyFuturesContract200Response.marginRequired) &&
        Objects.equals(this.expirationDate, buyFuturesContract200Response.expirationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(positionId, contractId, quantity, entryPrice, marginRequired, expirationDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyFuturesContract200Response {\n");
    sb.append("    positionId: ").append(toIndentedString(positionId)).append("\n");
    sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    entryPrice: ").append(toIndentedString(entryPrice)).append("\n");
    sb.append("    marginRequired: ").append(toIndentedString(marginRequired)).append("\n");
    sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
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

