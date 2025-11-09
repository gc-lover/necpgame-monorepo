package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * GuildWarStatus
 */


public class GuildWarStatus {

  private @Nullable String warId;

  private @Nullable String opponentGuildId;

  private @Nullable String phase;

  private @Nullable Integer score;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endAt;

  public GuildWarStatus warId(@Nullable String warId) {
    this.warId = warId;
    return this;
  }

  /**
   * Get warId
   * @return warId
   */
  
  @Schema(name = "warId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warId")
  public @Nullable String getWarId() {
    return warId;
  }

  public void setWarId(@Nullable String warId) {
    this.warId = warId;
  }

  public GuildWarStatus opponentGuildId(@Nullable String opponentGuildId) {
    this.opponentGuildId = opponentGuildId;
    return this;
  }

  /**
   * Get opponentGuildId
   * @return opponentGuildId
   */
  
  @Schema(name = "opponentGuildId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opponentGuildId")
  public @Nullable String getOpponentGuildId() {
    return opponentGuildId;
  }

  public void setOpponentGuildId(@Nullable String opponentGuildId) {
    this.opponentGuildId = opponentGuildId;
  }

  public GuildWarStatus phase(@Nullable String phase) {
    this.phase = phase;
    return this;
  }

  /**
   * Get phase
   * @return phase
   */
  
  @Schema(name = "phase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase")
  public @Nullable String getPhase() {
    return phase;
  }

  public void setPhase(@Nullable String phase) {
    this.phase = phase;
  }

  public GuildWarStatus score(@Nullable Integer score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable Integer getScore() {
    return score;
  }

  public void setScore(@Nullable Integer score) {
    this.score = score;
  }

  public GuildWarStatus startAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * Get startAt
   * @return startAt
   */
  @Valid 
  @Schema(name = "startAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startAt")
  public @Nullable OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public GuildWarStatus endAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * Get endAt
   * @return endAt
   */
  @Valid 
  @Schema(name = "endAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endAt")
  public @Nullable OffsetDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildWarStatus guildWarStatus = (GuildWarStatus) o;
    return Objects.equals(this.warId, guildWarStatus.warId) &&
        Objects.equals(this.opponentGuildId, guildWarStatus.opponentGuildId) &&
        Objects.equals(this.phase, guildWarStatus.phase) &&
        Objects.equals(this.score, guildWarStatus.score) &&
        Objects.equals(this.startAt, guildWarStatus.startAt) &&
        Objects.equals(this.endAt, guildWarStatus.endAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(warId, opponentGuildId, phase, score, startAt, endAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildWarStatus {\n");
    sb.append("    warId: ").append(toIndentedString(warId)).append("\n");
    sb.append("    opponentGuildId: ").append(toIndentedString(opponentGuildId)).append("\n");
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
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

