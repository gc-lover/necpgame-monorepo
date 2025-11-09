package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.OrderCompletionResultBonuses;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OrderCompletionResult
 */


public class OrderCompletionResult {

  private @Nullable UUID orderId;

  private @Nullable Integer paymentReleased;

  private @Nullable Integer reputationEarned;

  private @Nullable OrderCompletionResultBonuses bonuses;

  private @Nullable Boolean nextTierUnlocked;

  public OrderCompletionResult orderId(@Nullable UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @Valid 
  @Schema(name = "order_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_id")
  public @Nullable UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(@Nullable UUID orderId) {
    this.orderId = orderId;
  }

  public OrderCompletionResult paymentReleased(@Nullable Integer paymentReleased) {
    this.paymentReleased = paymentReleased;
    return this;
  }

  /**
   * Get paymentReleased
   * @return paymentReleased
   */
  
  @Schema(name = "payment_released", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payment_released")
  public @Nullable Integer getPaymentReleased() {
    return paymentReleased;
  }

  public void setPaymentReleased(@Nullable Integer paymentReleased) {
    this.paymentReleased = paymentReleased;
  }

  public OrderCompletionResult reputationEarned(@Nullable Integer reputationEarned) {
    this.reputationEarned = reputationEarned;
    return this;
  }

  /**
   * Get reputationEarned
   * @return reputationEarned
   */
  
  @Schema(name = "reputation_earned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_earned")
  public @Nullable Integer getReputationEarned() {
    return reputationEarned;
  }

  public void setReputationEarned(@Nullable Integer reputationEarned) {
    this.reputationEarned = reputationEarned;
  }

  public OrderCompletionResult bonuses(@Nullable OrderCompletionResultBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable OrderCompletionResultBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable OrderCompletionResultBonuses bonuses) {
    this.bonuses = bonuses;
  }

  public OrderCompletionResult nextTierUnlocked(@Nullable Boolean nextTierUnlocked) {
    this.nextTierUnlocked = nextTierUnlocked;
    return this;
  }

  /**
   * Get nextTierUnlocked
   * @return nextTierUnlocked
   */
  
  @Schema(name = "next_tier_unlocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("next_tier_unlocked")
  public @Nullable Boolean getNextTierUnlocked() {
    return nextTierUnlocked;
  }

  public void setNextTierUnlocked(@Nullable Boolean nextTierUnlocked) {
    this.nextTierUnlocked = nextTierUnlocked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderCompletionResult orderCompletionResult = (OrderCompletionResult) o;
    return Objects.equals(this.orderId, orderCompletionResult.orderId) &&
        Objects.equals(this.paymentReleased, orderCompletionResult.paymentReleased) &&
        Objects.equals(this.reputationEarned, orderCompletionResult.reputationEarned) &&
        Objects.equals(this.bonuses, orderCompletionResult.bonuses) &&
        Objects.equals(this.nextTierUnlocked, orderCompletionResult.nextTierUnlocked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, paymentReleased, reputationEarned, bonuses, nextTierUnlocked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderCompletionResult {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    paymentReleased: ").append(toIndentedString(paymentReleased)).append("\n");
    sb.append("    reputationEarned: ").append(toIndentedString(reputationEarned)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    nextTierUnlocked: ").append(toIndentedString(nextTierUnlocked)).append("\n");
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

