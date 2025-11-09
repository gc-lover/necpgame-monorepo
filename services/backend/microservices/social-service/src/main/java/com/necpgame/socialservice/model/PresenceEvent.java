package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.Presence;
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
 * PresenceEvent
 */


public class PresenceEvent {

  private @Nullable String playerId;

  private @Nullable Presence presence;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public PresenceEvent playerId(@Nullable String playerId) {
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

  public PresenceEvent presence(@Nullable Presence presence) {
    this.presence = presence;
    return this;
  }

  /**
   * Get presence
   * @return presence
   */
  @Valid 
  @Schema(name = "presence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("presence")
  public @Nullable Presence getPresence() {
    return presence;
  }

  public void setPresence(@Nullable Presence presence) {
    this.presence = presence;
  }

  public PresenceEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
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
    PresenceEvent presenceEvent = (PresenceEvent) o;
    return Objects.equals(this.playerId, presenceEvent.playerId) &&
        Objects.equals(this.presence, presenceEvent.presence) &&
        Objects.equals(this.occurredAt, presenceEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, presence, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PresenceEvent {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    presence: ").append(toIndentedString(presence)).append("\n");
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

