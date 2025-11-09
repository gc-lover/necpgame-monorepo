package com.necpgame.narrativeservice.model;

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
 * QuestBranchRequirementsSkillChecksInner
 */

@JsonTypeName("QuestBranch_requirements_skill_checks_inner")

public class QuestBranchRequirementsSkillChecksInner {

  private @Nullable String skill;

  private @Nullable Integer difficulty;

  public QuestBranchRequirementsSkillChecksInner skill(@Nullable String skill) {
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

  public QuestBranchRequirementsSkillChecksInner difficulty(@Nullable Integer difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  
  @Schema(name = "difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable Integer getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable Integer difficulty) {
    this.difficulty = difficulty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestBranchRequirementsSkillChecksInner questBranchRequirementsSkillChecksInner = (QuestBranchRequirementsSkillChecksInner) o;
    return Objects.equals(this.skill, questBranchRequirementsSkillChecksInner.skill) &&
        Objects.equals(this.difficulty, questBranchRequirementsSkillChecksInner.difficulty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skill, difficulty);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestBranchRequirementsSkillChecksInner {\n");
    sb.append("    skill: ").append(toIndentedString(skill)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
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

