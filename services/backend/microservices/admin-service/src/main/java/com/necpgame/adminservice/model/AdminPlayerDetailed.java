package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * AdminPlayerDetailed
 */


public class AdminPlayerDetailed {

  private @Nullable UUID playerId;

  private @Nullable String username;

  private @Nullable String email;

  private @Nullable String status;

  private @Nullable Integer level;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastLogin;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable Integer violationsCount;

  @Valid
  private List<Object> characters = new ArrayList<>();

  private @Nullable Integer premiumCurrency;

  private @Nullable Integer totalPlaytimeHours;

  @Valid
  private List<String> ipAddresses = new ArrayList<>();

  @Valid
  private List<Object> paymentHistory = new ArrayList<>();

  public AdminPlayerDetailed playerId(@Nullable UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable UUID playerId) {
    this.playerId = playerId;
  }

  public AdminPlayerDetailed username(@Nullable String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  
  @Schema(name = "username", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("username")
  public @Nullable String getUsername() {
    return username;
  }

  public void setUsername(@Nullable String username) {
    this.username = username;
  }

  public AdminPlayerDetailed email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public AdminPlayerDetailed status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public AdminPlayerDetailed level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public AdminPlayerDetailed lastLogin(@Nullable OffsetDateTime lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  /**
   * Get lastLogin
   * @return lastLogin
   */
  @Valid 
  @Schema(name = "last_login", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_login")
  public @Nullable OffsetDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(@Nullable OffsetDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public AdminPlayerDetailed createdAt(@Nullable OffsetDateTime createdAt) {
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

  public AdminPlayerDetailed violationsCount(@Nullable Integer violationsCount) {
    this.violationsCount = violationsCount;
    return this;
  }

  /**
   * Get violationsCount
   * @return violationsCount
   */
  
  @Schema(name = "violations_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("violations_count")
  public @Nullable Integer getViolationsCount() {
    return violationsCount;
  }

  public void setViolationsCount(@Nullable Integer violationsCount) {
    this.violationsCount = violationsCount;
  }

  public AdminPlayerDetailed characters(List<Object> characters) {
    this.characters = characters;
    return this;
  }

  public AdminPlayerDetailed addCharactersItem(Object charactersItem) {
    if (this.characters == null) {
      this.characters = new ArrayList<>();
    }
    this.characters.add(charactersItem);
    return this;
  }

  /**
   * Get characters
   * @return characters
   */
  
  @Schema(name = "characters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characters")
  public List<Object> getCharacters() {
    return characters;
  }

  public void setCharacters(List<Object> characters) {
    this.characters = characters;
  }

  public AdminPlayerDetailed premiumCurrency(@Nullable Integer premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
    return this;
  }

  /**
   * Get premiumCurrency
   * @return premiumCurrency
   */
  
  @Schema(name = "premium_currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premium_currency")
  public @Nullable Integer getPremiumCurrency() {
    return premiumCurrency;
  }

  public void setPremiumCurrency(@Nullable Integer premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
  }

  public AdminPlayerDetailed totalPlaytimeHours(@Nullable Integer totalPlaytimeHours) {
    this.totalPlaytimeHours = totalPlaytimeHours;
    return this;
  }

  /**
   * Get totalPlaytimeHours
   * @return totalPlaytimeHours
   */
  
  @Schema(name = "total_playtime_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_playtime_hours")
  public @Nullable Integer getTotalPlaytimeHours() {
    return totalPlaytimeHours;
  }

  public void setTotalPlaytimeHours(@Nullable Integer totalPlaytimeHours) {
    this.totalPlaytimeHours = totalPlaytimeHours;
  }

  public AdminPlayerDetailed ipAddresses(List<String> ipAddresses) {
    this.ipAddresses = ipAddresses;
    return this;
  }

  public AdminPlayerDetailed addIpAddressesItem(String ipAddressesItem) {
    if (this.ipAddresses == null) {
      this.ipAddresses = new ArrayList<>();
    }
    this.ipAddresses.add(ipAddressesItem);
    return this;
  }

  /**
   * Get ipAddresses
   * @return ipAddresses
   */
  
  @Schema(name = "ip_addresses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ip_addresses")
  public List<String> getIpAddresses() {
    return ipAddresses;
  }

  public void setIpAddresses(List<String> ipAddresses) {
    this.ipAddresses = ipAddresses;
  }

  public AdminPlayerDetailed paymentHistory(List<Object> paymentHistory) {
    this.paymentHistory = paymentHistory;
    return this;
  }

  public AdminPlayerDetailed addPaymentHistoryItem(Object paymentHistoryItem) {
    if (this.paymentHistory == null) {
      this.paymentHistory = new ArrayList<>();
    }
    this.paymentHistory.add(paymentHistoryItem);
    return this;
  }

  /**
   * Get paymentHistory
   * @return paymentHistory
   */
  
  @Schema(name = "payment_history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payment_history")
  public List<Object> getPaymentHistory() {
    return paymentHistory;
  }

  public void setPaymentHistory(List<Object> paymentHistory) {
    this.paymentHistory = paymentHistory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminPlayerDetailed adminPlayerDetailed = (AdminPlayerDetailed) o;
    return Objects.equals(this.playerId, adminPlayerDetailed.playerId) &&
        Objects.equals(this.username, adminPlayerDetailed.username) &&
        Objects.equals(this.email, adminPlayerDetailed.email) &&
        Objects.equals(this.status, adminPlayerDetailed.status) &&
        Objects.equals(this.level, adminPlayerDetailed.level) &&
        Objects.equals(this.lastLogin, adminPlayerDetailed.lastLogin) &&
        Objects.equals(this.createdAt, adminPlayerDetailed.createdAt) &&
        Objects.equals(this.violationsCount, adminPlayerDetailed.violationsCount) &&
        Objects.equals(this.characters, adminPlayerDetailed.characters) &&
        Objects.equals(this.premiumCurrency, adminPlayerDetailed.premiumCurrency) &&
        Objects.equals(this.totalPlaytimeHours, adminPlayerDetailed.totalPlaytimeHours) &&
        Objects.equals(this.ipAddresses, adminPlayerDetailed.ipAddresses) &&
        Objects.equals(this.paymentHistory, adminPlayerDetailed.paymentHistory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, username, email, status, level, lastLogin, createdAt, violationsCount, characters, premiumCurrency, totalPlaytimeHours, ipAddresses, paymentHistory);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminPlayerDetailed {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    violationsCount: ").append(toIndentedString(violationsCount)).append("\n");
    sb.append("    characters: ").append(toIndentedString(characters)).append("\n");
    sb.append("    premiumCurrency: ").append(toIndentedString(premiumCurrency)).append("\n");
    sb.append("    totalPlaytimeHours: ").append(toIndentedString(totalPlaytimeHours)).append("\n");
    sb.append("    ipAddresses: ").append(toIndentedString(ipAddresses)).append("\n");
    sb.append("    paymentHistory: ").append(toIndentedString(paymentHistory)).append("\n");
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

