package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RomanceEventGenerationRequestContext;
import com.necpgame.socialservice.model.RomanceEventGenerationRequestGenerationParams;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RomanceEventGenerationRequest
 */


public class RomanceEventGenerationRequest {

  private String playerId;

  private String npcId;

  private @Nullable RomanceEventGenerationRequestContext context;

  private @Nullable RomanceEventGenerationRequestGenerationParams generationParams;

  public RomanceEventGenerationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RomanceEventGenerationRequest(String playerId, String npcId) {
    this.playerId = playerId;
    this.npcId = npcId;
  }

  public RomanceEventGenerationRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("player_id")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public RomanceEventGenerationRequest npcId(String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  @NotNull 
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npc_id")
  public String getNpcId() {
    return npcId;
  }

  public void setNpcId(String npcId) {
    this.npcId = npcId;
  }

  public RomanceEventGenerationRequest context(@Nullable RomanceEventGenerationRequestContext context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @Valid 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable RomanceEventGenerationRequestContext getContext() {
    return context;
  }

  public void setContext(@Nullable RomanceEventGenerationRequestContext context) {
    this.context = context;
  }

  public RomanceEventGenerationRequest generationParams(@Nullable RomanceEventGenerationRequestGenerationParams generationParams) {
    this.generationParams = generationParams;
    return this;
  }

  /**
   * Get generationParams
   * @return generationParams
   */
  @Valid 
  @Schema(name = "generation_params", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generation_params")
  public @Nullable RomanceEventGenerationRequestGenerationParams getGenerationParams() {
    return generationParams;
  }

  public void setGenerationParams(@Nullable RomanceEventGenerationRequestGenerationParams generationParams) {
    this.generationParams = generationParams;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEventGenerationRequest romanceEventGenerationRequest = (RomanceEventGenerationRequest) o;
    return Objects.equals(this.playerId, romanceEventGenerationRequest.playerId) &&
        Objects.equals(this.npcId, romanceEventGenerationRequest.npcId) &&
        Objects.equals(this.context, romanceEventGenerationRequest.context) &&
        Objects.equals(this.generationParams, romanceEventGenerationRequest.generationParams);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, npcId, context, generationParams);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceEventGenerationRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    generationParams: ").append(toIndentedString(generationParams)).append("\n");
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

