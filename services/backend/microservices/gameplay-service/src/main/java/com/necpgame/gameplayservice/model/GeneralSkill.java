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
 * GeneralSkill
 */


public class GeneralSkill {

  private @Nullable String skillId;

  private @Nullable String category;

  public GeneralSkill skillId(@Nullable String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  
  @Schema(name = "skill_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_id")
  public @Nullable String getSkillId() {
    return skillId;
  }

  public void setSkillId(@Nullable String skillId) {
    this.skillId = skillId;
  }

  public GeneralSkill category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeneralSkill generalSkill = (GeneralSkill) o;
    return Objects.equals(this.skillId, generalSkill.skillId) &&
        Objects.equals(this.category, generalSkill.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillId, category);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeneralSkill {\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
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

