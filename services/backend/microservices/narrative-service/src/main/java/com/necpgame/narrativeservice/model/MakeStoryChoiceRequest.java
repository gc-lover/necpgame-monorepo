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
 * MakeStoryChoiceRequest
 */

@JsonTypeName("makeStoryChoice_request")

public class MakeStoryChoiceRequest {

  private String characterId;

  private String questId;

  private String choiceId;

  private String decision;

  private @Nullable String reasoning;

  public MakeStoryChoiceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MakeStoryChoiceRequest(String characterId, String questId, String choiceId, String decision) {
    this.characterId = characterId;
    this.questId = questId;
    this.choiceId = choiceId;
    this.decision = decision;
  }

  public MakeStoryChoiceRequest characterId(String characterId) {
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

  public MakeStoryChoiceRequest questId(String questId) {
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

  public MakeStoryChoiceRequest choiceId(String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * Get choiceId
   * @return choiceId
   */
  @NotNull 
  @Schema(name = "choice_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("choice_id")
  public String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(String choiceId) {
    this.choiceId = choiceId;
  }

  public MakeStoryChoiceRequest decision(String decision) {
    this.decision = decision;
    return this;
  }

  /**
   * Get decision
   * @return decision
   */
  @NotNull 
  @Schema(name = "decision", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("decision")
  public String getDecision() {
    return decision;
  }

  public void setDecision(String decision) {
    this.decision = decision;
  }

  public MakeStoryChoiceRequest reasoning(@Nullable String reasoning) {
    this.reasoning = reasoning;
    return this;
  }

  /**
   * Опциональное обоснование выбора
   * @return reasoning
   */
  
  @Schema(name = "reasoning", description = "Опциональное обоснование выбора", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reasoning")
  public @Nullable String getReasoning() {
    return reasoning;
  }

  public void setReasoning(@Nullable String reasoning) {
    this.reasoning = reasoning;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MakeStoryChoiceRequest makeStoryChoiceRequest = (MakeStoryChoiceRequest) o;
    return Objects.equals(this.characterId, makeStoryChoiceRequest.characterId) &&
        Objects.equals(this.questId, makeStoryChoiceRequest.questId) &&
        Objects.equals(this.choiceId, makeStoryChoiceRequest.choiceId) &&
        Objects.equals(this.decision, makeStoryChoiceRequest.decision) &&
        Objects.equals(this.reasoning, makeStoryChoiceRequest.reasoning);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, questId, choiceId, decision, reasoning);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MakeStoryChoiceRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    decision: ").append(toIndentedString(decision)).append("\n");
    sb.append("    reasoning: ").append(toIndentedString(reasoning)).append("\n");
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

