package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PlayerBattlePassProgress
 */


public class PlayerBattlePassProgress {

  private String playerId;

  private String seasonId;

  private Integer currentLevel;

  private Integer currentXP;

  private Integer totalXPEarned;

  private Boolean hasPremium;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime premiumPurchasedAt;

  @Valid
  private List<Integer> claimedFreeLevels = new ArrayList<>();

  @Valid
  private List<Integer> claimedPremiumLevels = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastXPEarnedAt;

  public PlayerBattlePassProgress() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerBattlePassProgress(String playerId, String seasonId, Integer currentLevel, Integer currentXP, Integer totalXPEarned, Boolean hasPremium) {
    this.playerId = playerId;
    this.seasonId = seasonId;
    this.currentLevel = currentLevel;
    this.currentXP = currentXP;
    this.totalXPEarned = totalXPEarned;
    this.hasPremium = hasPremium;
  }

  public PlayerBattlePassProgress playerId(String playerId) {
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

  public PlayerBattlePassProgress seasonId(String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  @NotNull 
  @Schema(name = "seasonId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("seasonId")
  public String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(String seasonId) {
    this.seasonId = seasonId;
  }

  public PlayerBattlePassProgress currentLevel(Integer currentLevel) {
    this.currentLevel = currentLevel;
    return this;
  }

  /**
   * Get currentLevel
   * @return currentLevel
   */
  @NotNull 
  @Schema(name = "currentLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentLevel")
  public Integer getCurrentLevel() {
    return currentLevel;
  }

  public void setCurrentLevel(Integer currentLevel) {
    this.currentLevel = currentLevel;
  }

  public PlayerBattlePassProgress currentXP(Integer currentXP) {
    this.currentXP = currentXP;
    return this;
  }

  /**
   * Get currentXP
   * @return currentXP
   */
  @NotNull 
  @Schema(name = "currentXP", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentXP")
  public Integer getCurrentXP() {
    return currentXP;
  }

  public void setCurrentXP(Integer currentXP) {
    this.currentXP = currentXP;
  }

  public PlayerBattlePassProgress totalXPEarned(Integer totalXPEarned) {
    this.totalXPEarned = totalXPEarned;
    return this;
  }

  /**
   * Get totalXPEarned
   * @return totalXPEarned
   */
  @NotNull 
  @Schema(name = "totalXPEarned", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalXPEarned")
  public Integer getTotalXPEarned() {
    return totalXPEarned;
  }

  public void setTotalXPEarned(Integer totalXPEarned) {
    this.totalXPEarned = totalXPEarned;
  }

  public PlayerBattlePassProgress hasPremium(Boolean hasPremium) {
    this.hasPremium = hasPremium;
    return this;
  }

  /**
   * Get hasPremium
   * @return hasPremium
   */
  @NotNull 
  @Schema(name = "hasPremium", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hasPremium")
  public Boolean getHasPremium() {
    return hasPremium;
  }

  public void setHasPremium(Boolean hasPremium) {
    this.hasPremium = hasPremium;
  }

  public PlayerBattlePassProgress premiumPurchasedAt(@Nullable OffsetDateTime premiumPurchasedAt) {
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

  public PlayerBattlePassProgress claimedFreeLevels(List<Integer> claimedFreeLevels) {
    this.claimedFreeLevels = claimedFreeLevels;
    return this;
  }

  public PlayerBattlePassProgress addClaimedFreeLevelsItem(Integer claimedFreeLevelsItem) {
    if (this.claimedFreeLevels == null) {
      this.claimedFreeLevels = new ArrayList<>();
    }
    this.claimedFreeLevels.add(claimedFreeLevelsItem);
    return this;
  }

  /**
   * Get claimedFreeLevels
   * @return claimedFreeLevels
   */
  
  @Schema(name = "claimedFreeLevels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("claimedFreeLevels")
  public List<Integer> getClaimedFreeLevels() {
    return claimedFreeLevels;
  }

  public void setClaimedFreeLevels(List<Integer> claimedFreeLevels) {
    this.claimedFreeLevels = claimedFreeLevels;
  }

  public PlayerBattlePassProgress claimedPremiumLevels(List<Integer> claimedPremiumLevels) {
    this.claimedPremiumLevels = claimedPremiumLevels;
    return this;
  }

  public PlayerBattlePassProgress addClaimedPremiumLevelsItem(Integer claimedPremiumLevelsItem) {
    if (this.claimedPremiumLevels == null) {
      this.claimedPremiumLevels = new ArrayList<>();
    }
    this.claimedPremiumLevels.add(claimedPremiumLevelsItem);
    return this;
  }

  /**
   * Get claimedPremiumLevels
   * @return claimedPremiumLevels
   */
  
  @Schema(name = "claimedPremiumLevels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("claimedPremiumLevels")
  public List<Integer> getClaimedPremiumLevels() {
    return claimedPremiumLevels;
  }

  public void setClaimedPremiumLevels(List<Integer> claimedPremiumLevels) {
    this.claimedPremiumLevels = claimedPremiumLevels;
  }

  public PlayerBattlePassProgress lastXPEarnedAt(@Nullable OffsetDateTime lastXPEarnedAt) {
    this.lastXPEarnedAt = lastXPEarnedAt;
    return this;
  }

  /**
   * Get lastXPEarnedAt
   * @return lastXPEarnedAt
   */
  @Valid 
  @Schema(name = "lastXPEarnedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastXPEarnedAt")
  public @Nullable OffsetDateTime getLastXPEarnedAt() {
    return lastXPEarnedAt;
  }

  public void setLastXPEarnedAt(@Nullable OffsetDateTime lastXPEarnedAt) {
    this.lastXPEarnedAt = lastXPEarnedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerBattlePassProgress playerBattlePassProgress = (PlayerBattlePassProgress) o;
    return Objects.equals(this.playerId, playerBattlePassProgress.playerId) &&
        Objects.equals(this.seasonId, playerBattlePassProgress.seasonId) &&
        Objects.equals(this.currentLevel, playerBattlePassProgress.currentLevel) &&
        Objects.equals(this.currentXP, playerBattlePassProgress.currentXP) &&
        Objects.equals(this.totalXPEarned, playerBattlePassProgress.totalXPEarned) &&
        Objects.equals(this.hasPremium, playerBattlePassProgress.hasPremium) &&
        Objects.equals(this.premiumPurchasedAt, playerBattlePassProgress.premiumPurchasedAt) &&
        Objects.equals(this.claimedFreeLevels, playerBattlePassProgress.claimedFreeLevels) &&
        Objects.equals(this.claimedPremiumLevels, playerBattlePassProgress.claimedPremiumLevels) &&
        Objects.equals(this.lastXPEarnedAt, playerBattlePassProgress.lastXPEarnedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, seasonId, currentLevel, currentXP, totalXPEarned, hasPremium, premiumPurchasedAt, claimedFreeLevels, claimedPremiumLevels, lastXPEarnedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerBattlePassProgress {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    currentLevel: ").append(toIndentedString(currentLevel)).append("\n");
    sb.append("    currentXP: ").append(toIndentedString(currentXP)).append("\n");
    sb.append("    totalXPEarned: ").append(toIndentedString(totalXPEarned)).append("\n");
    sb.append("    hasPremium: ").append(toIndentedString(hasPremium)).append("\n");
    sb.append("    premiumPurchasedAt: ").append(toIndentedString(premiumPurchasedAt)).append("\n");
    sb.append("    claimedFreeLevels: ").append(toIndentedString(claimedFreeLevels)).append("\n");
    sb.append("    claimedPremiumLevels: ").append(toIndentedString(claimedPremiumLevels)).append("\n");
    sb.append("    lastXPEarnedAt: ").append(toIndentedString(lastXPEarnedAt)).append("\n");
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

