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
 * CompleteQuestRequest
 */


public class CompleteQuestRequest {

  private UUID characterId;

  private String questId;

  public CompleteQuestRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CompleteQuestRequest(UUID characterId, String questId) {
    this.characterId = characterId;
    this.questId = questId;
  }

  public CompleteQuestRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public CompleteQuestRequest questId(String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  @NotNull 
  @Schema(name = "questId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("questId")
  public String getQuestId() {
    return questId;
  }

  public void setQuestId(String questId) {
    this.questId = questId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompleteQuestRequest completeQuestRequest = (CompleteQuestRequest) o;
    return Objects.equals(this.characterId, completeQuestRequest.characterId) &&
        Objects.equals(this.questId, completeQuestRequest.questId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, questId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteQuestRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
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

