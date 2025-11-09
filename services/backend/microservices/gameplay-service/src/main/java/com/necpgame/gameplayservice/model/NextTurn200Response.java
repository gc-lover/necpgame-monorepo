package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.Participant;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NextTurn200Response
 */

@JsonTypeName("nextTurn_200_response")

public class NextTurn200Response {

  private @Nullable Integer currentTurn;

  private @Nullable Participant activeParticipant;

  public NextTurn200Response currentTurn(@Nullable Integer currentTurn) {
    this.currentTurn = currentTurn;
    return this;
  }

  /**
   * Get currentTurn
   * @return currentTurn
   */
  
  @Schema(name = "current_turn", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_turn")
  public @Nullable Integer getCurrentTurn() {
    return currentTurn;
  }

  public void setCurrentTurn(@Nullable Integer currentTurn) {
    this.currentTurn = currentTurn;
  }

  public NextTurn200Response activeParticipant(@Nullable Participant activeParticipant) {
    this.activeParticipant = activeParticipant;
    return this;
  }

  /**
   * Get activeParticipant
   * @return activeParticipant
   */
  @Valid 
  @Schema(name = "active_participant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_participant")
  public @Nullable Participant getActiveParticipant() {
    return activeParticipant;
  }

  public void setActiveParticipant(@Nullable Participant activeParticipant) {
    this.activeParticipant = activeParticipant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NextTurn200Response nextTurn200Response = (NextTurn200Response) o;
    return Objects.equals(this.currentTurn, nextTurn200Response.currentTurn) &&
        Objects.equals(this.activeParticipant, nextTurn200Response.activeParticipant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentTurn, activeParticipant);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NextTurn200Response {\n");
    sb.append("    currentTurn: ").append(toIndentedString(currentTurn)).append("\n");
    sb.append("    activeParticipant: ").append(toIndentedString(activeParticipant)).append("\n");
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

