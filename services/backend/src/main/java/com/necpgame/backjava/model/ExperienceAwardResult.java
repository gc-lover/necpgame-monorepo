package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.LevelUpRewards;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExperienceAwardResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ExperienceAwardResult {

  private @Nullable Integer experienceAwarded;

  private @Nullable Integer previousLevel;

  private @Nullable Integer newLevel;

  private @Nullable Boolean leveledUp;

  private @Nullable Integer newExperienceTotal;

  private @Nullable LevelUpRewards levelUpRewards;

  public ExperienceAwardResult experienceAwarded(@Nullable Integer experienceAwarded) {
    this.experienceAwarded = experienceAwarded;
    return this;
  }

  /**
   * Get experienceAwarded
   * @return experienceAwarded
   */
  
  @Schema(name = "experience_awarded", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_awarded")
  public @Nullable Integer getExperienceAwarded() {
    return experienceAwarded;
  }

  public void setExperienceAwarded(@Nullable Integer experienceAwarded) {
    this.experienceAwarded = experienceAwarded;
  }

  public ExperienceAwardResult previousLevel(@Nullable Integer previousLevel) {
    this.previousLevel = previousLevel;
    return this;
  }

  /**
   * Get previousLevel
   * @return previousLevel
   */
  
  @Schema(name = "previous_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_level")
  public @Nullable Integer getPreviousLevel() {
    return previousLevel;
  }

  public void setPreviousLevel(@Nullable Integer previousLevel) {
    this.previousLevel = previousLevel;
  }

  public ExperienceAwardResult newLevel(@Nullable Integer newLevel) {
    this.newLevel = newLevel;
    return this;
  }

  /**
   * Get newLevel
   * @return newLevel
   */
  
  @Schema(name = "new_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_level")
  public @Nullable Integer getNewLevel() {
    return newLevel;
  }

  public void setNewLevel(@Nullable Integer newLevel) {
    this.newLevel = newLevel;
  }

  public ExperienceAwardResult leveledUp(@Nullable Boolean leveledUp) {
    this.leveledUp = leveledUp;
    return this;
  }

  /**
   * Get leveledUp
   * @return leveledUp
   */
  
  @Schema(name = "leveled_up", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leveled_up")
  public @Nullable Boolean getLeveledUp() {
    return leveledUp;
  }

  public void setLeveledUp(@Nullable Boolean leveledUp) {
    this.leveledUp = leveledUp;
  }

  public ExperienceAwardResult newExperienceTotal(@Nullable Integer newExperienceTotal) {
    this.newExperienceTotal = newExperienceTotal;
    return this;
  }

  /**
   * Get newExperienceTotal
   * @return newExperienceTotal
   */
  
  @Schema(name = "new_experience_total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_experience_total")
  public @Nullable Integer getNewExperienceTotal() {
    return newExperienceTotal;
  }

  public void setNewExperienceTotal(@Nullable Integer newExperienceTotal) {
    this.newExperienceTotal = newExperienceTotal;
  }

  public ExperienceAwardResult levelUpRewards(@Nullable LevelUpRewards levelUpRewards) {
    this.levelUpRewards = levelUpRewards;
    return this;
  }

  /**
   * Get levelUpRewards
   * @return levelUpRewards
   */
  @Valid 
  @Schema(name = "level_up_rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_up_rewards")
  public @Nullable LevelUpRewards getLevelUpRewards() {
    return levelUpRewards;
  }

  public void setLevelUpRewards(@Nullable LevelUpRewards levelUpRewards) {
    this.levelUpRewards = levelUpRewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExperienceAwardResult experienceAwardResult = (ExperienceAwardResult) o;
    return Objects.equals(this.experienceAwarded, experienceAwardResult.experienceAwarded) &&
        Objects.equals(this.previousLevel, experienceAwardResult.previousLevel) &&
        Objects.equals(this.newLevel, experienceAwardResult.newLevel) &&
        Objects.equals(this.leveledUp, experienceAwardResult.leveledUp) &&
        Objects.equals(this.newExperienceTotal, experienceAwardResult.newExperienceTotal) &&
        Objects.equals(this.levelUpRewards, experienceAwardResult.levelUpRewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experienceAwarded, previousLevel, newLevel, leveledUp, newExperienceTotal, levelUpRewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExperienceAwardResult {\n");
    sb.append("    experienceAwarded: ").append(toIndentedString(experienceAwarded)).append("\n");
    sb.append("    previousLevel: ").append(toIndentedString(previousLevel)).append("\n");
    sb.append("    newLevel: ").append(toIndentedString(newLevel)).append("\n");
    sb.append("    leveledUp: ").append(toIndentedString(leveledUp)).append("\n");
    sb.append("    newExperienceTotal: ").append(toIndentedString(newExperienceTotal)).append("\n");
    sb.append("    levelUpRewards: ").append(toIndentedString(levelUpRewards)).append("\n");
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

