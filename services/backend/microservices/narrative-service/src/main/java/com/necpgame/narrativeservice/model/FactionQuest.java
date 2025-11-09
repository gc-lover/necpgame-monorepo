package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.QuestRequirements;
import com.necpgame.narrativeservice.model.QuestRewards;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FactionQuest
 */


public class FactionQuest {

  private @Nullable String questId;

  private @Nullable String title;

  private @Nullable String description;

  /**
   * Gets or Sets faction
   */
  public enum FactionEnum {
    NCPD("NCPD"),
    
    MAXTAC("MAXTAC"),
    
    ARASAKA("ARASAKA"),
    
    SIXTH_STREET("SIXTH_STREET"),
    
    VOODOO_BOYS("VOODOO_BOYS"),
    
    ALDECALDOS("ALDECALDOS"),
    
    MILITECH("MILITECH"),
    
    BIOTECHNICA("BIOTECHNICA"),
    
    VALENTINOS("VALENTINOS"),
    
    MAELSTROM("MAELSTROM"),
    
    FIXERS("FIXERS"),
    
    RIPPERS("RIPPERS"),
    
    TRAUMA_TEAM("TRAUMA_TEAM"),
    
    NETRUNNERS("NETRUNNERS"),
    
    MEDIA("MEDIA"),
    
    POLITICS("POLITICS");

    private final String value;

    FactionEnum(String value) {
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
    public static FactionEnum fromValue(String value) {
      for (FactionEnum b : FactionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable FactionEnum faction;

  private @Nullable String category;

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

  private @Nullable QuestRequirements requirements;

  private @Nullable QuestRewards rewards;

  private @Nullable Integer branchesCount;

  private @Nullable Integer endingsCount;

  private @Nullable Integer estimatedTimeMinutes;

  public FactionQuest questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "quest_id", example = "faction_ncpd_serial_killer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

  public FactionQuest title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", example = "Night City's Most Wanted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public FactionQuest description(@Nullable String description) {
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

  public FactionQuest faction(@Nullable FactionEnum faction) {
    this.faction = faction;
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public @Nullable FactionEnum getFaction() {
    return faction;
  }

  public void setFaction(@Nullable FactionEnum faction) {
    this.faction = faction;
  }

  public FactionQuest category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", example = "Investigation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public FactionQuest difficulty(@Nullable DifficultyEnum difficulty) {
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

  public FactionQuest requirements(@Nullable QuestRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable QuestRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable QuestRequirements requirements) {
    this.requirements = requirements;
  }

  public FactionQuest rewards(@Nullable QuestRewards rewards) {
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
  public @Nullable QuestRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable QuestRewards rewards) {
    this.rewards = rewards;
  }

  public FactionQuest branchesCount(@Nullable Integer branchesCount) {
    this.branchesCount = branchesCount;
    return this;
  }

  /**
   * Количество веток
   * @return branchesCount
   */
  
  @Schema(name = "branches_count", example = "5", description = "Количество веток", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branches_count")
  public @Nullable Integer getBranchesCount() {
    return branchesCount;
  }

  public void setBranchesCount(@Nullable Integer branchesCount) {
    this.branchesCount = branchesCount;
  }

  public FactionQuest endingsCount(@Nullable Integer endingsCount) {
    this.endingsCount = endingsCount;
    return this;
  }

  /**
   * Количество концовок
   * @return endingsCount
   */
  
  @Schema(name = "endings_count", example = "12", description = "Количество концовок", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endings_count")
  public @Nullable Integer getEndingsCount() {
    return endingsCount;
  }

  public void setEndingsCount(@Nullable Integer endingsCount) {
    this.endingsCount = endingsCount;
  }

  public FactionQuest estimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
    return this;
  }

  /**
   * Get estimatedTimeMinutes
   * @return estimatedTimeMinutes
   */
  
  @Schema(name = "estimated_time_minutes", example = "45", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time_minutes")
  public @Nullable Integer getEstimatedTimeMinutes() {
    return estimatedTimeMinutes;
  }

  public void setEstimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionQuest factionQuest = (FactionQuest) o;
    return Objects.equals(this.questId, factionQuest.questId) &&
        Objects.equals(this.title, factionQuest.title) &&
        Objects.equals(this.description, factionQuest.description) &&
        Objects.equals(this.faction, factionQuest.faction) &&
        Objects.equals(this.category, factionQuest.category) &&
        Objects.equals(this.difficulty, factionQuest.difficulty) &&
        Objects.equals(this.requirements, factionQuest.requirements) &&
        Objects.equals(this.rewards, factionQuest.rewards) &&
        Objects.equals(this.branchesCount, factionQuest.branchesCount) &&
        Objects.equals(this.endingsCount, factionQuest.endingsCount) &&
        Objects.equals(this.estimatedTimeMinutes, factionQuest.estimatedTimeMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, title, description, faction, category, difficulty, requirements, rewards, branchesCount, endingsCount, estimatedTimeMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionQuest {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    branchesCount: ").append(toIndentedString(branchesCount)).append("\n");
    sb.append("    endingsCount: ").append(toIndentedString(endingsCount)).append("\n");
    sb.append("    estimatedTimeMinutes: ").append(toIndentedString(estimatedTimeMinutes)).append("\n");
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

