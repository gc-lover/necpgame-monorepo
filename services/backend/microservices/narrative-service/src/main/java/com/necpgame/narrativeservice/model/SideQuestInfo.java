package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * SideQuestInfo
 */


public class SideQuestInfo {

  private @Nullable String questId;

  private @Nullable String questName;

  private @Nullable String period;

  private @Nullable String description;

  /**
   * Gets or Sets difficulty
   */
  public enum DifficultyEnum {
    EASY("easy"),
    
    MEDIUM("medium"),
    
    HARD("hard"),
    
    VERY_HARD("very_hard");

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
  private List<String> rewards = new ArrayList<>();

  private @Nullable String startingNode;

  public SideQuestInfo questId(@Nullable String questId) {
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

  public SideQuestInfo questName(@Nullable String questName) {
    this.questName = questName;
    return this;
  }

  /**
   * Get questName
   * @return questName
   */
  
  @Schema(name = "quest_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_name")
  public @Nullable String getQuestName() {
    return questName;
  }

  public void setQuestName(@Nullable String questName) {
    this.questName = questName;
  }

  public SideQuestInfo period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public SideQuestInfo description(@Nullable String description) {
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

  public SideQuestInfo difficulty(@Nullable DifficultyEnum difficulty) {
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

  public SideQuestInfo rewards(List<String> rewards) {
    this.rewards = rewards;
    return this;
  }

  public SideQuestInfo addRewardsItem(String rewardsItem) {
    if (this.rewards == null) {
      this.rewards = new ArrayList<>();
    }
    this.rewards.add(rewardsItem);
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public List<String> getRewards() {
    return rewards;
  }

  public void setRewards(List<String> rewards) {
    this.rewards = rewards;
  }

  public SideQuestInfo startingNode(@Nullable String startingNode) {
    this.startingNode = startingNode;
    return this;
  }

  /**
   * Get startingNode
   * @return startingNode
   */
  
  @Schema(name = "starting_node", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starting_node")
  public @Nullable String getStartingNode() {
    return startingNode;
  }

  public void setStartingNode(@Nullable String startingNode) {
    this.startingNode = startingNode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SideQuestInfo sideQuestInfo = (SideQuestInfo) o;
    return Objects.equals(this.questId, sideQuestInfo.questId) &&
        Objects.equals(this.questName, sideQuestInfo.questName) &&
        Objects.equals(this.period, sideQuestInfo.period) &&
        Objects.equals(this.description, sideQuestInfo.description) &&
        Objects.equals(this.difficulty, sideQuestInfo.difficulty) &&
        Objects.equals(this.rewards, sideQuestInfo.rewards) &&
        Objects.equals(this.startingNode, sideQuestInfo.startingNode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, questName, period, description, difficulty, rewards, startingNode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SideQuestInfo {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    questName: ").append(toIndentedString(questName)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    startingNode: ").append(toIndentedString(startingNode)).append("\n");
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

