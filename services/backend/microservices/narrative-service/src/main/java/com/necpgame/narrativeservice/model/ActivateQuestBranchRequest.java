package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ActivateQuestBranchRequest
 */

@JsonTypeName("activateQuestBranch_request")

public class ActivateQuestBranchRequest {

  private String characterId;

  private String choiceId;

  @Valid
  private Map<String, Object> context = new HashMap<>();

  public ActivateQuestBranchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActivateQuestBranchRequest(String characterId, String choiceId) {
    this.characterId = characterId;
    this.choiceId = choiceId;
  }

  public ActivateQuestBranchRequest characterId(String characterId) {
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

  public ActivateQuestBranchRequest choiceId(String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * Choice that triggered branch
   * @return choiceId
   */
  @NotNull 
  @Schema(name = "choice_id", description = "Choice that triggered branch", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("choice_id")
  public String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(String choiceId) {
    this.choiceId = choiceId;
  }

  public ActivateQuestBranchRequest context(Map<String, Object> context) {
    this.context = context;
    return this;
  }

  public ActivateQuestBranchRequest putContextItem(String key, Object contextItem) {
    if (this.context == null) {
      this.context = new HashMap<>();
    }
    this.context.put(key, contextItem);
    return this;
  }

  /**
   * Additional context data
   * @return context
   */
  
  @Schema(name = "context", description = "Additional context data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public Map<String, Object> getContext() {
    return context;
  }

  public void setContext(Map<String, Object> context) {
    this.context = context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActivateQuestBranchRequest activateQuestBranchRequest = (ActivateQuestBranchRequest) o;
    return Objects.equals(this.characterId, activateQuestBranchRequest.characterId) &&
        Objects.equals(this.choiceId, activateQuestBranchRequest.choiceId) &&
        Objects.equals(this.context, activateQuestBranchRequest.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, choiceId, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActivateQuestBranchRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
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

