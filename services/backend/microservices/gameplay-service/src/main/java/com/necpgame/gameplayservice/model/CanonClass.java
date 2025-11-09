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
 * CanonClass
 */


public class CanonClass {

  private @Nullable String classId;

  private @Nullable String name;

  private @Nullable String loreSource;

  private @Nullable String description;

  public CanonClass classId(@Nullable String classId) {
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

  public CanonClass name(@Nullable String name) {
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

  public CanonClass loreSource(@Nullable String loreSource) {
    this.loreSource = loreSource;
    return this;
  }

  /**
   * Get loreSource
   * @return loreSource
   */
  
  @Schema(name = "lore_source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lore_source")
  public @Nullable String getLoreSource() {
    return loreSource;
  }

  public void setLoreSource(@Nullable String loreSource) {
    this.loreSource = loreSource;
  }

  public CanonClass description(@Nullable String description) {
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
    CanonClass canonClass = (CanonClass) o;
    return Objects.equals(this.classId, canonClass.classId) &&
        Objects.equals(this.name, canonClass.name) &&
        Objects.equals(this.loreSource, canonClass.loreSource) &&
        Objects.equals(this.description, canonClass.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, name, loreSource, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CanonClass {\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    loreSource: ").append(toIndentedString(loreSource)).append("\n");
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

