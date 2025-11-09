package com.necpgame.economyservice.model;

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
 * TradingQuota
 */


public class TradingQuota {

  private @Nullable String itemCategory;

  private @Nullable Integer maxQuantityPerWeek;

  private @Nullable Integer currentUsed;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime resetsAt;

  public TradingQuota itemCategory(@Nullable String itemCategory) {
    this.itemCategory = itemCategory;
    return this;
  }

  /**
   * Get itemCategory
   * @return itemCategory
   */
  
  @Schema(name = "item_category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_category")
  public @Nullable String getItemCategory() {
    return itemCategory;
  }

  public void setItemCategory(@Nullable String itemCategory) {
    this.itemCategory = itemCategory;
  }

  public TradingQuota maxQuantityPerWeek(@Nullable Integer maxQuantityPerWeek) {
    this.maxQuantityPerWeek = maxQuantityPerWeek;
    return this;
  }

  /**
   * Get maxQuantityPerWeek
   * @return maxQuantityPerWeek
   */
  
  @Schema(name = "max_quantity_per_week", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_quantity_per_week")
  public @Nullable Integer getMaxQuantityPerWeek() {
    return maxQuantityPerWeek;
  }

  public void setMaxQuantityPerWeek(@Nullable Integer maxQuantityPerWeek) {
    this.maxQuantityPerWeek = maxQuantityPerWeek;
  }

  public TradingQuota currentUsed(@Nullable Integer currentUsed) {
    this.currentUsed = currentUsed;
    return this;
  }

  /**
   * Get currentUsed
   * @return currentUsed
   */
  
  @Schema(name = "current_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_used")
  public @Nullable Integer getCurrentUsed() {
    return currentUsed;
  }

  public void setCurrentUsed(@Nullable Integer currentUsed) {
    this.currentUsed = currentUsed;
  }

  public TradingQuota resetsAt(@Nullable OffsetDateTime resetsAt) {
    this.resetsAt = resetsAt;
    return this;
  }

  /**
   * Get resetsAt
   * @return resetsAt
   */
  @Valid 
  @Schema(name = "resets_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resets_at")
  public @Nullable OffsetDateTime getResetsAt() {
    return resetsAt;
  }

  public void setResetsAt(@Nullable OffsetDateTime resetsAt) {
    this.resetsAt = resetsAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradingQuota tradingQuota = (TradingQuota) o;
    return Objects.equals(this.itemCategory, tradingQuota.itemCategory) &&
        Objects.equals(this.maxQuantityPerWeek, tradingQuota.maxQuantityPerWeek) &&
        Objects.equals(this.currentUsed, tradingQuota.currentUsed) &&
        Objects.equals(this.resetsAt, tradingQuota.resetsAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemCategory, maxQuantityPerWeek, currentUsed, resetsAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradingQuota {\n");
    sb.append("    itemCategory: ").append(toIndentedString(itemCategory)).append("\n");
    sb.append("    maxQuantityPerWeek: ").append(toIndentedString(maxQuantityPerWeek)).append("\n");
    sb.append("    currentUsed: ").append(toIndentedString(currentUsed)).append("\n");
    sb.append("    resetsAt: ").append(toIndentedString(resetsAt)).append("\n");
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

