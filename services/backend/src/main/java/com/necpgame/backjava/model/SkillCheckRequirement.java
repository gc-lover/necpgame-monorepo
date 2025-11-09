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

/**
 * SkillCheckRequirement
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SkillCheckRequirement {

  private @Nullable String skill;

  private @Nullable Integer difficulty;

  private @Nullable Boolean advantage;

  public SkillCheckRequirement skill(@Nullable String skill) {
    this.skill = skill;
    return this;
  }

  /**
   * Get skill
   * @return skill
   */
  
  @Schema(name = "skill", example = "PERSUASION", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill")
  public @Nullable String getSkill() {
    return skill;
  }

  public void setSkill(@Nullable String skill) {
    this.skill = skill;
  }

  public SkillCheckRequirement difficulty(@Nullable Integer difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * DC (Difficulty Class)
   * @return difficulty
   */
  
  @Schema(name = "difficulty", example = "15", description = "DC (Difficulty Class)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable Integer getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable Integer difficulty) {
    this.difficulty = difficulty;
  }

  public SkillCheckRequirement advantage(@Nullable Boolean advantage) {
    this.advantage = advantage;
    return this;
  }

  /**
   * Бросок с преимуществом (2d20, берём лучший)
   * @return advantage
   */
  
  @Schema(name = "advantage", description = "Бросок с преимуществом (2d20, берём лучший)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("advantage")
  public @Nullable Boolean getAdvantage() {
    return advantage;
  }

  public void setAdvantage(@Nullable Boolean advantage) {
    this.advantage = advantage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillCheckRequirement skillCheckRequirement = (SkillCheckRequirement) o;
    return Objects.equals(this.skill, skillCheckRequirement.skill) &&
        Objects.equals(this.difficulty, skillCheckRequirement.difficulty) &&
        Objects.equals(this.advantage, skillCheckRequirement.advantage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skill, difficulty, advantage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillCheckRequirement {\n");
    sb.append("    skill: ").append(toIndentedString(skill)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    advantage: ").append(toIndentedString(advantage)).append("\n");
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

