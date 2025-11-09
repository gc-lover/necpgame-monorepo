package com.necpgame.gameplayservice.model;

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
 * PremiumPurchaseResponse
 */


public class PremiumPurchaseResponse {

  private @Nullable String playerId;

  private @Nullable String seasonId;

  private @Nullable Boolean hasPremium;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime premiumPurchasedAt;

  private @Nullable Integer remainingCurrency;

  public PremiumPurchaseResponse playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public PremiumPurchaseResponse seasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  
  @Schema(name = "seasonId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasonId")
  public @Nullable String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
  }

  public PremiumPurchaseResponse hasPremium(@Nullable Boolean hasPremium) {
    this.hasPremium = hasPremium;
    return this;
  }

  /**
   * Get hasPremium
   * @return hasPremium
   */
  
  @Schema(name = "hasPremium", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hasPremium")
  public @Nullable Boolean getHasPremium() {
    return hasPremium;
  }

  public void setHasPremium(@Nullable Boolean hasPremium) {
    this.hasPremium = hasPremium;
  }

  public PremiumPurchaseResponse premiumPurchasedAt(@Nullable OffsetDateTime premiumPurchasedAt) {
    this.premiumPurchasedAt = premiumPurchasedAt;
    return this;
  }

  /**
   * Get premiumPurchasedAt
   * @return premiumPurchasedAt
   */
  @Valid 
  @Schema(name = "premiumPurchasedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premiumPurchasedAt")
  public @Nullable OffsetDateTime getPremiumPurchasedAt() {
    return premiumPurchasedAt;
  }

  public void setPremiumPurchasedAt(@Nullable OffsetDateTime premiumPurchasedAt) {
    this.premiumPurchasedAt = premiumPurchasedAt;
  }

  public PremiumPurchaseResponse remainingCurrency(@Nullable Integer remainingCurrency) {
    this.remainingCurrency = remainingCurrency;
    return this;
  }

  /**
   * Get remainingCurrency
   * @return remainingCurrency
   */
  
  @Schema(name = "remainingCurrency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remainingCurrency")
  public @Nullable Integer getRemainingCurrency() {
    return remainingCurrency;
  }

  public void setRemainingCurrency(@Nullable Integer remainingCurrency) {
    this.remainingCurrency = remainingCurrency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PremiumPurchaseResponse premiumPurchaseResponse = (PremiumPurchaseResponse) o;
    return Objects.equals(this.playerId, premiumPurchaseResponse.playerId) &&
        Objects.equals(this.seasonId, premiumPurchaseResponse.seasonId) &&
        Objects.equals(this.hasPremium, premiumPurchaseResponse.hasPremium) &&
        Objects.equals(this.premiumPurchasedAt, premiumPurchaseResponse.premiumPurchasedAt) &&
        Objects.equals(this.remainingCurrency, premiumPurchaseResponse.remainingCurrency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, seasonId, hasPremium, premiumPurchasedAt, remainingCurrency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PremiumPurchaseResponse {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    hasPremium: ").append(toIndentedString(hasPremium)).append("\n");
    sb.append("    premiumPurchasedAt: ").append(toIndentedString(premiumPurchasedAt)).append("\n");
    sb.append("    remainingCurrency: ").append(toIndentedString(remainingCurrency)).append("\n");
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

