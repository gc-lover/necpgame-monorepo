package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * QuestCompletionResult
 */


public class QuestCompletionResult {

  private @Nullable String questId;

  private @Nullable String endingAchieved;

  private @Nullable Object rewardsGranted;

  @Valid
  private List<Object> worldConsequences = new ArrayList<>();

  private @Nullable Object reputationChanges;

  @Valid
  private List<String> unlockedQuests = new ArrayList<>();

  public QuestCompletionResult questId(@Nullable String questId) {
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

  public QuestCompletionResult endingAchieved(@Nullable String endingAchieved) {
    this.endingAchieved = endingAchieved;
    return this;
  }

  /**
   * Get endingAchieved
   * @return endingAchieved
   */
  
  @Schema(name = "ending_achieved", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ending_achieved")
  public @Nullable String getEndingAchieved() {
    return endingAchieved;
  }

  public void setEndingAchieved(@Nullable String endingAchieved) {
    this.endingAchieved = endingAchieved;
  }

  public QuestCompletionResult rewardsGranted(@Nullable Object rewardsGranted) {
    this.rewardsGranted = rewardsGranted;
    return this;
  }

  /**
   * Get rewardsGranted
   * @return rewardsGranted
   */
  
  @Schema(name = "rewards_granted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards_granted")
  public @Nullable Object getRewardsGranted() {
    return rewardsGranted;
  }

  public void setRewardsGranted(@Nullable Object rewardsGranted) {
    this.rewardsGranted = rewardsGranted;
  }

  public QuestCompletionResult worldConsequences(List<Object> worldConsequences) {
    this.worldConsequences = worldConsequences;
    return this;
  }

  public QuestCompletionResult addWorldConsequencesItem(Object worldConsequencesItem) {
    if (this.worldConsequences == null) {
      this.worldConsequences = new ArrayList<>();
    }
    this.worldConsequences.add(worldConsequencesItem);
    return this;
  }

  /**
   * Влияние на мир
   * @return worldConsequences
   */
  
  @Schema(name = "world_consequences", description = "Влияние на мир", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("world_consequences")
  public List<Object> getWorldConsequences() {
    return worldConsequences;
  }

  public void setWorldConsequences(List<Object> worldConsequences) {
    this.worldConsequences = worldConsequences;
  }

  public QuestCompletionResult reputationChanges(@Nullable Object reputationChanges) {
    this.reputationChanges = reputationChanges;
    return this;
  }

  /**
   * Get reputationChanges
   * @return reputationChanges
   */
  
  @Schema(name = "reputation_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_changes")
  public @Nullable Object getReputationChanges() {
    return reputationChanges;
  }

  public void setReputationChanges(@Nullable Object reputationChanges) {
    this.reputationChanges = reputationChanges;
  }

  public QuestCompletionResult unlockedQuests(List<String> unlockedQuests) {
    this.unlockedQuests = unlockedQuests;
    return this;
  }

  public QuestCompletionResult addUnlockedQuestsItem(String unlockedQuestsItem) {
    if (this.unlockedQuests == null) {
      this.unlockedQuests = new ArrayList<>();
    }
    this.unlockedQuests.add(unlockedQuestsItem);
    return this;
  }

  /**
   * Get unlockedQuests
   * @return unlockedQuests
   */
  
  @Schema(name = "unlocked_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_quests")
  public List<String> getUnlockedQuests() {
    return unlockedQuests;
  }

  public void setUnlockedQuests(List<String> unlockedQuests) {
    this.unlockedQuests = unlockedQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestCompletionResult questCompletionResult = (QuestCompletionResult) o;
    return Objects.equals(this.questId, questCompletionResult.questId) &&
        Objects.equals(this.endingAchieved, questCompletionResult.endingAchieved) &&
        Objects.equals(this.rewardsGranted, questCompletionResult.rewardsGranted) &&
        Objects.equals(this.worldConsequences, questCompletionResult.worldConsequences) &&
        Objects.equals(this.reputationChanges, questCompletionResult.reputationChanges) &&
        Objects.equals(this.unlockedQuests, questCompletionResult.unlockedQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, endingAchieved, rewardsGranted, worldConsequences, reputationChanges, unlockedQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestCompletionResult {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    endingAchieved: ").append(toIndentedString(endingAchieved)).append("\n");
    sb.append("    rewardsGranted: ").append(toIndentedString(rewardsGranted)).append("\n");
    sb.append("    worldConsequences: ").append(toIndentedString(worldConsequences)).append("\n");
    sb.append("    reputationChanges: ").append(toIndentedString(reputationChanges)).append("\n");
    sb.append("    unlockedQuests: ").append(toIndentedString(unlockedQuests)).append("\n");
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

