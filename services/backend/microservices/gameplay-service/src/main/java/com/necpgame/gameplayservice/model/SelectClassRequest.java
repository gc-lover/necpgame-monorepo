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
 * SelectClassRequest
 */

@JsonTypeName("selectClass_request")

public class SelectClassRequest {

  private String characterId;

  private String classId;

  private @Nullable String subclassId;

  public SelectClassRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SelectClassRequest(String characterId, String classId) {
    this.characterId = characterId;
    this.classId = classId;
  }

  public SelectClassRequest characterId(String characterId) {
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

  public SelectClassRequest classId(String classId) {
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

  public SelectClassRequest subclassId(@Nullable String subclassId) {
    this.subclassId = subclassId;
    return this;
  }

  /**
   * Подкласс (опционально, можно выбрать позже)
   * @return subclassId
   */
  
  @Schema(name = "subclass_id", description = "Подкласс (опционально, можно выбрать позже)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subclass_id")
  public @Nullable String getSubclassId() {
    return subclassId;
  }

  public void setSubclassId(@Nullable String subclassId) {
    this.subclassId = subclassId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SelectClassRequest selectClassRequest = (SelectClassRequest) o;
    return Objects.equals(this.characterId, selectClassRequest.characterId) &&
        Objects.equals(this.classId, selectClassRequest.classId) &&
        Objects.equals(this.subclassId, selectClassRequest.subclassId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, classId, subclassId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SelectClassRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    subclassId: ").append(toIndentedString(subclassId)).append("\n");
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

