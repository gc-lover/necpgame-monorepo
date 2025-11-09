package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.QuestRewards;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestCompletionResult {

  private @Nullable UUID questId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime completionTime;

  private @Nullable QuestRewards rewards;

  @Valid
  private List<String> unlockedQuests = new ArrayList<>();

  @Valid
  private Map<String, Integer> reputationChanges = new HashMap<>();

  public QuestCompletionResult questId(@Nullable UUID questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  @Valid 
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public @Nullable UUID getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable UUID questId) {
    this.questId = questId;
  }

  public QuestCompletionResult completionTime(@Nullable OffsetDateTime completionTime) {
    this.completionTime = completionTime;
    return this;
  }

  /**
   * Get completionTime
   * @return completionTime
   */
  @Valid 
  @Schema(name = "completion_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completion_time")
  public @Nullable OffsetDateTime getCompletionTime() {
    return completionTime;
  }

  public void setCompletionTime(@Nullable OffsetDateTime completionTime) {
    this.completionTime = completionTime;
  }

  public QuestCompletionResult rewards(@Nullable QuestRewards rewards) {
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
   * Новые квесты, которые стали доступны
   * @return unlockedQuests
   */
  
  @Schema(name = "unlocked_quests", description = "Новые квесты, которые стали доступны", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_quests")
  public List<String> getUnlockedQuests() {
    return unlockedQuests;
  }

  public void setUnlockedQuests(List<String> unlockedQuests) {
    this.unlockedQuests = unlockedQuests;
  }

  public QuestCompletionResult reputationChanges(Map<String, Integer> reputationChanges) {
    this.reputationChanges = reputationChanges;
    return this;
  }

  public QuestCompletionResult putReputationChangesItem(String key, Integer reputationChangesItem) {
    if (this.reputationChanges == null) {
      this.reputationChanges = new HashMap<>();
    }
    this.reputationChanges.put(key, reputationChangesItem);
    return this;
  }

  /**
   * Get reputationChanges
   * @return reputationChanges
   */
  
  @Schema(name = "reputation_changes", example = "{\"ARASAKA\":-10,\"NOMADS\":25}", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_changes")
  public Map<String, Integer> getReputationChanges() {
    return reputationChanges;
  }

  public void setReputationChanges(Map<String, Integer> reputationChanges) {
    this.reputationChanges = reputationChanges;
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
        Objects.equals(this.completionTime, questCompletionResult.completionTime) &&
        Objects.equals(this.rewards, questCompletionResult.rewards) &&
        Objects.equals(this.unlockedQuests, questCompletionResult.unlockedQuests) &&
        Objects.equals(this.reputationChanges, questCompletionResult.reputationChanges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, completionTime, rewards, unlockedQuests, reputationChanges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestCompletionResult {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    completionTime: ").append(toIndentedString(completionTime)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    unlockedQuests: ").append(toIndentedString(unlockedQuests)).append("\n");
    sb.append("    reputationChanges: ").append(toIndentedString(reputationChanges)).append("\n");
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

