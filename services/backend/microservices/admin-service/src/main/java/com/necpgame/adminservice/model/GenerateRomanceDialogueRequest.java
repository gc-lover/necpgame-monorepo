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
 * GenerateRomanceDialogueRequest
 */

@JsonTypeName("generateRomanceDialogue_request")

public class GenerateRomanceDialogueRequest {

  private @Nullable String npcId;

  private @Nullable String relationshipStage;

  private @Nullable Object context;

  private @Nullable Integer affectionLevel;

  public GenerateRomanceDialogueRequest npcId(@Nullable String npcId) {
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

  public GenerateRomanceDialogueRequest relationshipStage(@Nullable String relationshipStage) {
    this.relationshipStage = relationshipStage;
    return this;
  }

  /**
   * Get relationshipStage
   * @return relationshipStage
   */
  
  @Schema(name = "relationship_stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_stage")
  public @Nullable String getRelationshipStage() {
    return relationshipStage;
  }

  public void setRelationshipStage(@Nullable String relationshipStage) {
    this.relationshipStage = relationshipStage;
  }

  public GenerateRomanceDialogueRequest context(@Nullable Object context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable Object getContext() {
    return context;
  }

  public void setContext(@Nullable Object context) {
    this.context = context;
  }

  public GenerateRomanceDialogueRequest affectionLevel(@Nullable Integer affectionLevel) {
    this.affectionLevel = affectionLevel;
    return this;
  }

  /**
   * Get affectionLevel
   * @return affectionLevel
   */
  
  @Schema(name = "affection_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affection_level")
  public @Nullable Integer getAffectionLevel() {
    return affectionLevel;
  }

  public void setAffectionLevel(@Nullable Integer affectionLevel) {
    this.affectionLevel = affectionLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateRomanceDialogueRequest generateRomanceDialogueRequest = (GenerateRomanceDialogueRequest) o;
    return Objects.equals(this.npcId, generateRomanceDialogueRequest.npcId) &&
        Objects.equals(this.relationshipStage, generateRomanceDialogueRequest.relationshipStage) &&
        Objects.equals(this.context, generateRomanceDialogueRequest.context) &&
        Objects.equals(this.affectionLevel, generateRomanceDialogueRequest.affectionLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, relationshipStage, context, affectionLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateRomanceDialogueRequest {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    relationshipStage: ").append(toIndentedString(relationshipStage)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    affectionLevel: ").append(toIndentedString(affectionLevel)).append("\n");
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

