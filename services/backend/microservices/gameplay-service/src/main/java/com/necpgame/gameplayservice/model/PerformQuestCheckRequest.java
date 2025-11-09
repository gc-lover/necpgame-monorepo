package com.necpgame.gameplayservice.model;

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
 * PerformQuestCheckRequest
 */

@JsonTypeName("performQuestCheck_request")

public class PerformQuestCheckRequest {

  private String characterId;

  private String questId;

  private String checkType;

  private Integer dc;

  public PerformQuestCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformQuestCheckRequest(String characterId, String questId, String checkType, Integer dc) {
    this.characterId = characterId;
    this.questId = questId;
    this.checkType = checkType;
    this.dc = dc;
  }

  public PerformQuestCheckRequest characterId(String characterId) {
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

  public PerformQuestCheckRequest questId(String questId) {
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

  public PerformQuestCheckRequest checkType(String checkType) {
    this.checkType = checkType;
    return this;
  }

  /**
   * Get checkType
   * @return checkType
   */
  @NotNull 
  @Schema(name = "check_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("check_type")
  public String getCheckType() {
    return checkType;
  }

  public void setCheckType(String checkType) {
    this.checkType = checkType;
  }

  public PerformQuestCheckRequest dc(Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Get dc
   * @return dc
   */
  @NotNull 
  @Schema(name = "dc", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dc")
  public Integer getDc() {
    return dc;
  }

  public void setDc(Integer dc) {
    this.dc = dc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformQuestCheckRequest performQuestCheckRequest = (PerformQuestCheckRequest) o;
    return Objects.equals(this.characterId, performQuestCheckRequest.characterId) &&
        Objects.equals(this.questId, performQuestCheckRequest.questId) &&
        Objects.equals(this.checkType, performQuestCheckRequest.checkType) &&
        Objects.equals(this.dc, performQuestCheckRequest.dc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, questId, checkType, dc);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformQuestCheckRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    checkType: ").append(toIndentedString(checkType)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
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

