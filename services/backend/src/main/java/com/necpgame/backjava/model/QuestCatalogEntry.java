package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.QuestCatalogEntryRewardsSummary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * QuestCatalogEntry
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestCatalogEntry {

  private @Nullable String questId;

  private @Nullable String title;

  private @Nullable String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    MAIN("MAIN"),
    
    SIDE("SIDE"),
    
    FACTION("FACTION"),
    
    DAILY("DAILY"),
    
    WEEKLY("WEEKLY"),
    
    RANDOM_EVENT("RANDOM_EVENT");

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

  private @Nullable String period;

  /**
   * Gets or Sets difficulty
   */
  public enum DifficultyEnum {
    EASY("EASY"),
    
    MEDIUM("MEDIUM"),
    
    HARD("HARD"),
    
    VERY_HARD("VERY_HARD"),
    
    EXTREME("EXTREME");

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

  private @Nullable Integer levelRequirement;

  private JsonNullable<String> faction = JsonNullable.<String>undefined();

  private @Nullable Integer estimatedTimeMinutes;

  @Valid
  private List<String> tags = new ArrayList<>();

  private @Nullable QuestCatalogEntryRewardsSummary rewardsSummary;

  private @Nullable Float completionRate;

  private @Nullable Float averageRating;

  public QuestCatalogEntry questId(@Nullable String questId) {
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

  public QuestCatalogEntry title(@Nullable String title) {
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

  public QuestCatalogEntry description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Краткое описание
   * @return description
   */
  
  @Schema(name = "description", description = "Краткое описание", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public QuestCatalogEntry type(@Nullable TypeEnum type) {
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

  public QuestCatalogEntry period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", example = "2060-2077", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public QuestCatalogEntry difficulty(@Nullable DifficultyEnum difficulty) {
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

  public QuestCatalogEntry levelRequirement(@Nullable Integer levelRequirement) {
    this.levelRequirement = levelRequirement;
    return this;
  }

  /**
   * Get levelRequirement
   * @return levelRequirement
   */
  
  @Schema(name = "level_requirement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_requirement")
  public @Nullable Integer getLevelRequirement() {
    return levelRequirement;
  }

  public void setLevelRequirement(@Nullable Integer levelRequirement) {
    this.levelRequirement = levelRequirement;
  }

  public QuestCatalogEntry faction(String faction) {
    this.faction = JsonNullable.of(faction);
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public JsonNullable<String> getFaction() {
    return faction;
  }

  public void setFaction(JsonNullable<String> faction) {
    this.faction = faction;
  }

  public QuestCatalogEntry estimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
    return this;
  }

  /**
   * Get estimatedTimeMinutes
   * @return estimatedTimeMinutes
   */
  
  @Schema(name = "estimated_time_minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time_minutes")
  public @Nullable Integer getEstimatedTimeMinutes() {
    return estimatedTimeMinutes;
  }

  public void setEstimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
  }

  public QuestCatalogEntry tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public QuestCatalogEntry addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  
  @Schema(name = "tags", example = "[\"combat\",\"hacking\",\"social\",\"romance\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public QuestCatalogEntry rewardsSummary(@Nullable QuestCatalogEntryRewardsSummary rewardsSummary) {
    this.rewardsSummary = rewardsSummary;
    return this;
  }

  /**
   * Get rewardsSummary
   * @return rewardsSummary
   */
  @Valid 
  @Schema(name = "rewards_summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards_summary")
  public @Nullable QuestCatalogEntryRewardsSummary getRewardsSummary() {
    return rewardsSummary;
  }

  public void setRewardsSummary(@Nullable QuestCatalogEntryRewardsSummary rewardsSummary) {
    this.rewardsSummary = rewardsSummary;
  }

  public QuestCatalogEntry completionRate(@Nullable Float completionRate) {
    this.completionRate = completionRate;
    return this;
  }

  /**
   * Процент игроков, завершивших квест
   * @return completionRate
   */
  
  @Schema(name = "completion_rate", description = "Процент игроков, завершивших квест", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completion_rate")
  public @Nullable Float getCompletionRate() {
    return completionRate;
  }

  public void setCompletionRate(@Nullable Float completionRate) {
    this.completionRate = completionRate;
  }

  public QuestCatalogEntry averageRating(@Nullable Float averageRating) {
    this.averageRating = averageRating;
    return this;
  }

  /**
   * Средняя оценка игроков
   * @return averageRating
   */
  
  @Schema(name = "average_rating", description = "Средняя оценка игроков", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_rating")
  public @Nullable Float getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(@Nullable Float averageRating) {
    this.averageRating = averageRating;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestCatalogEntry questCatalogEntry = (QuestCatalogEntry) o;
    return Objects.equals(this.questId, questCatalogEntry.questId) &&
        Objects.equals(this.title, questCatalogEntry.title) &&
        Objects.equals(this.description, questCatalogEntry.description) &&
        Objects.equals(this.type, questCatalogEntry.type) &&
        Objects.equals(this.period, questCatalogEntry.period) &&
        Objects.equals(this.difficulty, questCatalogEntry.difficulty) &&
        Objects.equals(this.levelRequirement, questCatalogEntry.levelRequirement) &&
        equalsNullable(this.faction, questCatalogEntry.faction) &&
        Objects.equals(this.estimatedTimeMinutes, questCatalogEntry.estimatedTimeMinutes) &&
        Objects.equals(this.tags, questCatalogEntry.tags) &&
        Objects.equals(this.rewardsSummary, questCatalogEntry.rewardsSummary) &&
        Objects.equals(this.completionRate, questCatalogEntry.completionRate) &&
        Objects.equals(this.averageRating, questCatalogEntry.averageRating);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, title, description, type, period, difficulty, levelRequirement, hashCodeNullable(faction), estimatedTimeMinutes, tags, rewardsSummary, completionRate, averageRating);
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
    sb.append("class QuestCatalogEntry {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    levelRequirement: ").append(toIndentedString(levelRequirement)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    estimatedTimeMinutes: ").append(toIndentedString(estimatedTimeMinutes)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    rewardsSummary: ").append(toIndentedString(rewardsSummary)).append("\n");
    sb.append("    completionRate: ").append(toIndentedString(completionRate)).append("\n");
    sb.append("    averageRating: ").append(toIndentedString(averageRating)).append("\n");
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

