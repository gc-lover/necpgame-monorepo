package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.WeeklyQuestRewards;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WeeklyQuest
 */


public class WeeklyQuest {

  private @Nullable String questId;

  private @Nullable String title;

  private @Nullable String description;

  private @Nullable String region;

  /**
   * Gets or Sets difficulty
   */
  public enum DifficultyEnum {
    MEDIUM("MEDIUM"),
    
    HARD("HARD"),
    
    VERY_HARD("VERY_HARD");

    private final String value;

    DifficultyEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DifficultyEnum fromValue(String value) {
      for (DifficultyEnum b : DifficultyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DifficultyEnum difficulty;

  @Valid
  private List<Object> objectives = new ArrayList<>();

  private @Nullable WeeklyQuestRewards rewards;

  private Integer timeLimitHours = 168;

  private @Nullable Float progressPercentage;

  public WeeklyQuest questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

  public WeeklyQuest title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public WeeklyQuest description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public WeeklyQuest region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public WeeklyQuest difficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  
  @Schema(name = "difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable DifficultyEnum getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
  }

  public WeeklyQuest objectives(List<Object> objectives) {
    this.objectives = objectives;
    return this;
  }

  public WeeklyQuest addObjectivesItem(Object objectivesItem) {
    if (this.objectives == null) {
      this.objectives = new ArrayList<>();
    }
    this.objectives.add(objectivesItem);
    return this;
  }

  /**
   * Get objectives
   * @return objectives
   */
  
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectives")
  public List<Object> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<Object> objectives) {
    this.objectives = objectives;
  }

  public WeeklyQuest rewards(@Nullable WeeklyQuestRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable WeeklyQuestRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable WeeklyQuestRewards rewards) {
    this.rewards = rewards;
  }

  public WeeklyQuest timeLimitHours(Integer timeLimitHours) {
    this.timeLimitHours = timeLimitHours;
    return this;
  }

  /**
   * Get timeLimitHours
   * @return timeLimitHours
   */
  
  @Schema(name = "time_limit_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_limit_hours")
  public Integer getTimeLimitHours() {
    return timeLimitHours;
  }

  public void setTimeLimitHours(Integer timeLimitHours) {
    this.timeLimitHours = timeLimitHours;
  }

  public WeeklyQuest progressPercentage(@Nullable Float progressPercentage) {
    this.progressPercentage = progressPercentage;
    return this;
  }

  /**
   * Get progressPercentage
   * @return progressPercentage
   */
  
  @Schema(name = "progress_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress_percentage")
  public @Nullable Float getProgressPercentage() {
    return progressPercentage;
  }

  public void setProgressPercentage(@Nullable Float progressPercentage) {
    this.progressPercentage = progressPercentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeeklyQuest weeklyQuest = (WeeklyQuest) o;
    return Objects.equals(this.questId, weeklyQuest.questId) &&
        Objects.equals(this.title, weeklyQuest.title) &&
        Objects.equals(this.description, weeklyQuest.description) &&
        Objects.equals(this.region, weeklyQuest.region) &&
        Objects.equals(this.difficulty, weeklyQuest.difficulty) &&
        Objects.equals(this.objectives, weeklyQuest.objectives) &&
        Objects.equals(this.rewards, weeklyQuest.rewards) &&
        Objects.equals(this.timeLimitHours, weeklyQuest.timeLimitHours) &&
        Objects.equals(this.progressPercentage, weeklyQuest.progressPercentage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, title, description, region, difficulty, objectives, rewards, timeLimitHours, progressPercentage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeeklyQuest {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    timeLimitHours: ").append(toIndentedString(timeLimitHours)).append("\n");
    sb.append("    progressPercentage: ").append(toIndentedString(progressPercentage)).append("\n");
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

