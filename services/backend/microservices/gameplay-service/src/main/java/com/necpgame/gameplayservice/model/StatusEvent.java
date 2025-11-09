package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.StatusEffect;
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
 * StatusEvent
 */


public class StatusEvent {

  private @Nullable String sessionId;

  private @Nullable String participantId;

  @Valid
  private List<@Valid StatusEffect> effects = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public StatusEvent sessionId(@Nullable String sessionId) {
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

  public StatusEvent participantId(@Nullable String participantId) {
    this.participantId = participantId;
    return this;
  }

  /**
   * Get participantId
   * @return participantId
   */
  
  @Schema(name = "participantId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participantId")
  public @Nullable String getParticipantId() {
    return participantId;
  }

  public void setParticipantId(@Nullable String participantId) {
    this.participantId = participantId;
  }

  public StatusEvent effects(List<@Valid StatusEffect> effects) {
    this.effects = effects;
    return this;
  }

  public StatusEvent addEffectsItem(StatusEffect effectsItem) {
    if (this.effects == null) {
      this.effects = new ArrayList<>();
    }
    this.effects.add(effectsItem);
    return this;
  }

  /**
   * Get effects
   * @return effects
   */
  @Valid 
  @Schema(name = "effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects")
  public List<@Valid StatusEffect> getEffects() {
    return effects;
  }

  public void setEffects(List<@Valid StatusEffect> effects) {
    this.effects = effects;
  }

  public StatusEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
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
    StatusEvent statusEvent = (StatusEvent) o;
    return Objects.equals(this.sessionId, statusEvent.sessionId) &&
        Objects.equals(this.participantId, statusEvent.participantId) &&
        Objects.equals(this.effects, statusEvent.effects) &&
        Objects.equals(this.occurredAt, statusEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, participantId, effects, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusEvent {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    participantId: ").append(toIndentedString(participantId)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
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

