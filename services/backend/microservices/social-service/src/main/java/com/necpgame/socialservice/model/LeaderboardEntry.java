package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LeaderboardEntry
 */


public class LeaderboardEntry {

  private Integer rank;

  private String playerId;

  private Integer referralsCount;

  private @Nullable Integer activeReferrals;

  private @Nullable Integer rewardsEarned;

  private @Nullable String region;

  public LeaderboardEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LeaderboardEntry(Integer rank, String playerId, Integer referralsCount) {
    this.rank = rank;
    this.playerId = playerId;
    this.referralsCount = referralsCount;
  }

  public LeaderboardEntry rank(Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  @NotNull 
  @Schema(name = "rank", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rank")
  public Integer getRank() {
    return rank;
  }

  public void setRank(Integer rank) {
    this.rank = rank;
  }

  public LeaderboardEntry playerId(String playerId) {
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

  public LeaderboardEntry referralsCount(Integer referralsCount) {
    this.referralsCount = referralsCount;
    return this;
  }

  /**
   * Get referralsCount
   * @return referralsCount
   */
  @NotNull 
  @Schema(name = "referralsCount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("referralsCount")
  public Integer getReferralsCount() {
    return referralsCount;
  }

  public void setReferralsCount(Integer referralsCount) {
    this.referralsCount = referralsCount;
  }

  public LeaderboardEntry activeReferrals(@Nullable Integer activeReferrals) {
    this.activeReferrals = activeReferrals;
    return this;
  }

  /**
   * Get activeReferrals
   * @return activeReferrals
   */
  
  @Schema(name = "activeReferrals", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeReferrals")
  public @Nullable Integer getActiveReferrals() {
    return activeReferrals;
  }

  public void setActiveReferrals(@Nullable Integer activeReferrals) {
    this.activeReferrals = activeReferrals;
  }

  public LeaderboardEntry rewardsEarned(@Nullable Integer rewardsEarned) {
    this.rewardsEarned = rewardsEarned;
    return this;
  }

  /**
   * Get rewardsEarned
   * @return rewardsEarned
   */
  
  @Schema(name = "rewardsEarned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardsEarned")
  public @Nullable Integer getRewardsEarned() {
    return rewardsEarned;
  }

  public void setRewardsEarned(@Nullable Integer rewardsEarned) {
    this.rewardsEarned = rewardsEarned;
  }

  public LeaderboardEntry region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaderboardEntry leaderboardEntry = (LeaderboardEntry) o;
    return Objects.equals(this.rank, leaderboardEntry.rank) &&
        Objects.equals(this.playerId, leaderboardEntry.playerId) &&
        Objects.equals(this.referralsCount, leaderboardEntry.referralsCount) &&
        Objects.equals(this.activeReferrals, leaderboardEntry.activeReferrals) &&
        Objects.equals(this.rewardsEarned, leaderboardEntry.rewardsEarned) &&
        Objects.equals(this.region, leaderboardEntry.region);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, playerId, referralsCount, activeReferrals, rewardsEarned, region);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardEntry {\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    referralsCount: ").append(toIndentedString(referralsCount)).append("\n");
    sb.append("    activeReferrals: ").append(toIndentedString(activeReferrals)).append("\n");
    sb.append("    rewardsEarned: ").append(toIndentedString(rewardsEarned)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
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

