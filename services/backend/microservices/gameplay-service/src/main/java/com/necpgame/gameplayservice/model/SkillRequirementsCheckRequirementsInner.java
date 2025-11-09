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
 * SkillRequirementsCheckRequirementsInner
 */

@JsonTypeName("SkillRequirementsCheck_requirements_inner")

public class SkillRequirementsCheckRequirementsInner {

  private @Nullable String skill;

  private @Nullable Integer requiredLevel;

  private @Nullable Integer characterLevel;

  private @Nullable Boolean met;

  public SkillRequirementsCheckRequirementsInner skill(@Nullable String skill) {
    this.skill = skill;
    return this;
  }

  /**
   * Get skill
   * @return skill
   */
  
  @Schema(name = "skill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill")
  public @Nullable String getSkill() {
    return skill;
  }

  public void setSkill(@Nullable String skill) {
    this.skill = skill;
  }

  public SkillRequirementsCheckRequirementsInner requiredLevel(@Nullable Integer requiredLevel) {
    this.requiredLevel = requiredLevel;
    return this;
  }

  /**
   * Get requiredLevel
   * @return requiredLevel
   */
  
  @Schema(name = "required_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_level")
  public @Nullable Integer getRequiredLevel() {
    return requiredLevel;
  }

  public void setRequiredLevel(@Nullable Integer requiredLevel) {
    this.requiredLevel = requiredLevel;
  }

  public SkillRequirementsCheckRequirementsInner characterLevel(@Nullable Integer characterLevel) {
    this.characterLevel = characterLevel;
    return this;
  }

  /**
   * Get characterLevel
   * @return characterLevel
   */
  
  @Schema(name = "character_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_level")
  public @Nullable Integer getCharacterLevel() {
    return characterLevel;
  }

  public void setCharacterLevel(@Nullable Integer characterLevel) {
    this.characterLevel = characterLevel;
  }

  public SkillRequirementsCheckRequirementsInner met(@Nullable Boolean met) {
    this.met = met;
    return this;
  }

  /**
   * Get met
   * @return met
   */
  
  @Schema(name = "met", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("met")
  public @Nullable Boolean getMet() {
    return met;
  }

  public void setMet(@Nullable Boolean met) {
    this.met = met;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillRequirementsCheckRequirementsInner skillRequirementsCheckRequirementsInner = (SkillRequirementsCheckRequirementsInner) o;
    return Objects.equals(this.skill, skillRequirementsCheckRequirementsInner.skill) &&
        Objects.equals(this.requiredLevel, skillRequirementsCheckRequirementsInner.requiredLevel) &&
        Objects.equals(this.characterLevel, skillRequirementsCheckRequirementsInner.characterLevel) &&
        Objects.equals(this.met, skillRequirementsCheckRequirementsInner.met);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skill, requiredLevel, characterLevel, met);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillRequirementsCheckRequirementsInner {\n");
    sb.append("    skill: ").append(toIndentedString(skill)).append("\n");
    sb.append("    requiredLevel: ").append(toIndentedString(requiredLevel)).append("\n");
    sb.append("    characterLevel: ").append(toIndentedString(characterLevel)).append("\n");
    sb.append("    met: ").append(toIndentedString(met)).append("\n");
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

