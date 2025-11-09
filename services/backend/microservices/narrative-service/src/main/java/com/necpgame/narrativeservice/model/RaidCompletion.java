package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.RaidCompletionRewards;
import java.math.BigDecimal;
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
 * RaidCompletion
 */


public class RaidCompletion {

  private @Nullable String raidId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime completedAt;

  private @Nullable BigDecimal duration;

  private @Nullable Integer playersParticipated;

  private @Nullable RaidCompletionRewards rewards;

  public RaidCompletion raidId(@Nullable String raidId) {
    this.raidId = raidId;
    return this;
  }

  /**
   * Get raidId
   * @return raidId
   */
  
  @Schema(name = "raid_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("raid_id")
  public @Nullable String getRaidId() {
    return raidId;
  }

  public void setRaidId(@Nullable String raidId) {
    this.raidId = raidId;
  }

  public RaidCompletion completedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
    return this;
  }

  /**
   * Get completedAt
   * @return completedAt
   */
  @Valid 
  @Schema(name = "completed_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed_at")
  public @Nullable OffsetDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
  }

  public RaidCompletion duration(@Nullable BigDecimal duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Get duration
   * @return duration
   */
  @Valid 
  @Schema(name = "duration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable BigDecimal getDuration() {
    return duration;
  }

  public void setDuration(@Nullable BigDecimal duration) {
    this.duration = duration;
  }

  public RaidCompletion playersParticipated(@Nullable Integer playersParticipated) {
    this.playersParticipated = playersParticipated;
    return this;
  }

  /**
   * Get playersParticipated
   * @return playersParticipated
   */
  
  @Schema(name = "players_participated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("players_participated")
  public @Nullable Integer getPlayersParticipated() {
    return playersParticipated;
  }

  public void setPlayersParticipated(@Nullable Integer playersParticipated) {
    this.playersParticipated = playersParticipated;
  }

  public RaidCompletion rewards(@Nullable RaidCompletionRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable RaidCompletionRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable RaidCompletionRewards rewards) {
    this.rewards = rewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RaidCompletion raidCompletion = (RaidCompletion) o;
    return Objects.equals(this.raidId, raidCompletion.raidId) &&
        Objects.equals(this.completedAt, raidCompletion.completedAt) &&
        Objects.equals(this.duration, raidCompletion.duration) &&
        Objects.equals(this.playersParticipated, raidCompletion.playersParticipated) &&
        Objects.equals(this.rewards, raidCompletion.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raidId, completedAt, duration, playersParticipated, rewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RaidCompletion {\n");
    sb.append("    raidId: ").append(toIndentedString(raidId)).append("\n");
    sb.append("    completedAt: ").append(toIndentedString(completedAt)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    playersParticipated: ").append(toIndentedString(playersParticipated)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
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

