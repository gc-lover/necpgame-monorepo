package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TurnState
 */


public class TurnState {

  private @Nullable Integer turnNumber;

  private @Nullable String currentActorId;

  /**
   * Gets or Sets phase
   */
  public enum PhaseEnum {
    PREPARE("PREPARE"),
    
    ACTION("ACTION"),
    
    RESOLUTION("RESOLUTION");

    private final String value;

    PhaseEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PhaseEnum fromValue(String value) {
      for (PhaseEnum b : PhaseEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PhaseEnum phase;

  private @Nullable Integer remainingTimeMs;

  public TurnState turnNumber(@Nullable Integer turnNumber) {
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

  public TurnState currentActorId(@Nullable String currentActorId) {
    this.currentActorId = currentActorId;
    return this;
  }

  /**
   * Get currentActorId
   * @return currentActorId
   */
  
  @Schema(name = "currentActorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentActorId")
  public @Nullable String getCurrentActorId() {
    return currentActorId;
  }

  public void setCurrentActorId(@Nullable String currentActorId) {
    this.currentActorId = currentActorId;
  }

  public TurnState phase(@Nullable PhaseEnum phase) {
    this.phase = phase;
    return this;
  }

  /**
   * Get phase
   * @return phase
   */
  
  @Schema(name = "phase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase")
  public @Nullable PhaseEnum getPhase() {
    return phase;
  }

  public void setPhase(@Nullable PhaseEnum phase) {
    this.phase = phase;
  }

  public TurnState remainingTimeMs(@Nullable Integer remainingTimeMs) {
    this.remainingTimeMs = remainingTimeMs;
    return this;
  }

  /**
   * Get remainingTimeMs
   * @return remainingTimeMs
   */
  
  @Schema(name = "remainingTimeMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remainingTimeMs")
  public @Nullable Integer getRemainingTimeMs() {
    return remainingTimeMs;
  }

  public void setRemainingTimeMs(@Nullable Integer remainingTimeMs) {
    this.remainingTimeMs = remainingTimeMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TurnState turnState = (TurnState) o;
    return Objects.equals(this.turnNumber, turnState.turnNumber) &&
        Objects.equals(this.currentActorId, turnState.currentActorId) &&
        Objects.equals(this.phase, turnState.phase) &&
        Objects.equals(this.remainingTimeMs, turnState.remainingTimeMs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(turnNumber, currentActorId, phase, remainingTimeMs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TurnState {\n");
    sb.append("    turnNumber: ").append(toIndentedString(turnNumber)).append("\n");
    sb.append("    currentActorId: ").append(toIndentedString(currentActorId)).append("\n");
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
    sb.append("    remainingTimeMs: ").append(toIndentedString(remainingTimeMs)).append("\n");
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

