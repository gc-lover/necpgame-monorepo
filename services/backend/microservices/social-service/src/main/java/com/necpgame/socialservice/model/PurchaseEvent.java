package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * PurchaseEvent
 */


public class PurchaseEvent {

  private String playerId;

  private String purchaseId;

  private @Nullable BigDecimal amount;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public PurchaseEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PurchaseEvent(String playerId, String purchaseId) {
    this.playerId = playerId;
    this.purchaseId = purchaseId;
  }

  public PurchaseEvent playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public PurchaseEvent purchaseId(String purchaseId) {
    this.purchaseId = purchaseId;
    return this;
  }

  /**
   * Get purchaseId
   * @return purchaseId
   */
  @NotNull 
  @Schema(name = "purchaseId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("purchaseId")
  public String getPurchaseId() {
    return purchaseId;
  }

  public void setPurchaseId(String purchaseId) {
    this.purchaseId = purchaseId;
  }

  public PurchaseEvent amount(@Nullable BigDecimal amount) {
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

  public PurchaseEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("occurredAt")
  public @Nullable OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurchaseEvent purchaseEvent = (PurchaseEvent) o;
    return Objects.equals(this.playerId, purchaseEvent.playerId) &&
        Objects.equals(this.purchaseId, purchaseEvent.purchaseId) &&
        Objects.equals(this.amount, purchaseEvent.amount) &&
        Objects.equals(this.occurredAt, purchaseEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, purchaseId, amount, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseEvent {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    purchaseId: ").append(toIndentedString(purchaseId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
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

