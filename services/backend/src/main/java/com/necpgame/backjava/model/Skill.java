package com.necpgame.backjava.model;

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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Skill {

  private @Nullable String skillId;

  private @Nullable String name;

  private @Nullable Integer level;

  private @Nullable Integer experience;

  private @Nullable Integer experienceToNextLevel;

  private @Nullable Float progressPercentage;

  private @Nullable String attributeDependency;

  public Skill skillId(@Nullable String skillId) {
    this.skillId = skillId;
    return this;
  }

  @Schema(name = "skill_id", example = "STEALTH", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_id")
  public @Nullable String getSkillId() {
    return skillId;
  }

  public void setSkillId(@Nullable String skillId) {
    this.skillId = skillId;
  }

  public Skill name(@Nullable String name) {
    this.name = name;
    return this;
  }

  @Schema(name = "name", example = "Stealth", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Skill level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  @Schema(name = "level", example = "8", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public Skill experience(@Nullable Integer experience) {
    this.experience = experience;
    return this;
  }

  @Schema(name = "experience", example = "3500", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable Integer getExperience() {
    return experience;
  }

  public void setExperience(@Nullable Integer experience) {
    this.experience = experience;
  }

  public Skill experienceToNextLevel(@Nullable Integer experienceToNextLevel) {
    this.experienceToNextLevel = experienceToNextLevel;
    return this;
  }

  @Schema(name = "experience_to_next_level", example = "4000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_to_next_level")
  public @Nullable Integer getExperienceToNextLevel() {
    return experienceToNextLevel;
  }

  public void setExperienceToNextLevel(@Nullable Integer experienceToNextLevel) {
    this.experienceToNextLevel = experienceToNextLevel;
  }

  public Skill progressPercentage(@Nullable Float progressPercentage) {
    this.progressPercentage = progressPercentage;
    return this;
  }

  @Schema(name = "progress_percentage", example = "87.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress_percentage")
  public @Nullable Float getProgressPercentage() {
    return progressPercentage;
  }

  public void setProgressPercentage(@Nullable Float progressPercentage) {
    this.progressPercentage = progressPercentage;
  }

  public Skill attributeDependency(@Nullable String attributeDependency) {
    this.attributeDependency = attributeDependency;
    return this;
  }

  @Schema(name = "attribute_dependency", example = "COOL", description = "От какого атрибута зависит", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_dependency")
  public @Nullable String getAttributeDependency() {
    return attributeDependency;
  }

  public void setAttributeDependency(@Nullable String attributeDependency) {
    this.attributeDependency = attributeDependency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Skill skill = (Skill) o;
    return Objects.equals(this.skillId, skill.skillId) &&
        Objects.equals(this.name, skill.name) &&
        Objects.equals(this.level, skill.level) &&
        Objects.equals(this.experience, skill.experience) &&
        Objects.equals(this.experienceToNextLevel, skill.experienceToNextLevel) &&
        Objects.equals(this.progressPercentage, skill.progressPercentage) &&
        Objects.equals(this.attributeDependency, skill.attributeDependency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillId, name, level, experience, experienceToNextLevel, progressPercentage, attributeDependency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Skill {\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    experienceToNextLevel: ").append(toIndentedString(experienceToNextLevel)).append("\n");
    sb.append("    progressPercentage: ").append(toIndentedString(progressPercentage)).append("\n");
    sb.append("    attributeDependency: ").append(toIndentedString(attributeDependency)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

