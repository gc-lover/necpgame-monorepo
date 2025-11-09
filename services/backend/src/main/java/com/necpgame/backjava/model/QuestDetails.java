package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.QuestCatalogEntryRewardsSummary;
import com.necpgame.backjava.model.QuestDetailsAllOfKeyNpcs;
import com.necpgame.backjava.model.QuestDetailsAllOfObjectives;
import com.necpgame.backjava.model.QuestDetailsAllOfRewardsDetailed;
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
 * QuestDetails
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestDetails {

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

  private @Nullable String fullDescription;

  private @Nullable String storyline;

  @Valid
  private List<@Valid QuestDetailsAllOfObjectives> objectives = new ArrayList<>();

  @Valid
  private List<@Valid QuestDetailsAllOfKeyNpcs> keyNpcs = new ArrayList<>();

  @Valid
  private List<String> locations = new ArrayList<>();

  @Valid
  private List<String> prerequisites = new ArrayList<>();

  @Valid
  private List<String> unlocks = new ArrayList<>();

  private @Nullable Integer branchesCount;

  private @Nullable Integer endingsCount;

  private @Nullable Boolean hasDialogueTree;

  private @Nullable Boolean hasSkillChecks;

  private @Nullable Boolean hasCombat;

  private @Nullable Boolean hasRomance;

  private @Nullable QuestDetailsAllOfRewardsDetailed rewardsDetailed;

  public QuestDetails questId(@Nullable String questId) {
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

  public QuestDetails title(@Nullable String title) {
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

  public QuestDetails description(@Nullable String description) {
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

  public QuestDetails type(@Nullable TypeEnum type) {
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

  public QuestDetails period(@Nullable String period) {
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

  public QuestDetails difficulty(@Nullable DifficultyEnum difficulty) {
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

  public QuestDetails levelRequirement(@Nullable Integer levelRequirement) {
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

  public QuestDetails faction(String faction) {
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

  public QuestDetails estimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
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

  public QuestDetails tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public QuestDetails addTagsItem(String tagsItem) {
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
  
  @Schema(name = "tags", example = "[combat, hacking, social, romance]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public QuestDetails rewardsSummary(@Nullable QuestCatalogEntryRewardsSummary rewardsSummary) {
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

  public QuestDetails completionRate(@Nullable Float completionRate) {
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

  public QuestDetails averageRating(@Nullable Float averageRating) {
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

  public QuestDetails fullDescription(@Nullable String fullDescription) {
    this.fullDescription = fullDescription;
    return this;
  }

  /**
   * Get fullDescription
   * @return fullDescription
   */
  
  @Schema(name = "full_description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("full_description")
  public @Nullable String getFullDescription() {
    return fullDescription;
  }

  public void setFullDescription(@Nullable String fullDescription) {
    this.fullDescription = fullDescription;
  }

  public QuestDetails storyline(@Nullable String storyline) {
    this.storyline = storyline;
    return this;
  }

  /**
   * Get storyline
   * @return storyline
   */
  
  @Schema(name = "storyline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("storyline")
  public @Nullable String getStoryline() {
    return storyline;
  }

  public void setStoryline(@Nullable String storyline) {
    this.storyline = storyline;
  }

  public QuestDetails objectives(List<@Valid QuestDetailsAllOfObjectives> objectives) {
    this.objectives = objectives;
    return this;
  }

  public QuestDetails addObjectivesItem(QuestDetailsAllOfObjectives objectivesItem) {
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
  public List<@Valid QuestDetailsAllOfObjectives> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<@Valid QuestDetailsAllOfObjectives> objectives) {
    this.objectives = objectives;
  }

  public QuestDetails keyNpcs(List<@Valid QuestDetailsAllOfKeyNpcs> keyNpcs) {
    this.keyNpcs = keyNpcs;
    return this;
  }

  public QuestDetails addKeyNpcsItem(QuestDetailsAllOfKeyNpcs keyNpcsItem) {
    if (this.keyNpcs == null) {
      this.keyNpcs = new ArrayList<>();
    }
    this.keyNpcs.add(keyNpcsItem);
    return this;
  }

  /**
   * Get keyNpcs
   * @return keyNpcs
   */
  @Valid 
  @Schema(name = "key_npcs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key_npcs")
  public List<@Valid QuestDetailsAllOfKeyNpcs> getKeyNpcs() {
    return keyNpcs;
  }

  public void setKeyNpcs(List<@Valid QuestDetailsAllOfKeyNpcs> keyNpcs) {
    this.keyNpcs = keyNpcs;
  }

  public QuestDetails locations(List<String> locations) {
    this.locations = locations;
    return this;
  }

  public QuestDetails addLocationsItem(String locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }
    this.locations.add(locationsItem);
    return this;
  }

  /**
   * Get locations
   * @return locations
   */
  
  @Schema(name = "locations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locations")
  public List<String> getLocations() {
    return locations;
  }

  public void setLocations(List<String> locations) {
    this.locations = locations;
  }

  public QuestDetails prerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
    return this;
  }

  public QuestDetails addPrerequisitesItem(String prerequisitesItem) {
    if (this.prerequisites == null) {
      this.prerequisites = new ArrayList<>();
    }
    this.prerequisites.add(prerequisitesItem);
    return this;
  }

  /**
   * Квесты, которые нужно завершить перед этим
   * @return prerequisites
   */
  
  @Schema(name = "prerequisites", description = "Квесты, которые нужно завершить перед этим", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prerequisites")
  public List<String> getPrerequisites() {
    return prerequisites;
  }

  public void setPrerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
  }

  public QuestDetails unlocks(List<String> unlocks) {
    this.unlocks = unlocks;
    return this;
  }

  public QuestDetails addUnlocksItem(String unlocksItem) {
    if (this.unlocks == null) {
      this.unlocks = new ArrayList<>();
    }
    this.unlocks.add(unlocksItem);
    return this;
  }

  /**
   * Что открывается после завершения
   * @return unlocks
   */
  
  @Schema(name = "unlocks", description = "Что открывается после завершения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocks")
  public List<String> getUnlocks() {
    return unlocks;
  }

  public void setUnlocks(List<String> unlocks) {
    this.unlocks = unlocks;
  }

  public QuestDetails branchesCount(@Nullable Integer branchesCount) {
    this.branchesCount = branchesCount;
    return this;
  }

  /**
   * Get branchesCount
   * @return branchesCount
   */
  
  @Schema(name = "branches_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branches_count")
  public @Nullable Integer getBranchesCount() {
    return branchesCount;
  }

  public void setBranchesCount(@Nullable Integer branchesCount) {
    this.branchesCount = branchesCount;
  }

  public QuestDetails endingsCount(@Nullable Integer endingsCount) {
    this.endingsCount = endingsCount;
    return this;
  }

  /**
   * Get endingsCount
   * @return endingsCount
   */
  
  @Schema(name = "endings_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endings_count")
  public @Nullable Integer getEndingsCount() {
    return endingsCount;
  }

  public void setEndingsCount(@Nullable Integer endingsCount) {
    this.endingsCount = endingsCount;
  }

  public QuestDetails hasDialogueTree(@Nullable Boolean hasDialogueTree) {
    this.hasDialogueTree = hasDialogueTree;
    return this;
  }

  /**
   * Get hasDialogueTree
   * @return hasDialogueTree
   */
  
  @Schema(name = "has_dialogue_tree", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("has_dialogue_tree")
  public @Nullable Boolean getHasDialogueTree() {
    return hasDialogueTree;
  }

  public void setHasDialogueTree(@Nullable Boolean hasDialogueTree) {
    this.hasDialogueTree = hasDialogueTree;
  }

  public QuestDetails hasSkillChecks(@Nullable Boolean hasSkillChecks) {
    this.hasSkillChecks = hasSkillChecks;
    return this;
  }

  /**
   * Get hasSkillChecks
   * @return hasSkillChecks
   */
  
  @Schema(name = "has_skill_checks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("has_skill_checks")
  public @Nullable Boolean getHasSkillChecks() {
    return hasSkillChecks;
  }

  public void setHasSkillChecks(@Nullable Boolean hasSkillChecks) {
    this.hasSkillChecks = hasSkillChecks;
  }

  public QuestDetails hasCombat(@Nullable Boolean hasCombat) {
    this.hasCombat = hasCombat;
    return this;
  }

  /**
   * Get hasCombat
   * @return hasCombat
   */
  
  @Schema(name = "has_combat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("has_combat")
  public @Nullable Boolean getHasCombat() {
    return hasCombat;
  }

  public void setHasCombat(@Nullable Boolean hasCombat) {
    this.hasCombat = hasCombat;
  }

  public QuestDetails hasRomance(@Nullable Boolean hasRomance) {
    this.hasRomance = hasRomance;
    return this;
  }

  /**
   * Get hasRomance
   * @return hasRomance
   */
  
  @Schema(name = "has_romance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("has_romance")
  public @Nullable Boolean getHasRomance() {
    return hasRomance;
  }

  public void setHasRomance(@Nullable Boolean hasRomance) {
    this.hasRomance = hasRomance;
  }

  public QuestDetails rewardsDetailed(@Nullable QuestDetailsAllOfRewardsDetailed rewardsDetailed) {
    this.rewardsDetailed = rewardsDetailed;
    return this;
  }

  /**
   * Get rewardsDetailed
   * @return rewardsDetailed
   */
  @Valid 
  @Schema(name = "rewards_detailed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards_detailed")
  public @Nullable QuestDetailsAllOfRewardsDetailed getRewardsDetailed() {
    return rewardsDetailed;
  }

  public void setRewardsDetailed(@Nullable QuestDetailsAllOfRewardsDetailed rewardsDetailed) {
    this.rewardsDetailed = rewardsDetailed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestDetails questDetails = (QuestDetails) o;
    return Objects.equals(this.questId, questDetails.questId) &&
        Objects.equals(this.title, questDetails.title) &&
        Objects.equals(this.description, questDetails.description) &&
        Objects.equals(this.type, questDetails.type) &&
        Objects.equals(this.period, questDetails.period) &&
        Objects.equals(this.difficulty, questDetails.difficulty) &&
        Objects.equals(this.levelRequirement, questDetails.levelRequirement) &&
        equalsNullable(this.faction, questDetails.faction) &&
        Objects.equals(this.estimatedTimeMinutes, questDetails.estimatedTimeMinutes) &&
        Objects.equals(this.tags, questDetails.tags) &&
        Objects.equals(this.rewardsSummary, questDetails.rewardsSummary) &&
        Objects.equals(this.completionRate, questDetails.completionRate) &&
        Objects.equals(this.averageRating, questDetails.averageRating) &&
        Objects.equals(this.fullDescription, questDetails.fullDescription) &&
        Objects.equals(this.storyline, questDetails.storyline) &&
        Objects.equals(this.objectives, questDetails.objectives) &&
        Objects.equals(this.keyNpcs, questDetails.keyNpcs) &&
        Objects.equals(this.locations, questDetails.locations) &&
        Objects.equals(this.prerequisites, questDetails.prerequisites) &&
        Objects.equals(this.unlocks, questDetails.unlocks) &&
        Objects.equals(this.branchesCount, questDetails.branchesCount) &&
        Objects.equals(this.endingsCount, questDetails.endingsCount) &&
        Objects.equals(this.hasDialogueTree, questDetails.hasDialogueTree) &&
        Objects.equals(this.hasSkillChecks, questDetails.hasSkillChecks) &&
        Objects.equals(this.hasCombat, questDetails.hasCombat) &&
        Objects.equals(this.hasRomance, questDetails.hasRomance) &&
        Objects.equals(this.rewardsDetailed, questDetails.rewardsDetailed);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, title, description, type, period, difficulty, levelRequirement, hashCodeNullable(faction), estimatedTimeMinutes, tags, rewardsSummary, completionRate, averageRating, fullDescription, storyline, objectives, keyNpcs, locations, prerequisites, unlocks, branchesCount, endingsCount, hasDialogueTree, hasSkillChecks, hasCombat, hasRomance, rewardsDetailed);
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
    sb.append("class QuestDetails {\n");
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
    sb.append("    fullDescription: ").append(toIndentedString(fullDescription)).append("\n");
    sb.append("    storyline: ").append(toIndentedString(storyline)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    keyNpcs: ").append(toIndentedString(keyNpcs)).append("\n");
    sb.append("    locations: ").append(toIndentedString(locations)).append("\n");
    sb.append("    prerequisites: ").append(toIndentedString(prerequisites)).append("\n");
    sb.append("    unlocks: ").append(toIndentedString(unlocks)).append("\n");
    sb.append("    branchesCount: ").append(toIndentedString(branchesCount)).append("\n");
    sb.append("    endingsCount: ").append(toIndentedString(endingsCount)).append("\n");
    sb.append("    hasDialogueTree: ").append(toIndentedString(hasDialogueTree)).append("\n");
    sb.append("    hasSkillChecks: ").append(toIndentedString(hasSkillChecks)).append("\n");
    sb.append("    hasCombat: ").append(toIndentedString(hasCombat)).append("\n");
    sb.append("    hasRomance: ").append(toIndentedString(hasRomance)).append("\n");
    sb.append("    rewardsDetailed: ").append(toIndentedString(rewardsDetailed)).append("\n");
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

