package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * IncreaseSkillRequest
 */

@JsonTypeName("increaseSkill_request")

public class IncreaseSkillRequest {

  private String characterId;

  private String skillId;

  private BigDecimal experienceGained;

  private @Nullable BigDecimal difficultyModifier;

  public IncreaseSkillRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IncreaseSkillRequest(String characterId, String skillId, BigDecimal experienceGained) {
    this.characterId = characterId;
    this.skillId = skillId;
    this.experienceGained = experienceGained;
  }

  public IncreaseSkillRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public IncreaseSkillRequest skillId(String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  @NotNull 
  @Schema(name = "skill_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skill_id")
  public String getSkillId() {
    return skillId;
  }

  public void setSkillId(String skillId) {
    this.skillId = skillId;
  }

  public IncreaseSkillRequest experienceGained(BigDecimal experienceGained) {
    this.experienceGained = experienceGained;
    return this;
  }

  /**
   * Get experienceGained
   * @return experienceGained
   */
  @NotNull @Valid 
  @Schema(name = "experience_gained", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("experience_gained")
  public BigDecimal getExperienceGained() {
    return experienceGained;
  }

  public void setExperienceGained(BigDecimal experienceGained) {
    this.experienceGained = experienceGained;
  }

  public IncreaseSkillRequest difficultyModifier(@Nullable BigDecimal difficultyModifier) {
    this.difficultyModifier = difficultyModifier;
    return this;
  }

  /**
   * Модификатор сложности ситуации (x1.5 для сложных ситуаций)
   * @return difficultyModifier
   */
  @Valid 
  @Schema(name = "difficulty_modifier", description = "Модификатор сложности ситуации (x1.5 для сложных ситуаций)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty_modifier")
  public @Nullable BigDecimal getDifficultyModifier() {
    return difficultyModifier;
  }

  public void setDifficultyModifier(@Nullable BigDecimal difficultyModifier) {
    this.difficultyModifier = difficultyModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncreaseSkillRequest increaseSkillRequest = (IncreaseSkillRequest) o;
    return Objects.equals(this.characterId, increaseSkillRequest.characterId) &&
        Objects.equals(this.skillId, increaseSkillRequest.skillId) &&
        Objects.equals(this.experienceGained, increaseSkillRequest.experienceGained) &&
        Objects.equals(this.difficultyModifier, increaseSkillRequest.difficultyModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, skillId, experienceGained, difficultyModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncreaseSkillRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    experienceGained: ").append(toIndentedString(experienceGained)).append("\n");
    sb.append("    difficultyModifier: ").append(toIndentedString(difficultyModifier)).append("\n");
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

