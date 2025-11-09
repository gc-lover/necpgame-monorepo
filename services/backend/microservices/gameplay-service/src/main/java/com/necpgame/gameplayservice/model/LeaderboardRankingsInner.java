package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LeaderboardRankingsInner
 */

@JsonTypeName("Leaderboard_rankings_inner")

public class LeaderboardRankingsInner {

  private @Nullable Integer rank;

  private @Nullable String accountId;

  private @Nullable String characterName;

  private @Nullable BigDecimal score;

  private @Nullable String propertyClass;

  private @Nullable Integer level;

  public LeaderboardRankingsInner rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public LeaderboardRankingsInner accountId(@Nullable String accountId) {
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

  public LeaderboardRankingsInner characterName(@Nullable String characterName) {
    this.characterName = characterName;
    return this;
  }

  /**
   * Get characterName
   * @return characterName
   */
  
  @Schema(name = "character_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_name")
  public @Nullable String getCharacterName() {
    return characterName;
  }

  public void setCharacterName(@Nullable String characterName) {
    this.characterName = characterName;
  }

  public LeaderboardRankingsInner score(@Nullable BigDecimal score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @Valid 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable BigDecimal getScore() {
    return score;
  }

  public void setScore(@Nullable BigDecimal score) {
    this.score = score;
  }

  public LeaderboardRankingsInner propertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * Get propertyClass
   * @return propertyClass
   */
  
  @Schema(name = "class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class")
  public @Nullable String getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
  }

  public LeaderboardRankingsInner level(@Nullable Integer level) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaderboardRankingsInner leaderboardRankingsInner = (LeaderboardRankingsInner) o;
    return Objects.equals(this.rank, leaderboardRankingsInner.rank) &&
        Objects.equals(this.accountId, leaderboardRankingsInner.accountId) &&
        Objects.equals(this.characterName, leaderboardRankingsInner.characterName) &&
        Objects.equals(this.score, leaderboardRankingsInner.score) &&
        Objects.equals(this.propertyClass, leaderboardRankingsInner.propertyClass) &&
        Objects.equals(this.level, leaderboardRankingsInner.level);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, accountId, characterName, score, propertyClass, level);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardRankingsInner {\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    characterName: ").append(toIndentedString(characterName)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
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

