package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.GetNPCDecisionRequestPlayerActionsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetNPCDecisionRequest
 */

@JsonTypeName("getNPCDecision_request")

public class GetNPCDecisionRequest {

  private @Nullable String npcId;

  private @Nullable Object situation;

  @Valid
  private List<@Valid GetNPCDecisionRequestPlayerActionsInner> playerActions = new ArrayList<>();

  private @Nullable Integer relationshipLevel;

  public GetNPCDecisionRequest npcId(@Nullable String npcId) {
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

  public GetNPCDecisionRequest situation(@Nullable Object situation) {
    this.situation = situation;
    return this;
  }

  /**
   * Get situation
   * @return situation
   */
  
  @Schema(name = "situation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("situation")
  public @Nullable Object getSituation() {
    return situation;
  }

  public void setSituation(@Nullable Object situation) {
    this.situation = situation;
  }

  public GetNPCDecisionRequest playerActions(List<@Valid GetNPCDecisionRequestPlayerActionsInner> playerActions) {
    this.playerActions = playerActions;
    return this;
  }

  public GetNPCDecisionRequest addPlayerActionsItem(GetNPCDecisionRequestPlayerActionsInner playerActionsItem) {
    if (this.playerActions == null) {
      this.playerActions = new ArrayList<>();
    }
    this.playerActions.add(playerActionsItem);
    return this;
  }

  /**
   * Get playerActions
   * @return playerActions
   */
  @Valid 
  @Schema(name = "player_actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_actions")
  public List<@Valid GetNPCDecisionRequestPlayerActionsInner> getPlayerActions() {
    return playerActions;
  }

  public void setPlayerActions(List<@Valid GetNPCDecisionRequestPlayerActionsInner> playerActions) {
    this.playerActions = playerActions;
  }

  public GetNPCDecisionRequest relationshipLevel(@Nullable Integer relationshipLevel) {
    this.relationshipLevel = relationshipLevel;
    return this;
  }

  /**
   * Get relationshipLevel
   * @return relationshipLevel
   */
  
  @Schema(name = "relationship_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_level")
  public @Nullable Integer getRelationshipLevel() {
    return relationshipLevel;
  }

  public void setRelationshipLevel(@Nullable Integer relationshipLevel) {
    this.relationshipLevel = relationshipLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetNPCDecisionRequest getNPCDecisionRequest = (GetNPCDecisionRequest) o;
    return Objects.equals(this.npcId, getNPCDecisionRequest.npcId) &&
        Objects.equals(this.situation, getNPCDecisionRequest.situation) &&
        Objects.equals(this.playerActions, getNPCDecisionRequest.playerActions) &&
        Objects.equals(this.relationshipLevel, getNPCDecisionRequest.relationshipLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, situation, playerActions, relationshipLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetNPCDecisionRequest {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    situation: ").append(toIndentedString(situation)).append("\n");
    sb.append("    playerActions: ").append(toIndentedString(playerActions)).append("\n");
    sb.append("    relationshipLevel: ").append(toIndentedString(relationshipLevel)).append("\n");
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

