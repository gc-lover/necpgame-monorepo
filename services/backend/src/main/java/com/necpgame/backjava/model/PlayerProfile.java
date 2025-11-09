package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
<<<<<<< HEAD
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
=======
import com.necpgame.backjava.model.PlayerProfileSettings;
import com.necpgame.backjava.model.PlayerProfileSocial;
import java.util.UUID;
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
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

<<<<<<< HEAD
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
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
=======

public class PlayerProfile {

  private UUID accountId;

  private @Nullable Integer premiumCurrency;

  private @Nullable Integer totalPlaytimeSeconds;

  private String language;

  private String timezone;

  private PlayerProfileSettings settings;

  private PlayerProfileSocial social;

  public PlayerProfile() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerProfile(UUID accountId, String language, String timezone, PlayerProfileSettings settings, PlayerProfileSocial social) {
    this.accountId = accountId;
    this.language = language;
    this.timezone = timezone;
    this.settings = settings;
    this.social = social;
  }

  public PlayerProfile accountId(UUID accountId) {
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
<<<<<<< HEAD
  
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account_id")
  public @Nullable String getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable String accountId) {
=======
  @NotNull @Valid 
  @Schema(name = "accountId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accountId")
  public UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(UUID accountId) {
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
    this.accountId = accountId;
  }

  public PlayerProfile premiumCurrency(@Nullable Integer premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
    return this;
  }

  /**
<<<<<<< HEAD
   * NECP Coins
   * @return premiumCurrency
   */
  
  @Schema(name = "premium_currency", description = "NECP Coins", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premium_currency")
=======
   * Get premiumCurrency
   * minimum: 0
   * @return premiumCurrency
   */
  @Min(value = 0) 
  @Schema(name = "premiumCurrency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premiumCurrency")
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
  public @Nullable Integer getPremiumCurrency() {
    return premiumCurrency;
  }

  public void setPremiumCurrency(@Nullable Integer premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
  }

<<<<<<< HEAD
  public PlayerProfile settings(@Nullable Object settings) {
=======
  public PlayerProfile totalPlaytimeSeconds(@Nullable Integer totalPlaytimeSeconds) {
    this.totalPlaytimeSeconds = totalPlaytimeSeconds;
    return this;
  }

  /**
   * Get totalPlaytimeSeconds
   * minimum: 0
   * @return totalPlaytimeSeconds
   */
  @Min(value = 0) 
  @Schema(name = "totalPlaytimeSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPlaytimeSeconds")
  public @Nullable Integer getTotalPlaytimeSeconds() {
    return totalPlaytimeSeconds;
  }

  public void setTotalPlaytimeSeconds(@Nullable Integer totalPlaytimeSeconds) {
    this.totalPlaytimeSeconds = totalPlaytimeSeconds;
  }

  public PlayerProfile language(String language) {
    this.language = language;
    return this;
  }

  /**
   * Get language
   * @return language
   */
  @NotNull 
  @Schema(name = "language", example = "ru", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("language")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public PlayerProfile timezone(String timezone) {
    this.timezone = timezone;
    return this;
  }

  /**
   * Get timezone
   * @return timezone
   */
  @NotNull 
  @Schema(name = "timezone", example = "Europe/Moscow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timezone")
  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public PlayerProfile settings(PlayerProfileSettings settings) {
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
    this.settings = settings;
    return this;
  }

  /**
   * Get settings
   * @return settings
   */
<<<<<<< HEAD
  
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
=======
  @NotNull @Valid 
  @Schema(name = "settings", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("settings")
  public PlayerProfileSettings getSettings() {
    return settings;
  }

  public void setSettings(PlayerProfileSettings settings) {
    this.settings = settings;
  }

  public PlayerProfile social(PlayerProfileSocial social) {
    this.social = social;
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
    return this;
  }

  /**
<<<<<<< HEAD
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
=======
   * Get social
   * @return social
   */
  @NotNull @Valid 
  @Schema(name = "social", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("social")
  public PlayerProfileSocial getSocial() {
    return social;
  }

  public void setSocial(PlayerProfileSocial social) {
    this.social = social;
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
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
<<<<<<< HEAD
    return Objects.equals(this.playerId, playerProfile.playerId) &&
        Objects.equals(this.accountId, playerProfile.accountId) &&
        Objects.equals(this.premiumCurrency, playerProfile.premiumCurrency) &&
        Objects.equals(this.settings, playerProfile.settings) &&
        Objects.equals(this.createdAt, playerProfile.createdAt);
=======
    return Objects.equals(this.accountId, playerProfile.accountId) &&
        Objects.equals(this.premiumCurrency, playerProfile.premiumCurrency) &&
        Objects.equals(this.totalPlaytimeSeconds, playerProfile.totalPlaytimeSeconds) &&
        Objects.equals(this.language, playerProfile.language) &&
        Objects.equals(this.timezone, playerProfile.timezone) &&
        Objects.equals(this.settings, playerProfile.settings) &&
        Objects.equals(this.social, playerProfile.social);
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
  }

  @Override
  public int hashCode() {
<<<<<<< HEAD
    return Objects.hash(playerId, accountId, premiumCurrency, settings, createdAt);
=======
    return Objects.hash(accountId, premiumCurrency, totalPlaytimeSeconds, language, timezone, settings, social);
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerProfile {\n");
<<<<<<< HEAD
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    premiumCurrency: ").append(toIndentedString(premiumCurrency)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
=======
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    premiumCurrency: ").append(toIndentedString(premiumCurrency)).append("\n");
    sb.append("    totalPlaytimeSeconds: ").append(toIndentedString(totalPlaytimeSeconds)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    social: ").append(toIndentedString(social)).append("\n");
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
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

