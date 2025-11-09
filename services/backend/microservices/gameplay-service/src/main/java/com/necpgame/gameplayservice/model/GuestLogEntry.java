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
 * GuestLogEntry
 */


public class GuestLogEntry {

  private @Nullable String playerId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime joinedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime leftAt;

  @Valid
  private List<String> actions = new ArrayList<>();

  public GuestLogEntry playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public GuestLogEntry joinedAt(@Nullable OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
    return this;
  }

  /**
   * Get joinedAt
   * @return joinedAt
   */
  @Valid 
  @Schema(name = "joinedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("joinedAt")
  public @Nullable OffsetDateTime getJoinedAt() {
    return joinedAt;
  }

  public void setJoinedAt(@Nullable OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
  }

  public GuestLogEntry leftAt(@Nullable OffsetDateTime leftAt) {
    this.leftAt = leftAt;
    return this;
  }

  /**
   * Get leftAt
   * @return leftAt
   */
  @Valid 
  @Schema(name = "leftAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leftAt")
  public @Nullable OffsetDateTime getLeftAt() {
    return leftAt;
  }

  public void setLeftAt(@Nullable OffsetDateTime leftAt) {
    this.leftAt = leftAt;
  }

  public GuestLogEntry actions(List<String> actions) {
    this.actions = actions;
    return this;
  }

  public GuestLogEntry addActionsItem(String actionsItem) {
    if (this.actions == null) {
      this.actions = new ArrayList<>();
    }
    this.actions.add(actionsItem);
    return this;
  }

  /**
   * Get actions
   * @return actions
   */
  
  @Schema(name = "actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actions")
  public List<String> getActions() {
    return actions;
  }

  public void setActions(List<String> actions) {
    this.actions = actions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestLogEntry guestLogEntry = (GuestLogEntry) o;
    return Objects.equals(this.playerId, guestLogEntry.playerId) &&
        Objects.equals(this.joinedAt, guestLogEntry.joinedAt) &&
        Objects.equals(this.leftAt, guestLogEntry.leftAt) &&
        Objects.equals(this.actions, guestLogEntry.actions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, joinedAt, leftAt, actions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestLogEntry {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    joinedAt: ").append(toIndentedString(joinedAt)).append("\n");
    sb.append("    leftAt: ").append(toIndentedString(leftAt)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
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

