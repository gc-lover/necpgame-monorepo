package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AbilityDetailRequirements
 */

@JsonTypeName("AbilityDetail_requirements")

public class AbilityDetailRequirements {

  private @Nullable Integer level;

  private @Nullable Object attributes;

  private @Nullable Object skills;

  @Valid
  private List<String> implants = new ArrayList<>();

  public AbilityDetailRequirements level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public AbilityDetailRequirements attributes(@Nullable Object attributes) {
    this.attributes = attributes;
    return this;
  }

  /**
   * Get attributes
   * @return attributes
   */
  
  @Schema(name = "attributes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attributes")
  public @Nullable Object getAttributes() {
    return attributes;
  }

  public void setAttributes(@Nullable Object attributes) {
    this.attributes = attributes;
  }

  public AbilityDetailRequirements skills(@Nullable Object skills) {
    this.skills = skills;
    return this;
  }

  /**
   * Get skills
   * @return skills
   */
  
  @Schema(name = "skills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skills")
  public @Nullable Object getSkills() {
    return skills;
  }

  public void setSkills(@Nullable Object skills) {
    this.skills = skills;
  }

  public AbilityDetailRequirements implants(List<String> implants) {
    this.implants = implants;
    return this;
  }

  public AbilityDetailRequirements addImplantsItem(String implantsItem) {
    if (this.implants == null) {
      this.implants = new ArrayList<>();
    }
    this.implants.add(implantsItem);
    return this;
  }

  /**
   * Get implants
   * @return implants
   */
  
  @Schema(name = "implants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implants")
  public List<String> getImplants() {
    return implants;
  }

  public void setImplants(List<String> implants) {
    this.implants = implants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityDetailRequirements abilityDetailRequirements = (AbilityDetailRequirements) o;
    return Objects.equals(this.level, abilityDetailRequirements.level) &&
        Objects.equals(this.attributes, abilityDetailRequirements.attributes) &&
        Objects.equals(this.skills, abilityDetailRequirements.skills) &&
        Objects.equals(this.implants, abilityDetailRequirements.implants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, attributes, skills, implants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityDetailRequirements {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    skills: ").append(toIndentedString(skills)).append("\n");
    sb.append("    implants: ").append(toIndentedString(implants)).append("\n");
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

