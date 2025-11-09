package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.SkillExperienceResultRewards;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SkillExperienceResult
 */


public class SkillExperienceResult {

  private @Nullable String skillId;

  private @Nullable Integer experienceAdded;

  private @Nullable Integer previousLevel;

  private @Nullable Integer newLevel;

  private @Nullable Boolean leveledUp;

  private JsonNullable<SkillExperienceResultRewards> rewards = JsonNullable.<SkillExperienceResultRewards>undefined();

  public SkillExperienceResult skillId(@Nullable String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  
  @Schema(name = "skill_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_id")
  public @Nullable String getSkillId() {
    return skillId;
  }

  public void setSkillId(@Nullable String skillId) {
    this.skillId = skillId;
  }

  public SkillExperienceResult experienceAdded(@Nullable Integer experienceAdded) {
    this.experienceAdded = experienceAdded;
    return this;
  }

  /**
   * Get experienceAdded
   * @return experienceAdded
   */
  
  @Schema(name = "experience_added", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_added")
  public @Nullable Integer getExperienceAdded() {
    return experienceAdded;
  }

  public void setExperienceAdded(@Nullable Integer experienceAdded) {
    this.experienceAdded = experienceAdded;
  }

  public SkillExperienceResult previousLevel(@Nullable Integer previousLevel) {
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

  public SkillExperienceResult newLevel(@Nullable Integer newLevel) {
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

  public SkillExperienceResult leveledUp(@Nullable Boolean leveledUp) {
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

  public SkillExperienceResult rewards(SkillExperienceResultRewards rewards) {
    this.rewards = JsonNullable.of(rewards);
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public JsonNullable<SkillExperienceResultRewards> getRewards() {
    return rewards;
  }

  public void setRewards(JsonNullable<SkillExperienceResultRewards> rewards) {
    this.rewards = rewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillExperienceResult skillExperienceResult = (SkillExperienceResult) o;
    return Objects.equals(this.skillId, skillExperienceResult.skillId) &&
        Objects.equals(this.experienceAdded, skillExperienceResult.experienceAdded) &&
        Objects.equals(this.previousLevel, skillExperienceResult.previousLevel) &&
        Objects.equals(this.newLevel, skillExperienceResult.newLevel) &&
        Objects.equals(this.leveledUp, skillExperienceResult.leveledUp) &&
        equalsNullable(this.rewards, skillExperienceResult.rewards);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillId, experienceAdded, previousLevel, newLevel, leveledUp, hashCodeNullable(rewards));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillExperienceResult {\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    experienceAdded: ").append(toIndentedString(experienceAdded)).append("\n");
    sb.append("    previousLevel: ").append(toIndentedString(previousLevel)).append("\n");
    sb.append("    newLevel: ").append(toIndentedString(newLevel)).append("\n");
    sb.append("    leveledUp: ").append(toIndentedString(leveledUp)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
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

