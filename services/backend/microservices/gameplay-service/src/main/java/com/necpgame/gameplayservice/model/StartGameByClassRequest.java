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
 * StartGameByClassRequest
 */

@JsonTypeName("startGameByClass_request")

public class StartGameByClassRequest {

  private String characterId;

  private String classId;

  public StartGameByClassRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StartGameByClassRequest(String characterId, String classId) {
    this.characterId = characterId;
    this.classId = classId;
  }

  public StartGameByClassRequest characterId(String characterId) {
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

  public StartGameByClassRequest classId(String classId) {
    this.classId = classId;
    return this;
  }

  /**
   * Get classId
   * @return classId
   */
  @NotNull 
  @Schema(name = "class_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("class_id")
  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartGameByClassRequest startGameByClassRequest = (StartGameByClassRequest) o;
    return Objects.equals(this.characterId, startGameByClassRequest.characterId) &&
        Objects.equals(this.classId, startGameByClassRequest.classId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, classId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartGameByClassRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
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

