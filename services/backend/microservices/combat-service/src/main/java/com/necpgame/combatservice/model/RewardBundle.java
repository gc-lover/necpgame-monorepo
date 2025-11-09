package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.combatservice.model.RewardBundleCurrencyInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RewardBundle
 */


public class RewardBundle {

  private @Nullable Integer xp;

  @Valid
  private List<@Valid RewardBundleCurrencyInner> currency = new ArrayList<>();

  @Valid
  private List<Map<String, Object>> items = new ArrayList<>();

  private @Nullable Integer ratingChange;

  private @Nullable Integer reputation;

  public RewardBundle xp(@Nullable Integer xp) {
    this.xp = xp;
    return this;
  }

  /**
   * Get xp
   * @return xp
   */
  
  @Schema(name = "xp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xp")
  public @Nullable Integer getXp() {
    return xp;
  }

  public void setXp(@Nullable Integer xp) {
    this.xp = xp;
  }

  public RewardBundle currency(List<@Valid RewardBundleCurrencyInner> currency) {
    this.currency = currency;
    return this;
  }

  public RewardBundle addCurrencyItem(RewardBundleCurrencyInner currencyItem) {
    if (this.currency == null) {
      this.currency = new ArrayList<>();
    }
    this.currency.add(currencyItem);
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Valid 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public List<@Valid RewardBundleCurrencyInner> getCurrency() {
    return currency;
  }

  public void setCurrency(List<@Valid RewardBundleCurrencyInner> currency) {
    this.currency = currency;
  }

  public RewardBundle items(List<Map<String, Object>> items) {
    this.items = items;
    return this;
  }

  public RewardBundle addItemsItem(Map<String, Object> itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<Map<String, Object>> getItems() {
    return items;
  }

  public void setItems(List<Map<String, Object>> items) {
    this.items = items;
  }

  public RewardBundle ratingChange(@Nullable Integer ratingChange) {
    this.ratingChange = ratingChange;
    return this;
  }

  /**
   * Get ratingChange
   * @return ratingChange
   */
  
  @Schema(name = "ratingChange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ratingChange")
  public @Nullable Integer getRatingChange() {
    return ratingChange;
  }

  public void setRatingChange(@Nullable Integer ratingChange) {
    this.ratingChange = ratingChange;
  }

  public RewardBundle reputation(@Nullable Integer reputation) {
    this.reputation = reputation;
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public @Nullable Integer getReputation() {
    return reputation;
  }

  public void setReputation(@Nullable Integer reputation) {
    this.reputation = reputation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardBundle rewardBundle = (RewardBundle) o;
    return Objects.equals(this.xp, rewardBundle.xp) &&
        Objects.equals(this.currency, rewardBundle.currency) &&
        Objects.equals(this.items, rewardBundle.items) &&
        Objects.equals(this.ratingChange, rewardBundle.ratingChange) &&
        Objects.equals(this.reputation, rewardBundle.reputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(xp, currency, items, ratingChange, reputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardBundle {\n");
    sb.append("    xp: ").append(toIndentedString(xp)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    ratingChange: ").append(toIndentedString(ratingChange)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
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

