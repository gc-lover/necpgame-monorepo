package com.necpgame.narrativeservice.model;

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
 * ValidateBranchCoherenceRequest
 */

@JsonTypeName("validateBranchCoherence_request")

public class ValidateBranchCoherenceRequest {

  private String characterId;

  private String questId;

  private @Nullable String proposedBranchId;

  public ValidateBranchCoherenceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidateBranchCoherenceRequest(String characterId, String questId) {
    this.characterId = characterId;
    this.questId = questId;
  }

  public ValidateBranchCoherenceRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public ValidateBranchCoherenceRequest questId(String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  @NotNull 
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quest_id")
  public String getQuestId() {
    return questId;
  }

  public void setQuestId(String questId) {
    this.questId = questId;
  }

  public ValidateBranchCoherenceRequest proposedBranchId(@Nullable String proposedBranchId) {
    this.proposedBranchId = proposedBranchId;
    return this;
  }

  /**
   * Branch для валидации
   * @return proposedBranchId
   */
  
  @Schema(name = "proposed_branch_id", description = "Branch для валидации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("proposed_branch_id")
  public @Nullable String getProposedBranchId() {
    return proposedBranchId;
  }

  public void setProposedBranchId(@Nullable String proposedBranchId) {
    this.proposedBranchId = proposedBranchId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidateBranchCoherenceRequest validateBranchCoherenceRequest = (ValidateBranchCoherenceRequest) o;
    return Objects.equals(this.characterId, validateBranchCoherenceRequest.characterId) &&
        Objects.equals(this.questId, validateBranchCoherenceRequest.questId) &&
        Objects.equals(this.proposedBranchId, validateBranchCoherenceRequest.proposedBranchId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, questId, proposedBranchId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidateBranchCoherenceRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    proposedBranchId: ").append(toIndentedString(proposedBranchId)).append("\n");
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

