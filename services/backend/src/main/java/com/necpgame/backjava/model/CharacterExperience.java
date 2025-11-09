package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterExperience
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CharacterExperience {

  private @Nullable UUID characterId;

  private @Nullable Integer level;

  private @Nullable Integer experience;

  private @Nullable Integer experienceToNextLevel;

  private @Nullable Float progressToNextLevel;

  private @Nullable Integer totalExperienceEarned;

  private @Nullable Integer levelCap;

  public CharacterExperience characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public CharacterExperience level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", example = "15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public CharacterExperience experience(@Nullable Integer experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  
  @Schema(name = "experience", example = "45000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable Integer getExperience() {
    return experience;
  }

  public void setExperience(@Nullable Integer experience) {
    this.experience = experience;
  }

  public CharacterExperience experienceToNextLevel(@Nullable Integer experienceToNextLevel) {
    this.experienceToNextLevel = experienceToNextLevel;
    return this;
  }

  /**
   * Get experienceToNextLevel
   * @return experienceToNextLevel
   */
  
  @Schema(name = "experience_to_next_level", example = "50000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_to_next_level")
  public @Nullable Integer getExperienceToNextLevel() {
    return experienceToNextLevel;
  }

  public void setExperienceToNextLevel(@Nullable Integer experienceToNextLevel) {
    this.experienceToNextLevel = experienceToNextLevel;
  }

  public CharacterExperience progressToNextLevel(@Nullable Float progressToNextLevel) {
    this.progressToNextLevel = progressToNextLevel;
    return this;
  }

  /**
   * Процент прогресса
   * @return progressToNextLevel
   */
  
  @Schema(name = "progress_to_next_level", example = "90.0", description = "Процент прогресса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress_to_next_level")
  public @Nullable Float getProgressToNextLevel() {
    return progressToNextLevel;
  }

  public void setProgressToNextLevel(@Nullable Float progressToNextLevel) {
    this.progressToNextLevel = progressToNextLevel;
  }

  public CharacterExperience totalExperienceEarned(@Nullable Integer totalExperienceEarned) {
    this.totalExperienceEarned = totalExperienceEarned;
    return this;
  }

  /**
   * Get totalExperienceEarned
   * @return totalExperienceEarned
   */
  
  @Schema(name = "total_experience_earned", example = "245000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_experience_earned")
  public @Nullable Integer getTotalExperienceEarned() {
    return totalExperienceEarned;
  }

  public void setTotalExperienceEarned(@Nullable Integer totalExperienceEarned) {
    this.totalExperienceEarned = totalExperienceEarned;
  }

  public CharacterExperience levelCap(@Nullable Integer levelCap) {
    this.levelCap = levelCap;
    return this;
  }

  /**
   * Get levelCap
   * @return levelCap
   */
  
  @Schema(name = "level_cap", example = "50", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_cap")
  public @Nullable Integer getLevelCap() {
    return levelCap;
  }

  public void setLevelCap(@Nullable Integer levelCap) {
    this.levelCap = levelCap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterExperience characterExperience = (CharacterExperience) o;
    return Objects.equals(this.characterId, characterExperience.characterId) &&
        Objects.equals(this.level, characterExperience.level) &&
        Objects.equals(this.experience, characterExperience.experience) &&
        Objects.equals(this.experienceToNextLevel, characterExperience.experienceToNextLevel) &&
        Objects.equals(this.progressToNextLevel, characterExperience.progressToNextLevel) &&
        Objects.equals(this.totalExperienceEarned, characterExperience.totalExperienceEarned) &&
        Objects.equals(this.levelCap, characterExperience.levelCap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, level, experience, experienceToNextLevel, progressToNextLevel, totalExperienceEarned, levelCap);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterExperience {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    experienceToNextLevel: ").append(toIndentedString(experienceToNextLevel)).append("\n");
    sb.append("    progressToNextLevel: ").append(toIndentedString(progressToNextLevel)).append("\n");
    sb.append("    totalExperienceEarned: ").append(toIndentedString(totalExperienceEarned)).append("\n");
    sb.append("    levelCap: ").append(toIndentedString(levelCap)).append("\n");
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

