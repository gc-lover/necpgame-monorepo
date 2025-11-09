package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ProgressionMilestoneRequirement;
import com.necpgame.gameplayservice.model.ProgressionMilestoneRewards;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProgressionMilestone
 */


public class ProgressionMilestone {

  private @Nullable String milestoneId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable ProgressionMilestoneRequirement requirement;

  private @Nullable Boolean completed;

  private @Nullable ProgressionMilestoneRewards rewards;

  public ProgressionMilestone milestoneId(@Nullable String milestoneId) {
    this.milestoneId = milestoneId;
    return this;
  }

  /**
   * Get milestoneId
   * @return milestoneId
   */
  
  @Schema(name = "milestone_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("milestone_id")
  public @Nullable String getMilestoneId() {
    return milestoneId;
  }

  public void setMilestoneId(@Nullable String milestoneId) {
    this.milestoneId = milestoneId;
  }

  public ProgressionMilestone name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Reach Level 10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public ProgressionMilestone description(@Nullable String description) {
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

  public ProgressionMilestone requirement(@Nullable ProgressionMilestoneRequirement requirement) {
    this.requirement = requirement;
    return this;
  }

  /**
   * Get requirement
   * @return requirement
   */
  @Valid 
  @Schema(name = "requirement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirement")
  public @Nullable ProgressionMilestoneRequirement getRequirement() {
    return requirement;
  }

  public void setRequirement(@Nullable ProgressionMilestoneRequirement requirement) {
    this.requirement = requirement;
  }

  public ProgressionMilestone completed(@Nullable Boolean completed) {
    this.completed = completed;
    return this;
  }

  /**
   * Get completed
   * @return completed
   */
  
  @Schema(name = "completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed")
  public @Nullable Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(@Nullable Boolean completed) {
    this.completed = completed;
  }

  public ProgressionMilestone rewards(@Nullable ProgressionMilestoneRewards rewards) {
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
  public @Nullable ProgressionMilestoneRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable ProgressionMilestoneRewards rewards) {
    this.rewards = rewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressionMilestone progressionMilestone = (ProgressionMilestone) o;
    return Objects.equals(this.milestoneId, progressionMilestone.milestoneId) &&
        Objects.equals(this.name, progressionMilestone.name) &&
        Objects.equals(this.description, progressionMilestone.description) &&
        Objects.equals(this.requirement, progressionMilestone.requirement) &&
        Objects.equals(this.completed, progressionMilestone.completed) &&
        Objects.equals(this.rewards, progressionMilestone.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(milestoneId, name, description, requirement, completed, rewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressionMilestone {\n");
    sb.append("    milestoneId: ").append(toIndentedString(milestoneId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requirement: ").append(toIndentedString(requirement)).append("\n");
    sb.append("    completed: ").append(toIndentedString(completed)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
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

