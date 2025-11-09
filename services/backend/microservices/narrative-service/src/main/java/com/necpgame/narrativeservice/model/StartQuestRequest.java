package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * StartQuestRequest
 */


public class StartQuestRequest {

  private UUID characterId;

  private String questTemplateId;

  public StartQuestRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StartQuestRequest(UUID characterId, String questTemplateId) {
    this.characterId = characterId;
    this.questTemplateId = questTemplateId;
  }

  public StartQuestRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public StartQuestRequest questTemplateId(String questTemplateId) {
    this.questTemplateId = questTemplateId;
    return this;
  }

  /**
   * Get questTemplateId
   * @return questTemplateId
   */
  @NotNull 
  @Schema(name = "quest_template_id", example = "quest_first_contact", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quest_template_id")
  public String getQuestTemplateId() {
    return questTemplateId;
  }

  public void setQuestTemplateId(String questTemplateId) {
    this.questTemplateId = questTemplateId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartQuestRequest startQuestRequest = (StartQuestRequest) o;
    return Objects.equals(this.characterId, startQuestRequest.characterId) &&
        Objects.equals(this.questTemplateId, startQuestRequest.questTemplateId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, questTemplateId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartQuestRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    questTemplateId: ").append(toIndentedString(questTemplateId)).append("\n");
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

