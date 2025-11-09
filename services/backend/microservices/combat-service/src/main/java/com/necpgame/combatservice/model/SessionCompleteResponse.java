package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.combatservice.model.RewardBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SessionCompleteResponse
 */


public class SessionCompleteResponse {

  private @Nullable RewardBundle rewards;

  @Valid
  private List<String> achievements = new ArrayList<>();

  @Valid
  private List<Map<String, Object>> questProgress = new ArrayList<>();

  public SessionCompleteResponse rewards(@Nullable RewardBundle rewards) {
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
  public @Nullable RewardBundle getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable RewardBundle rewards) {
    this.rewards = rewards;
  }

  public SessionCompleteResponse achievements(List<String> achievements) {
    this.achievements = achievements;
    return this;
  }

  public SessionCompleteResponse addAchievementsItem(String achievementsItem) {
    if (this.achievements == null) {
      this.achievements = new ArrayList<>();
    }
    this.achievements.add(achievementsItem);
    return this;
  }

  /**
   * Get achievements
   * @return achievements
   */
  
  @Schema(name = "achievements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("achievements")
  public List<String> getAchievements() {
    return achievements;
  }

  public void setAchievements(List<String> achievements) {
    this.achievements = achievements;
  }

  public SessionCompleteResponse questProgress(List<Map<String, Object>> questProgress) {
    this.questProgress = questProgress;
    return this;
  }

  public SessionCompleteResponse addQuestProgressItem(Map<String, Object> questProgressItem) {
    if (this.questProgress == null) {
      this.questProgress = new ArrayList<>();
    }
    this.questProgress.add(questProgressItem);
    return this;
  }

  /**
   * Get questProgress
   * @return questProgress
   */
  @Valid 
  @Schema(name = "questProgress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questProgress")
  public List<Map<String, Object>> getQuestProgress() {
    return questProgress;
  }

  public void setQuestProgress(List<Map<String, Object>> questProgress) {
    this.questProgress = questProgress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionCompleteResponse sessionCompleteResponse = (SessionCompleteResponse) o;
    return Objects.equals(this.rewards, sessionCompleteResponse.rewards) &&
        Objects.equals(this.achievements, sessionCompleteResponse.achievements) &&
        Objects.equals(this.questProgress, sessionCompleteResponse.questProgress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewards, achievements, questProgress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionCompleteResponse {\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    achievements: ").append(toIndentedString(achievements)).append("\n");
    sb.append("    questProgress: ").append(toIndentedString(questProgress)).append("\n");
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

