package com.necpgame.gameplayservice.model;

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

  private Integer score;

  private @Nullable String companionId;

  private @Nullable Integer ownedRareCount;

  private @Nullable Integer collectionsCompleted;

  public LeaderboardEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LeaderboardEntry(Integer rank, String playerId, Integer score) {
    this.rank = rank;
    this.playerId = playerId;
    this.score = score;
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

  public LeaderboardEntry score(Integer score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @NotNull 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("score")
  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public LeaderboardEntry companionId(@Nullable String companionId) {
    this.companionId = companionId;
    return this;
  }

  /**
   * Get companionId
   * @return companionId
   */
  
  @Schema(name = "companionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("companionId")
  public @Nullable String getCompanionId() {
    return companionId;
  }

  public void setCompanionId(@Nullable String companionId) {
    this.companionId = companionId;
  }

  public LeaderboardEntry ownedRareCount(@Nullable Integer ownedRareCount) {
    this.ownedRareCount = ownedRareCount;
    return this;
  }

  /**
   * Get ownedRareCount
   * @return ownedRareCount
   */
  
  @Schema(name = "ownedRareCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownedRareCount")
  public @Nullable Integer getOwnedRareCount() {
    return ownedRareCount;
  }

  public void setOwnedRareCount(@Nullable Integer ownedRareCount) {
    this.ownedRareCount = ownedRareCount;
  }

  public LeaderboardEntry collectionsCompleted(@Nullable Integer collectionsCompleted) {
    this.collectionsCompleted = collectionsCompleted;
    return this;
  }

  /**
   * Get collectionsCompleted
   * @return collectionsCompleted
   */
  
  @Schema(name = "collectionsCompleted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("collectionsCompleted")
  public @Nullable Integer getCollectionsCompleted() {
    return collectionsCompleted;
  }

  public void setCollectionsCompleted(@Nullable Integer collectionsCompleted) {
    this.collectionsCompleted = collectionsCompleted;
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
        Objects.equals(this.score, leaderboardEntry.score) &&
        Objects.equals(this.companionId, leaderboardEntry.companionId) &&
        Objects.equals(this.ownedRareCount, leaderboardEntry.ownedRareCount) &&
        Objects.equals(this.collectionsCompleted, leaderboardEntry.collectionsCompleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, playerId, score, companionId, ownedRareCount, collectionsCompleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardEntry {\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    companionId: ").append(toIndentedString(companionId)).append("\n");
    sb.append("    ownedRareCount: ").append(toIndentedString(ownedRareCount)).append("\n");
    sb.append("    collectionsCompleted: ").append(toIndentedString(collectionsCompleted)).append("\n");
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

