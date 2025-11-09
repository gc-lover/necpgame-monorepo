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
 * ExecutionResultTradesInner
 */

@JsonTypeName("ExecutionResult_trades_inner")

public class ExecutionResultTradesInner {

  private @Nullable String tradeId;

  private @Nullable BigDecimal price;

  private @Nullable Integer quantity;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime executedAt;

  public ExecutionResultTradesInner tradeId(@Nullable String tradeId) {
    this.tradeId = tradeId;
    return this;
  }

  /**
   * Get tradeId
   * @return tradeId
   */
  
  @Schema(name = "trade_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trade_id")
  public @Nullable String getTradeId() {
    return tradeId;
  }

  public void setTradeId(@Nullable String tradeId) {
    this.tradeId = tradeId;
  }

  public ExecutionResultTradesInner price(@Nullable BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
   */
  @Valid 
  @Schema(name = "price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public @Nullable BigDecimal getPrice() {
    return price;
  }

  public void setPrice(@Nullable BigDecimal price) {
    this.price = price;
  }

  public ExecutionResultTradesInner quantity(@Nullable Integer quantity) {
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

  public ExecutionResultTradesInner executedAt(@Nullable OffsetDateTime executedAt) {
    this.executedAt = executedAt;
    return this;
  }

  /**
   * Get executedAt
   * @return executedAt
   */
  @Valid 
  @Schema(name = "executed_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("executed_at")
  public @Nullable OffsetDateTime getExecutedAt() {
    return executedAt;
  }

  public void setExecutedAt(@Nullable OffsetDateTime executedAt) {
    this.executedAt = executedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecutionResultTradesInner executionResultTradesInner = (ExecutionResultTradesInner) o;
    return Objects.equals(this.tradeId, executionResultTradesInner.tradeId) &&
        Objects.equals(this.price, executionResultTradesInner.price) &&
        Objects.equals(this.quantity, executionResultTradesInner.quantity) &&
        Objects.equals(this.executedAt, executionResultTradesInner.executedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tradeId, price, quantity, executedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecutionResultTradesInner {\n");
    sb.append("    tradeId: ").append(toIndentedString(tradeId)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    executedAt: ").append(toIndentedString(executedAt)).append("\n");
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

