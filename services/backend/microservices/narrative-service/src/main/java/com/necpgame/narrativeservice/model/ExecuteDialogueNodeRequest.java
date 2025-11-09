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
 * ExecuteDialogueNodeRequest
 */

@JsonTypeName("executeDialogueNode_request")

public class ExecuteDialogueNodeRequest {

  private String choiceId;

  private String characterId;

  private @Nullable Integer skillModifier;

  public ExecuteDialogueNodeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExecuteDialogueNodeRequest(String choiceId, String characterId) {
    this.choiceId = choiceId;
    this.characterId = characterId;
  }

  public ExecuteDialogueNodeRequest choiceId(String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * ID выбранного dialogue choice
   * @return choiceId
   */
  @NotNull 
  @Schema(name = "choice_id", example = "choice_persuade", description = "ID выбранного dialogue choice", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("choice_id")
  public String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(String choiceId) {
    this.choiceId = choiceId;
  }

  public ExecuteDialogueNodeRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID персонажа игрока
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", example = "char_123", description = "ID персонажа игрока", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public ExecuteDialogueNodeRequest skillModifier(@Nullable Integer skillModifier) {
    this.skillModifier = skillModifier;
    return this;
  }

  /**
   * Модификатор навыка (если применим)
   * @return skillModifier
   */
  
  @Schema(name = "skill_modifier", example = "5", description = "Модификатор навыка (если применим)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_modifier")
  public @Nullable Integer getSkillModifier() {
    return skillModifier;
  }

  public void setSkillModifier(@Nullable Integer skillModifier) {
    this.skillModifier = skillModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecuteDialogueNodeRequest executeDialogueNodeRequest = (ExecuteDialogueNodeRequest) o;
    return Objects.equals(this.choiceId, executeDialogueNodeRequest.choiceId) &&
        Objects.equals(this.characterId, executeDialogueNodeRequest.characterId) &&
        Objects.equals(this.skillModifier, executeDialogueNodeRequest.skillModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(choiceId, characterId, skillModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecuteDialogueNodeRequest {\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    skillModifier: ").append(toIndentedString(skillModifier)).append("\n");
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

