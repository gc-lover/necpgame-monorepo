package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReferralSettingsReferrerRewardsInner
 */

@JsonTypeName("ReferralSettings_referrerRewards_inner")

public class ReferralSettingsReferrerRewardsInner {

  private @Nullable Integer milestone;

  private @Nullable String reward;

  public ReferralSettingsReferrerRewardsInner milestone(@Nullable Integer milestone) {
    this.milestone = milestone;
    return this;
  }

  /**
   * Get milestone
   * @return milestone
   */
  
  @Schema(name = "milestone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("milestone")
  public @Nullable Integer getMilestone() {
    return milestone;
  }

  public void setMilestone(@Nullable Integer milestone) {
    this.milestone = milestone;
  }

  public ReferralSettingsReferrerRewardsInner reward(@Nullable String reward) {
    this.reward = reward;
    return this;
  }

  /**
   * Get reward
   * @return reward
   */
  
  @Schema(name = "reward", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reward")
  public @Nullable String getReward() {
    return reward;
  }

  public void setReward(@Nullable String reward) {
    this.reward = reward;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReferralSettingsReferrerRewardsInner referralSettingsReferrerRewardsInner = (ReferralSettingsReferrerRewardsInner) o;
    return Objects.equals(this.milestone, referralSettingsReferrerRewardsInner.milestone) &&
        Objects.equals(this.reward, referralSettingsReferrerRewardsInner.reward);
  }

  @Override
  public int hashCode() {
    return Objects.hash(milestone, reward);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReferralSettingsReferrerRewardsInner {\n");
    sb.append("    milestone: ").append(toIndentedString(milestone)).append("\n");
    sb.append("    reward: ").append(toIndentedString(reward)).append("\n");
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

