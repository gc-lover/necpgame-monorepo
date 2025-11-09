package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * GuildLeaderboardEntry
 */


public class GuildLeaderboardEntry {

  private @Nullable Integer rank;

  private @Nullable UUID guildId;

  private @Nullable String guildName;

  private @Nullable String guildTag;

  private @Nullable BigDecimal score;

  private @Nullable String scoreDisplay;

  private @Nullable Integer memberCount;

  private @Nullable String leaderName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public GuildLeaderboardEntry rank(@Nullable Integer rank) {
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

  public GuildLeaderboardEntry guildId(@Nullable UUID guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  @Valid 
  @Schema(name = "guild_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_id")
  public @Nullable UUID getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable UUID guildId) {
    this.guildId = guildId;
  }

  public GuildLeaderboardEntry guildName(@Nullable String guildName) {
    this.guildName = guildName;
    return this;
  }

  /**
   * Get guildName
   * @return guildName
   */
  
  @Schema(name = "guild_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_name")
  public @Nullable String getGuildName() {
    return guildName;
  }

  public void setGuildName(@Nullable String guildName) {
    this.guildName = guildName;
  }

  public GuildLeaderboardEntry guildTag(@Nullable String guildTag) {
    this.guildTag = guildTag;
    return this;
  }

  /**
   * Get guildTag
   * @return guildTag
   */
  
  @Schema(name = "guild_tag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_tag")
  public @Nullable String getGuildTag() {
    return guildTag;
  }

  public void setGuildTag(@Nullable String guildTag) {
    this.guildTag = guildTag;
  }

  public GuildLeaderboardEntry score(@Nullable BigDecimal score) {
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

  public GuildLeaderboardEntry scoreDisplay(@Nullable String scoreDisplay) {
    this.scoreDisplay = scoreDisplay;
    return this;
  }

  /**
   * Get scoreDisplay
   * @return scoreDisplay
   */
  
  @Schema(name = "score_display", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score_display")
  public @Nullable String getScoreDisplay() {
    return scoreDisplay;
  }

  public void setScoreDisplay(@Nullable String scoreDisplay) {
    this.scoreDisplay = scoreDisplay;
  }

  public GuildLeaderboardEntry memberCount(@Nullable Integer memberCount) {
    this.memberCount = memberCount;
    return this;
  }

  /**
   * Get memberCount
   * @return memberCount
   */
  
  @Schema(name = "member_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("member_count")
  public @Nullable Integer getMemberCount() {
    return memberCount;
  }

  public void setMemberCount(@Nullable Integer memberCount) {
    this.memberCount = memberCount;
  }

  public GuildLeaderboardEntry leaderName(@Nullable String leaderName) {
    this.leaderName = leaderName;
    return this;
  }

  /**
   * Get leaderName
   * @return leaderName
   */
  
  @Schema(name = "leader_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leader_name")
  public @Nullable String getLeaderName() {
    return leaderName;
  }

  public void setLeaderName(@Nullable String leaderName) {
    this.leaderName = leaderName;
  }

  public GuildLeaderboardEntry updatedAt(@Nullable OffsetDateTime updatedAt) {
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
    GuildLeaderboardEntry guildLeaderboardEntry = (GuildLeaderboardEntry) o;
    return Objects.equals(this.rank, guildLeaderboardEntry.rank) &&
        Objects.equals(this.guildId, guildLeaderboardEntry.guildId) &&
        Objects.equals(this.guildName, guildLeaderboardEntry.guildName) &&
        Objects.equals(this.guildTag, guildLeaderboardEntry.guildTag) &&
        Objects.equals(this.score, guildLeaderboardEntry.score) &&
        Objects.equals(this.scoreDisplay, guildLeaderboardEntry.scoreDisplay) &&
        Objects.equals(this.memberCount, guildLeaderboardEntry.memberCount) &&
        Objects.equals(this.leaderName, guildLeaderboardEntry.leaderName) &&
        Objects.equals(this.updatedAt, guildLeaderboardEntry.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, guildId, guildName, guildTag, score, scoreDisplay, memberCount, leaderName, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildLeaderboardEntry {\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    guildName: ").append(toIndentedString(guildName)).append("\n");
    sb.append("    guildTag: ").append(toIndentedString(guildTag)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    scoreDisplay: ").append(toIndentedString(scoreDisplay)).append("\n");
    sb.append("    memberCount: ").append(toIndentedString(memberCount)).append("\n");
    sb.append("    leaderName: ").append(toIndentedString(leaderName)).append("\n");
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

