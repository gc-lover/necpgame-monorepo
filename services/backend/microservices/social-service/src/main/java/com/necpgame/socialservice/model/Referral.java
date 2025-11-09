package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.MilestoneProgress;
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
 * Referral
 */


public class Referral {

  private String referralId;

  private String referrerId;

  private String referredPlayerId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    ACTIVE("ACTIVE"),
    
    COMPLETED("COMPLETED"),
    
    FLAGGED("FLAGGED");

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

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime registeredAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime completedAt;

  @Valid
  private List<@Valid MilestoneProgress> milestones = new ArrayList<>();

  @Valid
  private List<@Valid RewardGrant> rewards = new ArrayList<>();

  public Referral() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Referral(String referralId, String referrerId, String referredPlayerId, StatusEnum status) {
    this.referralId = referralId;
    this.referrerId = referrerId;
    this.referredPlayerId = referredPlayerId;
    this.status = status;
  }

  public Referral referralId(String referralId) {
    this.referralId = referralId;
    return this;
  }

  /**
   * Get referralId
   * @return referralId
   */
  @NotNull 
  @Schema(name = "referralId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("referralId")
  public String getReferralId() {
    return referralId;
  }

  public void setReferralId(String referralId) {
    this.referralId = referralId;
  }

  public Referral referrerId(String referrerId) {
    this.referrerId = referrerId;
    return this;
  }

  /**
   * Get referrerId
   * @return referrerId
   */
  @NotNull 
  @Schema(name = "referrerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("referrerId")
  public String getReferrerId() {
    return referrerId;
  }

  public void setReferrerId(String referrerId) {
    this.referrerId = referrerId;
  }

  public Referral referredPlayerId(String referredPlayerId) {
    this.referredPlayerId = referredPlayerId;
    return this;
  }

  /**
   * Get referredPlayerId
   * @return referredPlayerId
   */
  @NotNull 
  @Schema(name = "referredPlayerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("referredPlayerId")
  public String getReferredPlayerId() {
    return referredPlayerId;
  }

  public void setReferredPlayerId(String referredPlayerId) {
    this.referredPlayerId = referredPlayerId;
  }

  public Referral status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Referral registeredAt(@Nullable OffsetDateTime registeredAt) {
    this.registeredAt = registeredAt;
    return this;
  }

  /**
   * Get registeredAt
   * @return registeredAt
   */
  @Valid 
  @Schema(name = "registeredAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("registeredAt")
  public @Nullable OffsetDateTime getRegisteredAt() {
    return registeredAt;
  }

  public void setRegisteredAt(@Nullable OffsetDateTime registeredAt) {
    this.registeredAt = registeredAt;
  }

  public Referral completedAt(@Nullable OffsetDateTime completedAt) {
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

  public Referral milestones(List<@Valid MilestoneProgress> milestones) {
    this.milestones = milestones;
    return this;
  }

  public Referral addMilestonesItem(MilestoneProgress milestonesItem) {
    if (this.milestones == null) {
      this.milestones = new ArrayList<>();
    }
    this.milestones.add(milestonesItem);
    return this;
  }

  /**
   * Get milestones
   * @return milestones
   */
  @Valid 
  @Schema(name = "milestones", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("milestones")
  public List<@Valid MilestoneProgress> getMilestones() {
    return milestones;
  }

  public void setMilestones(List<@Valid MilestoneProgress> milestones) {
    this.milestones = milestones;
  }

  public Referral rewards(List<@Valid RewardGrant> rewards) {
    this.rewards = rewards;
    return this;
  }

  public Referral addRewardsItem(RewardGrant rewardsItem) {
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
    Referral referral = (Referral) o;
    return Objects.equals(this.referralId, referral.referralId) &&
        Objects.equals(this.referrerId, referral.referrerId) &&
        Objects.equals(this.referredPlayerId, referral.referredPlayerId) &&
        Objects.equals(this.status, referral.status) &&
        Objects.equals(this.registeredAt, referral.registeredAt) &&
        Objects.equals(this.completedAt, referral.completedAt) &&
        Objects.equals(this.milestones, referral.milestones) &&
        Objects.equals(this.rewards, referral.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(referralId, referrerId, referredPlayerId, status, registeredAt, completedAt, milestones, rewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Referral {\n");
    sb.append("    referralId: ").append(toIndentedString(referralId)).append("\n");
    sb.append("    referrerId: ").append(toIndentedString(referrerId)).append("\n");
    sb.append("    referredPlayerId: ").append(toIndentedString(referredPlayerId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    registeredAt: ").append(toIndentedString(registeredAt)).append("\n");
    sb.append("    completedAt: ").append(toIndentedString(completedAt)).append("\n");
    sb.append("    milestones: ").append(toIndentedString(milestones)).append("\n");
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

