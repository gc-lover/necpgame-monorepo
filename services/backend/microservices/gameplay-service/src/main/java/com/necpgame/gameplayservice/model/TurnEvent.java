package com.necpgame.gameplayservice.model;

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
 * TurnEvent
 */


public class TurnEvent {

  private @Nullable String sessionId;

  private @Nullable Integer turnNumber;

  private @Nullable String actorId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public TurnEvent sessionId(@Nullable String sessionId) {
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

  public TurnEvent turnNumber(@Nullable Integer turnNumber) {
    this.turnNumber = turnNumber;
    return this;
  }

  /**
   * Get turnNumber
   * @return turnNumber
   */
  
  @Schema(name = "turnNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("turnNumber")
  public @Nullable Integer getTurnNumber() {
    return turnNumber;
  }

  public void setTurnNumber(@Nullable Integer turnNumber) {
    this.turnNumber = turnNumber;
  }

  public TurnEvent actorId(@Nullable String actorId) {
    this.actorId = actorId;
    return this;
  }

  /**
   * Get actorId
   * @return actorId
   */
  
  @Schema(name = "actorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actorId")
  public @Nullable String getActorId() {
    return actorId;
  }

  public void setActorId(@Nullable String actorId) {
    this.actorId = actorId;
  }

  public TurnEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
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
    TurnEvent turnEvent = (TurnEvent) o;
    return Objects.equals(this.sessionId, turnEvent.sessionId) &&
        Objects.equals(this.turnNumber, turnEvent.turnNumber) &&
        Objects.equals(this.actorId, turnEvent.actorId) &&
        Objects.equals(this.occurredAt, turnEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, turnNumber, actorId, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TurnEvent {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    turnNumber: ").append(toIndentedString(turnNumber)).append("\n");
    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
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

