package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.RewardGrant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * MilestoneProgress
 */


public class MilestoneProgress {

  private String milestoneId;

  private String name;

  private Integer target;

  private @Nullable Integer current;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    LOCKED("LOCKED"),
    
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime completedAt;

  @Valid
  private List<@Valid RewardGrant> rewards = new ArrayList<>();

  public MilestoneProgress() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MilestoneProgress(String milestoneId, String name, Integer target) {
    this.milestoneId = milestoneId;
    this.name = name;
    this.target = target;
  }

  public MilestoneProgress milestoneId(String milestoneId) {
    this.milestoneId = milestoneId;
    return this;
  }

  /**
   * Get milestoneId
   * @return milestoneId
   */
  @NotNull 
  @Schema(name = "milestoneId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("milestoneId")
  public String getMilestoneId() {
    return milestoneId;
  }

  public void setMilestoneId(String milestoneId) {
    this.milestoneId = milestoneId;
  }

  public MilestoneProgress name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public MilestoneProgress target(Integer target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  @NotNull 
  @Schema(name = "target", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target")
  public Integer getTarget() {
    return target;
  }

  public void setTarget(Integer target) {
    this.target = target;
  }

  public MilestoneProgress current(@Nullable Integer current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  
  @Schema(name = "current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current")
  public @Nullable Integer getCurrent() {
    return current;
  }

  public void setCurrent(@Nullable Integer current) {
    this.current = current;
  }

  public MilestoneProgress status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public MilestoneProgress completedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
    return this;
  }

  /**
   * Get completedAt
   * @return completedAt
   */
  @Valid 
  @Schema(name = "completedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completedAt")
  public @Nullable OffsetDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
  }

  public MilestoneProgress rewards(List<@Valid RewardGrant> rewards) {
    this.rewards = rewards;
    return this;
  }

  public MilestoneProgress addRewardsItem(RewardGrant rewardsItem) {
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
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public List<@Valid RewardGrant> getRewards() {
    return rewards;
  }

  public void setRewards(List<@Valid RewardGrant> rewards) {
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
    MilestoneProgress milestoneProgress = (MilestoneProgress) o;
    return Objects.equals(this.milestoneId, milestoneProgress.milestoneId) &&
        Objects.equals(this.name, milestoneProgress.name) &&
        Objects.equals(this.target, milestoneProgress.target) &&
        Objects.equals(this.current, milestoneProgress.current) &&
        Objects.equals(this.status, milestoneProgress.status) &&
        Objects.equals(this.completedAt, milestoneProgress.completedAt) &&
        Objects.equals(this.rewards, milestoneProgress.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(milestoneId, name, target, current, status, completedAt, rewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MilestoneProgress {\n");
    sb.append("    milestoneId: ").append(toIndentedString(milestoneId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    completedAt: ").append(toIndentedString(completedAt)).append("\n");
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

