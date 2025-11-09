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
 * PremiumStatusResponse
 */


public class PremiumStatusResponse {

  private @Nullable String playerId;

  private @Nullable String seasonId;

  private @Nullable Boolean hasPremium;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime premiumPurchasedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable String entitlementSource;

  public PremiumStatusResponse playerId(@Nullable String playerId) {
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

  public PremiumStatusResponse seasonId(@Nullable String seasonId) {
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

  public PremiumStatusResponse hasPremium(@Nullable Boolean hasPremium) {
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

  public PremiumStatusResponse premiumPurchasedAt(@Nullable OffsetDateTime premiumPurchasedAt) {
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

  public PremiumStatusResponse expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public PremiumStatusResponse entitlementSource(@Nullable String entitlementSource) {
    this.entitlementSource = entitlementSource;
    return this;
  }

  /**
   * Get entitlementSource
   * @return entitlementSource
   */
  
  @Schema(name = "entitlementSource", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entitlementSource")
  public @Nullable String getEntitlementSource() {
    return entitlementSource;
  }

  public void setEntitlementSource(@Nullable String entitlementSource) {
    this.entitlementSource = entitlementSource;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PremiumStatusResponse premiumStatusResponse = (PremiumStatusResponse) o;
    return Objects.equals(this.playerId, premiumStatusResponse.playerId) &&
        Objects.equals(this.seasonId, premiumStatusResponse.seasonId) &&
        Objects.equals(this.hasPremium, premiumStatusResponse.hasPremium) &&
        Objects.equals(this.premiumPurchasedAt, premiumStatusResponse.premiumPurchasedAt) &&
        Objects.equals(this.expiresAt, premiumStatusResponse.expiresAt) &&
        Objects.equals(this.entitlementSource, premiumStatusResponse.entitlementSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, seasonId, hasPremium, premiumPurchasedAt, expiresAt, entitlementSource);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PremiumStatusResponse {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    hasPremium: ").append(toIndentedString(hasPremium)).append("\n");
    sb.append("    premiumPurchasedAt: ").append(toIndentedString(premiumPurchasedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    entitlementSource: ").append(toIndentedString(entitlementSource)).append("\n");
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

