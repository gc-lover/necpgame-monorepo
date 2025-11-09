package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SynergyRequirements
 */

@JsonTypeName("Synergy_requirements")

public class SynergyRequirements {

  @Valid
  private Map<String, Integer> attributes = new HashMap<>();

  @Valid
  private Map<String, Integer> skills = new HashMap<>();

  public SynergyRequirements attributes(Map<String, Integer> attributes) {
    this.attributes = attributes;
    return this;
  }

  public SynergyRequirements putAttributesItem(String key, Integer attributesItem) {
    if (this.attributes == null) {
      this.attributes = new HashMap<>();
    }
    this.attributes.put(key, attributesItem);
    return this;
  }

  /**
   * Get attributes
   * @return attributes
   */
  
  @Schema(name = "attributes", example = "{\"INTELLIGENCE\":15,\"TECH\":12}", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attributes")
  public Map<String, Integer> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Integer> attributes) {
    this.attributes = attributes;
  }

  public SynergyRequirements skills(Map<String, Integer> skills) {
    this.skills = skills;
    return this;
  }

  public SynergyRequirements putSkillsItem(String key, Integer skillsItem) {
    if (this.skills == null) {
      this.skills = new HashMap<>();
    }
    this.skills.put(key, skillsItem);
    return this;
  }

  /**
   * Get skills
   * @return skills
   */
  
  @Schema(name = "skills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skills")
  public Map<String, Integer> getSkills() {
    return skills;
  }

  public void setSkills(Map<String, Integer> skills) {
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
    SynergyRequirements synergyRequirements = (SynergyRequirements) o;
    return Objects.equals(this.attributes, synergyRequirements.attributes) &&
        Objects.equals(this.skills, synergyRequirements.skills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributes, skills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SynergyRequirements {\n");
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

