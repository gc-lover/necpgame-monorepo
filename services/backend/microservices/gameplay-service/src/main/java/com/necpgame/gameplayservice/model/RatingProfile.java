package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ActivityType;
import com.necpgame.gameplayservice.model.Tier;
import java.time.OffsetDateTime;
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
 * RatingProfile
 */


public class RatingProfile {

  private UUID playerId;

  private ActivityType activityType;

  private String leagueId;

  private Integer rating;

  private @Nullable Integer peakRating;

  private @Nullable Tier tier;

  private @Nullable Integer division;

  private @Nullable Integer gamesPlayed;

  private @Nullable Integer wins;

  private @Nullable Integer losses;

  private @Nullable Integer draws;

  private @Nullable Float winRate;

  private @Nullable Integer streak;

  private @Nullable Integer placementMatchesRemaining;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastGameAt;

  private @Nullable Float smurfScore;

  public RatingProfile() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingProfile(UUID playerId, ActivityType activityType, String leagueId, Integer rating) {
    this.playerId = playerId;
    this.activityType = activityType;
    this.leagueId = leagueId;
    this.rating = rating;
  }

  public RatingProfile playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public RatingProfile activityType(ActivityType activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * Get activityType
   * @return activityType
   */
  @NotNull @Valid 
  @Schema(name = "activityType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activityType")
  public ActivityType getActivityType() {
    return activityType;
  }

  public void setActivityType(ActivityType activityType) {
    this.activityType = activityType;
  }

  public RatingProfile leagueId(String leagueId) {
    this.leagueId = leagueId;
    return this;
  }

  /**
   * Get leagueId
   * @return leagueId
   */
  @NotNull 
  @Schema(name = "leagueId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("leagueId")
  public String getLeagueId() {
    return leagueId;
  }

  public void setLeagueId(String leagueId) {
    this.leagueId = leagueId;
  }

  public RatingProfile rating(Integer rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Get rating
   * @return rating
   */
  @NotNull 
  @Schema(name = "rating", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rating")
  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  public RatingProfile peakRating(@Nullable Integer peakRating) {
    this.peakRating = peakRating;
    return this;
  }

  /**
   * Get peakRating
   * @return peakRating
   */
  
  @Schema(name = "peakRating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("peakRating")
  public @Nullable Integer getPeakRating() {
    return peakRating;
  }

  public void setPeakRating(@Nullable Integer peakRating) {
    this.peakRating = peakRating;
  }

  public RatingProfile tier(@Nullable Tier tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  @Valid 
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable Tier getTier() {
    return tier;
  }

  public void setTier(@Nullable Tier tier) {
    this.tier = tier;
  }

  public RatingProfile division(@Nullable Integer division) {
    this.division = division;
    return this;
  }

  /**
   * Get division
   * minimum: 1
   * maximum: 5
   * @return division
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "division", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("division")
  public @Nullable Integer getDivision() {
    return division;
  }

  public void setDivision(@Nullable Integer division) {
    this.division = division;
  }

  public RatingProfile gamesPlayed(@Nullable Integer gamesPlayed) {
    this.gamesPlayed = gamesPlayed;
    return this;
  }

  /**
   * Get gamesPlayed
   * minimum: 0
   * @return gamesPlayed
   */
  @Min(value = 0) 
  @Schema(name = "gamesPlayed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gamesPlayed")
  public @Nullable Integer getGamesPlayed() {
    return gamesPlayed;
  }

  public void setGamesPlayed(@Nullable Integer gamesPlayed) {
    this.gamesPlayed = gamesPlayed;
  }

  public RatingProfile wins(@Nullable Integer wins) {
    this.wins = wins;
    return this;
  }

  /**
   * Get wins
   * minimum: 0
   * @return wins
   */
  @Min(value = 0) 
  @Schema(name = "wins", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wins")
  public @Nullable Integer getWins() {
    return wins;
  }

  public void setWins(@Nullable Integer wins) {
    this.wins = wins;
  }

  public RatingProfile losses(@Nullable Integer losses) {
    this.losses = losses;
    return this;
  }

  /**
   * Get losses
   * minimum: 0
   * @return losses
   */
  @Min(value = 0) 
  @Schema(name = "losses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("losses")
  public @Nullable Integer getLosses() {
    return losses;
  }

  public void setLosses(@Nullable Integer losses) {
    this.losses = losses;
  }

  public RatingProfile draws(@Nullable Integer draws) {
    this.draws = draws;
    return this;
  }

  /**
   * Get draws
   * minimum: 0
   * @return draws
   */
  @Min(value = 0) 
  @Schema(name = "draws", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("draws")
  public @Nullable Integer getDraws() {
    return draws;
  }

  public void setDraws(@Nullable Integer draws) {
    this.draws = draws;
  }

  public RatingProfile winRate(@Nullable Float winRate) {
    this.winRate = winRate;
    return this;
  }

  /**
   * Get winRate
   * minimum: 0
   * maximum: 100
   * @return winRate
   */
  @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "winRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winRate")
  public @Nullable Float getWinRate() {
    return winRate;
  }

  public void setWinRate(@Nullable Float winRate) {
    this.winRate = winRate;
  }

  public RatingProfile streak(@Nullable Integer streak) {
    this.streak = streak;
    return this;
  }

  /**
   * Get streak
   * @return streak
   */
  
  @Schema(name = "streak", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("streak")
  public @Nullable Integer getStreak() {
    return streak;
  }

  public void setStreak(@Nullable Integer streak) {
    this.streak = streak;
  }

  public RatingProfile placementMatchesRemaining(@Nullable Integer placementMatchesRemaining) {
    this.placementMatchesRemaining = placementMatchesRemaining;
    return this;
  }

  /**
   * Get placementMatchesRemaining
   * minimum: 0
   * @return placementMatchesRemaining
   */
  @Min(value = 0) 
  @Schema(name = "placementMatchesRemaining", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("placementMatchesRemaining")
  public @Nullable Integer getPlacementMatchesRemaining() {
    return placementMatchesRemaining;
  }

  public void setPlacementMatchesRemaining(@Nullable Integer placementMatchesRemaining) {
    this.placementMatchesRemaining = placementMatchesRemaining;
  }

  public RatingProfile lastGameAt(@Nullable OffsetDateTime lastGameAt) {
    this.lastGameAt = lastGameAt;
    return this;
  }

  /**
   * Get lastGameAt
   * @return lastGameAt
   */
  @Valid 
  @Schema(name = "lastGameAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastGameAt")
  public @Nullable OffsetDateTime getLastGameAt() {
    return lastGameAt;
  }

  public void setLastGameAt(@Nullable OffsetDateTime lastGameAt) {
    this.lastGameAt = lastGameAt;
  }

  public RatingProfile smurfScore(@Nullable Float smurfScore) {
    this.smurfScore = smurfScore;
    return this;
  }

  /**
   * Get smurfScore
   * minimum: 0
   * maximum: 1
   * @return smurfScore
   */
  @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "smurfScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("smurfScore")
  public @Nullable Float getSmurfScore() {
    return smurfScore;
  }

  public void setSmurfScore(@Nullable Float smurfScore) {
    this.smurfScore = smurfScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingProfile ratingProfile = (RatingProfile) o;
    return Objects.equals(this.playerId, ratingProfile.playerId) &&
        Objects.equals(this.activityType, ratingProfile.activityType) &&
        Objects.equals(this.leagueId, ratingProfile.leagueId) &&
        Objects.equals(this.rating, ratingProfile.rating) &&
        Objects.equals(this.peakRating, ratingProfile.peakRating) &&
        Objects.equals(this.tier, ratingProfile.tier) &&
        Objects.equals(this.division, ratingProfile.division) &&
        Objects.equals(this.gamesPlayed, ratingProfile.gamesPlayed) &&
        Objects.equals(this.wins, ratingProfile.wins) &&
        Objects.equals(this.losses, ratingProfile.losses) &&
        Objects.equals(this.draws, ratingProfile.draws) &&
        Objects.equals(this.winRate, ratingProfile.winRate) &&
        Objects.equals(this.streak, ratingProfile.streak) &&
        Objects.equals(this.placementMatchesRemaining, ratingProfile.placementMatchesRemaining) &&
        Objects.equals(this.lastGameAt, ratingProfile.lastGameAt) &&
        Objects.equals(this.smurfScore, ratingProfile.smurfScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, activityType, leagueId, rating, peakRating, tier, division, gamesPlayed, wins, losses, draws, winRate, streak, placementMatchesRemaining, lastGameAt, smurfScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingProfile {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    leagueId: ").append(toIndentedString(leagueId)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    peakRating: ").append(toIndentedString(peakRating)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    division: ").append(toIndentedString(division)).append("\n");
    sb.append("    gamesPlayed: ").append(toIndentedString(gamesPlayed)).append("\n");
    sb.append("    wins: ").append(toIndentedString(wins)).append("\n");
    sb.append("    losses: ").append(toIndentedString(losses)).append("\n");
    sb.append("    draws: ").append(toIndentedString(draws)).append("\n");
    sb.append("    winRate: ").append(toIndentedString(winRate)).append("\n");
    sb.append("    streak: ").append(toIndentedString(streak)).append("\n");
    sb.append("    placementMatchesRemaining: ").append(toIndentedString(placementMatchesRemaining)).append("\n");
    sb.append("    lastGameAt: ").append(toIndentedString(lastGameAt)).append("\n");
    sb.append("    smurfScore: ").append(toIndentedString(smurfScore)).append("\n");
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

