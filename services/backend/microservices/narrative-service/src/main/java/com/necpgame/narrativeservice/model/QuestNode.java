package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.QuestNodeLocation;
import com.necpgame.narrativeservice.model.QuestNodeQuestGiver;
import com.necpgame.narrativeservice.model.QuestNodeRewards;
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
 * QuestNode
 */


public class QuestNode {

  private String questId;

  private String name;

  /**
   * Gets or Sets questType
   */
  public enum QuestTypeEnum {
    MAIN("main"),
    
    SIDE("side"),
    
    GIG("gig"),
    
    FACTION("faction"),
    
    ROMANCE("romance"),
    
    NCPD("ncpd"),
    
    WORLD_EVENT("world_event");

    private final String value;

    QuestTypeEnum(String value) {
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
    public static QuestTypeEnum fromValue(String value) {
      for (QuestTypeEnum b : QuestTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable QuestTypeEnum questType;

  private @Nullable Integer level;

  @Valid
  private List<String> prerequisites = new ArrayList<>();

  private @Nullable QuestNodeLocation location;

  private @Nullable QuestNodeQuestGiver questGiver;

  private @Nullable QuestNodeRewards rewards;

  @Valid
  private List<String> branches = new ArrayList<>();

  @Valid
  private List<String> unlocks = new ArrayList<>();

  public QuestNode() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QuestNode(String questId, String name) {
    this.questId = questId;
    this.name = name;
  }

  public QuestNode questId(String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  @NotNull 
  @Schema(name = "quest_id", example = "MQ-001", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quest_id")
  public String getQuestId() {
    return questId;
  }

  public void setQuestId(String questId) {
    this.questId = questId;
  }

  public QuestNode name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "The Rescue", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public QuestNode questType(@Nullable QuestTypeEnum questType) {
    this.questType = questType;
    return this;
  }

  /**
   * Get questType
   * @return questType
   */
  
  @Schema(name = "quest_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_type")
  public @Nullable QuestTypeEnum getQuestType() {
    return questType;
  }

  public void setQuestType(@Nullable QuestTypeEnum questType) {
    this.questType = questType;
  }

  public QuestNode level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Рекомендуемый уровень
   * minimum: 1
   * maximum: 60
   * @return level
   */
  @Min(value = 1) @Max(value = 60) 
  @Schema(name = "level", description = "Рекомендуемый уровень", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public QuestNode prerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
    return this;
  }

  public QuestNode addPrerequisitesItem(String prerequisitesItem) {
    if (this.prerequisites == null) {
      this.prerequisites = new ArrayList<>();
    }
    this.prerequisites.add(prerequisitesItem);
    return this;
  }

  /**
   * Quest IDs prerequisites
   * @return prerequisites
   */
  
  @Schema(name = "prerequisites", description = "Quest IDs prerequisites", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prerequisites")
  public List<String> getPrerequisites() {
    return prerequisites;
  }

  public void setPrerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
  }

  public QuestNode location(@Nullable QuestNodeLocation location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  @Valid 
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable QuestNodeLocation getLocation() {
    return location;
  }

  public void setLocation(@Nullable QuestNodeLocation location) {
    this.location = location;
  }

  public QuestNode questGiver(@Nullable QuestNodeQuestGiver questGiver) {
    this.questGiver = questGiver;
    return this;
  }

  /**
   * Get questGiver
   * @return questGiver
   */
  @Valid 
  @Schema(name = "quest_giver", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_giver")
  public @Nullable QuestNodeQuestGiver getQuestGiver() {
    return questGiver;
  }

  public void setQuestGiver(@Nullable QuestNodeQuestGiver questGiver) {
    this.questGiver = questGiver;
  }

  public QuestNode rewards(@Nullable QuestNodeRewards rewards) {
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
  public @Nullable QuestNodeRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable QuestNodeRewards rewards) {
    this.rewards = rewards;
  }

  public QuestNode branches(List<String> branches) {
    this.branches = branches;
    return this;
  }

  public QuestNode addBranchesItem(String branchesItem) {
    if (this.branches == null) {
      this.branches = new ArrayList<>();
    }
    this.branches.add(branchesItem);
    return this;
  }

  /**
   * Возможные ветвления
   * @return branches
   */
  
  @Schema(name = "branches", description = "Возможные ветвления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branches")
  public List<String> getBranches() {
    return branches;
  }

  public void setBranches(List<String> branches) {
    this.branches = branches;
  }

  public QuestNode unlocks(List<String> unlocks) {
    this.unlocks = unlocks;
    return this;
  }

  public QuestNode addUnlocksItem(String unlocksItem) {
    if (this.unlocks == null) {
      this.unlocks = new ArrayList<>();
    }
    this.unlocks.add(unlocksItem);
    return this;
  }

  /**
   * Квесты, которые разблокирует
   * @return unlocks
   */
  
  @Schema(name = "unlocks", description = "Квесты, которые разблокирует", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocks")
  public List<String> getUnlocks() {
    return unlocks;
  }

  public void setUnlocks(List<String> unlocks) {
    this.unlocks = unlocks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestNode questNode = (QuestNode) o;
    return Objects.equals(this.questId, questNode.questId) &&
        Objects.equals(this.name, questNode.name) &&
        Objects.equals(this.questType, questNode.questType) &&
        Objects.equals(this.level, questNode.level) &&
        Objects.equals(this.prerequisites, questNode.prerequisites) &&
        Objects.equals(this.location, questNode.location) &&
        Objects.equals(this.questGiver, questNode.questGiver) &&
        Objects.equals(this.rewards, questNode.rewards) &&
        Objects.equals(this.branches, questNode.branches) &&
        Objects.equals(this.unlocks, questNode.unlocks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, name, questType, level, prerequisites, location, questGiver, rewards, branches, unlocks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestNode {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    questType: ").append(toIndentedString(questType)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    prerequisites: ").append(toIndentedString(prerequisites)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    questGiver: ").append(toIndentedString(questGiver)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
    sb.append("    unlocks: ").append(toIndentedString(unlocks)).append("\n");
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

