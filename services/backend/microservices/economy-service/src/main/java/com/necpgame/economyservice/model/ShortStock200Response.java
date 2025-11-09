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
 * ShortStock200Response
 */

@JsonTypeName("shortStock_200_response")

public class ShortStock200Response {

  private @Nullable String positionId;

  private @Nullable String ticker;

  private @Nullable Integer quantity;

  private @Nullable BigDecimal entryPrice;

  private @Nullable BigDecimal marginRequired;

  public ShortStock200Response positionId(@Nullable String positionId) {
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

  public ShortStock200Response ticker(@Nullable String ticker) {
    this.ticker = ticker;
    return this;
  }

  /**
   * Get ticker
   * @return ticker
   */
  
  @Schema(name = "ticker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticker")
  public @Nullable String getTicker() {
    return ticker;
  }

  public void setTicker(@Nullable String ticker) {
    this.ticker = ticker;
  }

  public ShortStock200Response quantity(@Nullable Integer quantity) {
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

  public ShortStock200Response entryPrice(@Nullable BigDecimal entryPrice) {
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

  public ShortStock200Response marginRequired(@Nullable BigDecimal marginRequired) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShortStock200Response shortStock200Response = (ShortStock200Response) o;
    return Objects.equals(this.positionId, shortStock200Response.positionId) &&
        Objects.equals(this.ticker, shortStock200Response.ticker) &&
        Objects.equals(this.quantity, shortStock200Response.quantity) &&
        Objects.equals(this.entryPrice, shortStock200Response.entryPrice) &&
        Objects.equals(this.marginRequired, shortStock200Response.marginRequired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(positionId, ticker, quantity, entryPrice, marginRequired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShortStock200Response {\n");
    sb.append("    positionId: ").append(toIndentedString(positionId)).append("\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    entryPrice: ").append(toIndentedString(entryPrice)).append("\n");
    sb.append("    marginRequired: ").append(toIndentedString(marginRequired)).append("\n");
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

