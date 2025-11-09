package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateDialogueRequest
 */

@JsonTypeName("generateDialogue_request")

public class GenerateDialogueRequest {

  private @Nullable String npcId;

  private @Nullable String context;

  private @Nullable String mood;

  private @Nullable Integer playerReputation;

  public GenerateDialogueRequest npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public GenerateDialogueRequest context(@Nullable String context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable String getContext() {
    return context;
  }

  public void setContext(@Nullable String context) {
    this.context = context;
  }

  public GenerateDialogueRequest mood(@Nullable String mood) {
    this.mood = mood;
    return this;
  }

  /**
   * Get mood
   * @return mood
   */
  
  @Schema(name = "mood", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mood")
  public @Nullable String getMood() {
    return mood;
  }

  public void setMood(@Nullable String mood) {
    this.mood = mood;
  }

  public GenerateDialogueRequest playerReputation(@Nullable Integer playerReputation) {
    this.playerReputation = playerReputation;
    return this;
  }

  /**
   * Get playerReputation
   * @return playerReputation
   */
  
  @Schema(name = "player_reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_reputation")
  public @Nullable Integer getPlayerReputation() {
    return playerReputation;
  }

  public void setPlayerReputation(@Nullable Integer playerReputation) {
    this.playerReputation = playerReputation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateDialogueRequest generateDialogueRequest = (GenerateDialogueRequest) o;
    return Objects.equals(this.npcId, generateDialogueRequest.npcId) &&
        Objects.equals(this.context, generateDialogueRequest.context) &&
        Objects.equals(this.mood, generateDialogueRequest.mood) &&
        Objects.equals(this.playerReputation, generateDialogueRequest.playerReputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, context, mood, playerReputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateDialogueRequest {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    mood: ").append(toIndentedString(mood)).append("\n");
    sb.append("    playerReputation: ").append(toIndentedString(playerReputation)).append("\n");
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

