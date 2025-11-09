package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.CreatePlayerCharacterRequestAppearance;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreatePlayerCharacterRequest
 */

@JsonTypeName("createPlayerCharacter_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CreatePlayerCharacterRequest {

  private String name;

  private String classId;

  private CreatePlayerCharacterRequestAppearance appearance;

  private @Nullable String originId;

  public CreatePlayerCharacterRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreatePlayerCharacterRequest(String name, String classId, CreatePlayerCharacterRequestAppearance appearance) {
    this.name = name;
    this.classId = classId;
    this.appearance = appearance;
  }

  public CreatePlayerCharacterRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Имя персонажа (уникальное)
   * @return name
   */
  @NotNull @Size(min = 3, max = 20) 
  @Schema(name = "name", description = "Имя персонажа (уникальное)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreatePlayerCharacterRequest classId(String classId) {
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

  public CreatePlayerCharacterRequest appearance(CreatePlayerCharacterRequestAppearance appearance) {
    this.appearance = appearance;
    return this;
  }

  /**
   * Get appearance
   * @return appearance
   */
  @NotNull @Valid 
  @Schema(name = "appearance", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("appearance")
  public CreatePlayerCharacterRequestAppearance getAppearance() {
    return appearance;
  }

  public void setAppearance(CreatePlayerCharacterRequestAppearance appearance) {
    this.appearance = appearance;
  }

  public CreatePlayerCharacterRequest originId(@Nullable String originId) {
    this.originId = originId;
    return this;
  }

  /**
   * Life Path (опционально)
   * @return originId
   */
  
  @Schema(name = "origin_id", description = "Life Path (опционально)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("origin_id")
  public @Nullable String getOriginId() {
    return originId;
  }

  public void setOriginId(@Nullable String originId) {
    this.originId = originId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePlayerCharacterRequest createPlayerCharacterRequest = (CreatePlayerCharacterRequest) o;
    return Objects.equals(this.name, createPlayerCharacterRequest.name) &&
        Objects.equals(this.classId, createPlayerCharacterRequest.classId) &&
        Objects.equals(this.appearance, createPlayerCharacterRequest.appearance) &&
        Objects.equals(this.originId, createPlayerCharacterRequest.originId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, classId, appearance, originId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePlayerCharacterRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    appearance: ").append(toIndentedString(appearance)).append("\n");
    sb.append("    originId: ").append(toIndentedString(originId)).append("\n");
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

