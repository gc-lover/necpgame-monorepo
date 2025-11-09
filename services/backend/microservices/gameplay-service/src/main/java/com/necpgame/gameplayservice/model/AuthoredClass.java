package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AuthoredClass
 */


public class AuthoredClass {

  private @Nullable String classId;

  private @Nullable String name;

  private @Nullable String description;

  public AuthoredClass classId(@Nullable String classId) {
    this.classId = classId;
    return this;
  }

  /**
   * Get classId
   * @return classId
   */
  
  @Schema(name = "class_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_id")
  public @Nullable String getClassId() {
    return classId;
  }

  public void setClassId(@Nullable String classId) {
    this.classId = classId;
  }

  public AuthoredClass name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public AuthoredClass description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthoredClass authoredClass = (AuthoredClass) o;
    return Objects.equals(this.classId, authoredClass.classId) &&
        Objects.equals(this.name, authoredClass.name) &&
        Objects.equals(this.description, authoredClass.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, name, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthoredClass {\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

