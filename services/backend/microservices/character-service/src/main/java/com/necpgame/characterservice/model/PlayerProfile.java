package com.necpgame.characterservice.model;

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
 * PlayerProfile
 */


public class PlayerProfile {

  private @Nullable String playerId;

  private @Nullable String accountId;

  private @Nullable Integer premiumCurrency;

  private @Nullable Object settings;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public PlayerProfile playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public PlayerProfile accountId(@Nullable String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account_id")
  public @Nullable String getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable String accountId) {
    this.accountId = accountId;
  }

  public PlayerProfile premiumCurrency(@Nullable Integer premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
    return this;
  }

  /**
   * NECP Coins
   * @return premiumCurrency
   */
  
  @Schema(name = "premium_currency", description = "NECP Coins", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premium_currency")
  public @Nullable Integer getPremiumCurrency() {
    return premiumCurrency;
  }

  public void setPremiumCurrency(@Nullable Integer premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
  }

  public PlayerProfile settings(@Nullable Object settings) {
    this.settings = settings;
    return this;
  }

  /**
   * Get settings
   * @return settings
   */
  
  @Schema(name = "settings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("settings")
  public @Nullable Object getSettings() {
    return settings;
  }

  public void setSettings(@Nullable Object settings) {
    this.settings = settings;
  }

  public PlayerProfile createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerProfile playerProfile = (PlayerProfile) o;
    return Objects.equals(this.playerId, playerProfile.playerId) &&
        Objects.equals(this.accountId, playerProfile.accountId) &&
        Objects.equals(this.premiumCurrency, playerProfile.premiumCurrency) &&
        Objects.equals(this.settings, playerProfile.settings) &&
        Objects.equals(this.createdAt, playerProfile.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, accountId, premiumCurrency, settings, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerProfile {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    premiumCurrency: ").append(toIndentedString(premiumCurrency)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

