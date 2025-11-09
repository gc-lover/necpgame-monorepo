package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.AchievementRewards;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProgressUpdateResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ProgressUpdateResult {

  private @Nullable UUID achievementId;

  private @Nullable Integer previousProgress;

  private @Nullable Integer newProgress;

  private @Nullable Boolean unlocked;

  private @Nullable AchievementRewards rewardsGranted;

  public ProgressUpdateResult achievementId(@Nullable UUID achievementId) {
    this.achievementId = achievementId;
    return this;
  }

  /**
   * Get achievementId
   * @return achievementId
   */
  @Valid 
  @Schema(name = "achievement_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("achievement_id")
  public @Nullable UUID getAchievementId() {
    return achievementId;
  }

  public void setAchievementId(@Nullable UUID achievementId) {
    this.achievementId = achievementId;
  }

  public ProgressUpdateResult previousProgress(@Nullable Integer previousProgress) {
    this.previousProgress = previousProgress;
    return this;
  }

  /**
   * Get previousProgress
   * @return previousProgress
   */
  
  @Schema(name = "previous_progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_progress")
  public @Nullable Integer getPreviousProgress() {
    return previousProgress;
  }

  public void setPreviousProgress(@Nullable Integer previousProgress) {
    this.previousProgress = previousProgress;
  }

  public ProgressUpdateResult newProgress(@Nullable Integer newProgress) {
    this.newProgress = newProgress;
    return this;
  }

  /**
   * Get newProgress
   * @return newProgress
   */
  
  @Schema(name = "new_progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_progress")
  public @Nullable Integer getNewProgress() {
    return newProgress;
  }

  public void setNewProgress(@Nullable Integer newProgress) {
    this.newProgress = newProgress;
  }

  public ProgressUpdateResult unlocked(@Nullable Boolean unlocked) {
    this.unlocked = unlocked;
    return this;
  }

  /**
   * Get unlocked
   * @return unlocked
   */
  
  @Schema(name = "unlocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked")
  public @Nullable Boolean getUnlocked() {
    return unlocked;
  }

  public void setUnlocked(@Nullable Boolean unlocked) {
    this.unlocked = unlocked;
  }

  public ProgressUpdateResult rewardsGranted(@Nullable AchievementRewards rewardsGranted) {
    this.rewardsGranted = rewardsGranted;
    return this;
  }

  /**
   * Get rewardsGranted
   * @return rewardsGranted
   */
  @Valid 
  @Schema(name = "rewards_granted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards_granted")
  public @Nullable AchievementRewards getRewardsGranted() {
    return rewardsGranted;
  }

  public void setRewardsGranted(@Nullable AchievementRewards rewardsGranted) {
    this.rewardsGranted = rewardsGranted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressUpdateResult progressUpdateResult = (ProgressUpdateResult) o;
    return Objects.equals(this.achievementId, progressUpdateResult.achievementId) &&
        Objects.equals(this.previousProgress, progressUpdateResult.previousProgress) &&
        Objects.equals(this.newProgress, progressUpdateResult.newProgress) &&
        Objects.equals(this.unlocked, progressUpdateResult.unlocked) &&
        Objects.equals(this.rewardsGranted, progressUpdateResult.rewardsGranted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(achievementId, previousProgress, newProgress, unlocked, rewardsGranted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressUpdateResult {\n");
    sb.append("    achievementId: ").append(toIndentedString(achievementId)).append("\n");
    sb.append("    previousProgress: ").append(toIndentedString(previousProgress)).append("\n");
    sb.append("    newProgress: ").append(toIndentedString(newProgress)).append("\n");
    sb.append("    unlocked: ").append(toIndentedString(unlocked)).append("\n");
    sb.append("    rewardsGranted: ").append(toIndentedString(rewardsGranted)).append("\n");
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

