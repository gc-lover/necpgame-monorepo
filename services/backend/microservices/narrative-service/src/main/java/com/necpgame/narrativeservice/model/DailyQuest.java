package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.DailyQuestObjectivesInner;
import com.necpgame.narrativeservice.model.DailyQuestRewards;
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
 * DailyQuest
 */


public class DailyQuest {

  private @Nullable String questId;

  private @Nullable String title;

  private @Nullable String description;

  private @Nullable String region;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    COMBAT("COMBAT"),
    
    GATHER("GATHER"),
    
    CRAFT("CRAFT"),
    
    SOCIAL("SOCIAL"),
    
    EXPLORATION("EXPLORATION");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  @Valid
  private List<@Valid DailyQuestObjectivesInner> objectives = new ArrayList<>();

  private @Nullable DailyQuestRewards rewards;

  private Integer timeLimitHours = 24;

  private Boolean repeatable = true;

  private @Nullable Integer completionsToday;

  public DailyQuest questId(@Nullable String questId) {
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

  public DailyQuest title(@Nullable String title) {
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

  public DailyQuest description(@Nullable String description) {
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

  public DailyQuest region(@Nullable String region) {
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

  public DailyQuest type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public DailyQuest objectives(List<@Valid DailyQuestObjectivesInner> objectives) {
    this.objectives = objectives;
    return this;
  }

  public DailyQuest addObjectivesItem(DailyQuestObjectivesInner objectivesItem) {
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
  @Valid 
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectives")
  public List<@Valid DailyQuestObjectivesInner> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<@Valid DailyQuestObjectivesInner> objectives) {
    this.objectives = objectives;
  }

  public DailyQuest rewards(@Nullable DailyQuestRewards rewards) {
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
  public @Nullable DailyQuestRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable DailyQuestRewards rewards) {
    this.rewards = rewards;
  }

  public DailyQuest timeLimitHours(Integer timeLimitHours) {
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

  public DailyQuest repeatable(Boolean repeatable) {
    this.repeatable = repeatable;
    return this;
  }

  /**
   * Get repeatable
   * @return repeatable
   */
  
  @Schema(name = "repeatable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("repeatable")
  public Boolean getRepeatable() {
    return repeatable;
  }

  public void setRepeatable(Boolean repeatable) {
    this.repeatable = repeatable;
  }

  public DailyQuest completionsToday(@Nullable Integer completionsToday) {
    this.completionsToday = completionsToday;
    return this;
  }

  /**
   * Сколько раз уже выполнено сегодня
   * @return completionsToday
   */
  
  @Schema(name = "completions_today", description = "Сколько раз уже выполнено сегодня", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completions_today")
  public @Nullable Integer getCompletionsToday() {
    return completionsToday;
  }

  public void setCompletionsToday(@Nullable Integer completionsToday) {
    this.completionsToday = completionsToday;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DailyQuest dailyQuest = (DailyQuest) o;
    return Objects.equals(this.questId, dailyQuest.questId) &&
        Objects.equals(this.title, dailyQuest.title) &&
        Objects.equals(this.description, dailyQuest.description) &&
        Objects.equals(this.region, dailyQuest.region) &&
        Objects.equals(this.type, dailyQuest.type) &&
        Objects.equals(this.objectives, dailyQuest.objectives) &&
        Objects.equals(this.rewards, dailyQuest.rewards) &&
        Objects.equals(this.timeLimitHours, dailyQuest.timeLimitHours) &&
        Objects.equals(this.repeatable, dailyQuest.repeatable) &&
        Objects.equals(this.completionsToday, dailyQuest.completionsToday);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, title, description, region, type, objectives, rewards, timeLimitHours, repeatable, completionsToday);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DailyQuest {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    timeLimitHours: ").append(toIndentedString(timeLimitHours)).append("\n");
    sb.append("    repeatable: ").append(toIndentedString(repeatable)).append("\n");
    sb.append("    completionsToday: ").append(toIndentedString(completionsToday)).append("\n");
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

