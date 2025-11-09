package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TradeResult
 */


public class TradeResult {

  private @Nullable Boolean success;

  private @Nullable String itemId;

  private @Nullable Integer quantity;

  private @Nullable BigDecimal totalPrice;

  private @Nullable String currencyUsed;

  private @Nullable BigDecimal reputationChange;

  private @Nullable BigDecimal experienceGained;

  public TradeResult success(@Nullable Boolean success) {
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

  public TradeResult itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public TradeResult quantity(@Nullable Integer quantity) {
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

  public TradeResult totalPrice(@Nullable BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  /**
   * Get totalPrice
   * @return totalPrice
   */
  @Valid 
  @Schema(name = "total_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_price")
  public @Nullable BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(@Nullable BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public TradeResult currencyUsed(@Nullable String currencyUsed) {
    this.currencyUsed = currencyUsed;
    return this;
  }

  /**
   * Get currencyUsed
   * @return currencyUsed
   */
  
  @Schema(name = "currency_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency_used")
  public @Nullable String getCurrencyUsed() {
    return currencyUsed;
  }

  public void setCurrencyUsed(@Nullable String currencyUsed) {
    this.currencyUsed = currencyUsed;
  }

  public TradeResult reputationChange(@Nullable BigDecimal reputationChange) {
    this.reputationChange = reputationChange;
    return this;
  }

  /**
   * Get reputationChange
   * @return reputationChange
   */
  @Valid 
  @Schema(name = "reputation_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_change")
  public @Nullable BigDecimal getReputationChange() {
    return reputationChange;
  }

  public void setReputationChange(@Nullable BigDecimal reputationChange) {
    this.reputationChange = reputationChange;
  }

  public TradeResult experienceGained(@Nullable BigDecimal experienceGained) {
    this.experienceGained = experienceGained;
    return this;
  }

  /**
   * Get experienceGained
   * @return experienceGained
   */
  @Valid 
  @Schema(name = "experience_gained", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_gained")
  public @Nullable BigDecimal getExperienceGained() {
    return experienceGained;
  }

  public void setExperienceGained(@Nullable BigDecimal experienceGained) {
    this.experienceGained = experienceGained;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradeResult tradeResult = (TradeResult) o;
    return Objects.equals(this.success, tradeResult.success) &&
        Objects.equals(this.itemId, tradeResult.itemId) &&
        Objects.equals(this.quantity, tradeResult.quantity) &&
        Objects.equals(this.totalPrice, tradeResult.totalPrice) &&
        Objects.equals(this.currencyUsed, tradeResult.currencyUsed) &&
        Objects.equals(this.reputationChange, tradeResult.reputationChange) &&
        Objects.equals(this.experienceGained, tradeResult.experienceGained);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, itemId, quantity, totalPrice, currencyUsed, reputationChange, experienceGained);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradeResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    totalPrice: ").append(toIndentedString(totalPrice)).append("\n");
    sb.append("    currencyUsed: ").append(toIndentedString(currencyUsed)).append("\n");
    sb.append("    reputationChange: ").append(toIndentedString(reputationChange)).append("\n");
    sb.append("    experienceGained: ").append(toIndentedString(experienceGained)).append("\n");
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

