package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.RewardBundle;
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
 * SessionCompleteEvent
 */


public class SessionCompleteEvent {

  private @Nullable String sessionId;

  private @Nullable String winningTeamId;

  private @Nullable RewardBundle rewards;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public SessionCompleteEvent sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionId")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public SessionCompleteEvent winningTeamId(@Nullable String winningTeamId) {
    this.winningTeamId = winningTeamId;
    return this;
  }

  /**
   * Get winningTeamId
   * @return winningTeamId
   */
  
  @Schema(name = "winningTeamId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winningTeamId")
  public @Nullable String getWinningTeamId() {
    return winningTeamId;
  }

  public void setWinningTeamId(@Nullable String winningTeamId) {
    this.winningTeamId = winningTeamId;
  }

  public SessionCompleteEvent rewards(@Nullable RewardBundle rewards) {
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
  public @Nullable RewardBundle getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable RewardBundle rewards) {
    this.rewards = rewards;
  }

  public SessionCompleteEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("occurredAt")
  public @Nullable OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionCompleteEvent sessionCompleteEvent = (SessionCompleteEvent) o;
    return Objects.equals(this.sessionId, sessionCompleteEvent.sessionId) &&
        Objects.equals(this.winningTeamId, sessionCompleteEvent.winningTeamId) &&
        Objects.equals(this.rewards, sessionCompleteEvent.rewards) &&
        Objects.equals(this.occurredAt, sessionCompleteEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, winningTeamId, rewards, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionCompleteEvent {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    winningTeamId: ").append(toIndentedString(winningTeamId)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
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

