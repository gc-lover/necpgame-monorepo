package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.LeaderboardEntryGuild;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LeaderboardEntry {

  private @Nullable Integer rank;

  private @Nullable UUID playerId;

  private @Nullable String playerName;

  private @Nullable BigDecimal score;

  private @Nullable String scoreDisplay;

  private JsonNullable<String> activeTitle = JsonNullable.<String>undefined();

  private JsonNullable<LeaderboardEntryGuild> guild = JsonNullable.<LeaderboardEntryGuild>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public LeaderboardEntry rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public LeaderboardEntry playerId(@Nullable UUID playerId) {
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

  public LeaderboardEntry playerName(@Nullable String playerName) {
    this.playerName = playerName;
    return this;
  }

  /**
   * Get playerName
   * @return playerName
   */
  
  @Schema(name = "player_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_name")
  public @Nullable String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(@Nullable String playerName) {
    this.playerName = playerName;
  }

  public LeaderboardEntry score(@Nullable BigDecimal score) {
    this.score = score;
    return this;
  }

  /**
   * Значение (level, wealth, rating, etc.)
   * @return score
   */
  @Valid 
  @Schema(name = "score", example = "50", description = "Значение (level, wealth, rating, etc.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable BigDecimal getScore() {
    return score;
  }

  public void setScore(@Nullable BigDecimal score) {
    this.score = score;
  }

  public LeaderboardEntry scoreDisplay(@Nullable String scoreDisplay) {
    this.scoreDisplay = scoreDisplay;
    return this;
  }

  /**
   * Get scoreDisplay
   * @return scoreDisplay
   */
  
  @Schema(name = "score_display", example = "Level 50 (10,523,456 eddies)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score_display")
  public @Nullable String getScoreDisplay() {
    return scoreDisplay;
  }

  public void setScoreDisplay(@Nullable String scoreDisplay) {
    this.scoreDisplay = scoreDisplay;
  }

  public LeaderboardEntry activeTitle(String activeTitle) {
    this.activeTitle = JsonNullable.of(activeTitle);
    return this;
  }

  /**
   * Get activeTitle
   * @return activeTitle
   */
  
  @Schema(name = "active_title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_title")
  public JsonNullable<String> getActiveTitle() {
    return activeTitle;
  }

  public void setActiveTitle(JsonNullable<String> activeTitle) {
    this.activeTitle = activeTitle;
  }

  public LeaderboardEntry guild(LeaderboardEntryGuild guild) {
    this.guild = JsonNullable.of(guild);
    return this;
  }

  /**
   * Get guild
   * @return guild
   */
  @Valid 
  @Schema(name = "guild", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild")
  public JsonNullable<LeaderboardEntryGuild> getGuild() {
    return guild;
  }

  public void setGuild(JsonNullable<LeaderboardEntryGuild> guild) {
    this.guild = guild;
  }

  public LeaderboardEntry updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updated_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updated_at")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
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
        Objects.equals(this.playerName, leaderboardEntry.playerName) &&
        Objects.equals(this.score, leaderboardEntry.score) &&
        Objects.equals(this.scoreDisplay, leaderboardEntry.scoreDisplay) &&
        equalsNullable(this.activeTitle, leaderboardEntry.activeTitle) &&
        equalsNullable(this.guild, leaderboardEntry.guild) &&
        Objects.equals(this.updatedAt, leaderboardEntry.updatedAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, playerId, playerName, score, scoreDisplay, hashCodeNullable(activeTitle), hashCodeNullable(guild), updatedAt);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardEntry {\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    playerName: ").append(toIndentedString(playerName)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    scoreDisplay: ").append(toIndentedString(scoreDisplay)).append("\n");
    sb.append("    activeTitle: ").append(toIndentedString(activeTitle)).append("\n");
    sb.append("    guild: ").append(toIndentedString(guild)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

