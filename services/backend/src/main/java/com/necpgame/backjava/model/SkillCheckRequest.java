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
 * SkillCheckRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SkillCheckRequest {

  private String skill;

  private Integer difficulty;

  private Boolean advantage = false;

  public SkillCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SkillCheckRequest(String skill, Integer difficulty) {
    this.skill = skill;
    this.difficulty = difficulty;
  }

  public SkillCheckRequest skill(String skill) {
    this.skill = skill;
    return this;
  }

  /**
   * Get skill
   * @return skill
   */
  @NotNull 
  @Schema(name = "skill", example = "STEALTH", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skill")
  public String getSkill() {
    return skill;
  }

  public void setSkill(String skill) {
    this.skill = skill;
  }

  public SkillCheckRequest difficulty(Integer difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  @NotNull 
  @Schema(name = "difficulty", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("difficulty")
  public Integer getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Integer difficulty) {
    this.difficulty = difficulty;
  }

  public SkillCheckRequest advantage(Boolean advantage) {
    this.advantage = advantage;
    return this;
  }

  /**
   * Get advantage
   * @return advantage
   */
  
  @Schema(name = "advantage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("advantage")
  public Boolean getAdvantage() {
    return advantage;
  }

  public void setAdvantage(Boolean advantage) {
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
    SkillCheckRequest skillCheckRequest = (SkillCheckRequest) o;
    return Objects.equals(this.skill, skillCheckRequest.skill) &&
        Objects.equals(this.difficulty, skillCheckRequest.difficulty) &&
        Objects.equals(this.advantage, skillCheckRequest.advantage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skill, difficulty, advantage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillCheckRequest {\n");
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

