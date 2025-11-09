package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.WarReward;
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
 * WarResolutionResponse
 */


public class WarResolutionResponse {

  private @Nullable String warId;

  private @Nullable String outcome;

  @Valid
  private List<@Valid WarReward> rewards = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime cooldownEndsAt;

  public WarResolutionResponse warId(@Nullable String warId) {
    this.warId = warId;
    return this;
  }

  /**
   * Get warId
   * @return warId
   */
  
  @Schema(name = "warId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warId")
  public @Nullable String getWarId() {
    return warId;
  }

  public void setWarId(@Nullable String warId) {
    this.warId = warId;
  }

  public WarResolutionResponse outcome(@Nullable String outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable String getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable String outcome) {
    this.outcome = outcome;
  }

  public WarResolutionResponse rewards(List<@Valid WarReward> rewards) {
    this.rewards = rewards;
    return this;
  }

  public WarResolutionResponse addRewardsItem(WarReward rewardsItem) {
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
  public List<@Valid WarReward> getRewards() {
    return rewards;
  }

  public void setRewards(List<@Valid WarReward> rewards) {
    this.rewards = rewards;
  }

  public WarResolutionResponse cooldownEndsAt(@Nullable OffsetDateTime cooldownEndsAt) {
    this.cooldownEndsAt = cooldownEndsAt;
    return this;
  }

  /**
   * Get cooldownEndsAt
   * @return cooldownEndsAt
   */
  @Valid 
  @Schema(name = "cooldownEndsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownEndsAt")
  public @Nullable OffsetDateTime getCooldownEndsAt() {
    return cooldownEndsAt;
  }

  public void setCooldownEndsAt(@Nullable OffsetDateTime cooldownEndsAt) {
    this.cooldownEndsAt = cooldownEndsAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarResolutionResponse warResolutionResponse = (WarResolutionResponse) o;
    return Objects.equals(this.warId, warResolutionResponse.warId) &&
        Objects.equals(this.outcome, warResolutionResponse.outcome) &&
        Objects.equals(this.rewards, warResolutionResponse.rewards) &&
        Objects.equals(this.cooldownEndsAt, warResolutionResponse.cooldownEndsAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(warId, outcome, rewards, cooldownEndsAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarResolutionResponse {\n");
    sb.append("    warId: ").append(toIndentedString(warId)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    cooldownEndsAt: ").append(toIndentedString(cooldownEndsAt)).append("\n");
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

