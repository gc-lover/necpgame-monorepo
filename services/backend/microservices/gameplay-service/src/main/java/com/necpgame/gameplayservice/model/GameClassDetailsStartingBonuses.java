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
 * GameClassDetailsStartingBonuses
 */

@JsonTypeName("GameClassDetails_starting_bonuses")

public class GameClassDetailsStartingBonuses {

  private @Nullable Object attributes;

  private @Nullable Object skills;

  public GameClassDetailsStartingBonuses attributes(@Nullable Object attributes) {
    this.attributes = attributes;
    return this;
  }

  /**
   * Бонусы к атрибутам (REF, BODY, INT, etc.)
   * @return attributes
   */
  
  @Schema(name = "attributes", description = "Бонусы к атрибутам (REF, BODY, INT, etc.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attributes")
  public @Nullable Object getAttributes() {
    return attributes;
  }

  public void setAttributes(@Nullable Object attributes) {
    this.attributes = attributes;
  }

  public GameClassDetailsStartingBonuses skills(@Nullable Object skills) {
    this.skills = skills;
    return this;
  }

  /**
   * Начальные уровни навыков
   * @return skills
   */
  
  @Schema(name = "skills", description = "Начальные уровни навыков", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skills")
  public @Nullable Object getSkills() {
    return skills;
  }

  public void setSkills(@Nullable Object skills) {
    this.skills = skills;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameClassDetailsStartingBonuses gameClassDetailsStartingBonuses = (GameClassDetailsStartingBonuses) o;
    return Objects.equals(this.attributes, gameClassDetailsStartingBonuses.attributes) &&
        Objects.equals(this.skills, gameClassDetailsStartingBonuses.skills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributes, skills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameClassDetailsStartingBonuses {\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    skills: ").append(toIndentedString(skills)).append("\n");
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

