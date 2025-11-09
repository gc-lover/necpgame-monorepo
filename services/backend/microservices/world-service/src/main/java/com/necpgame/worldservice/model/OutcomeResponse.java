package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.ReputationChange;
import com.necpgame.worldservice.model.RewardPayload;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * OutcomeResponse
 */


public class OutcomeResponse {

  private @Nullable UUID chainId;

  private @Nullable String outcome;

  private @Nullable RewardPayload rewards;

  @Valid
  private List<@Valid ReputationChange> reputation = new ArrayList<>();

  @Valid
  private List<String> unlocks = new ArrayList<>();

  @Valid
  private List<UUID> scheduledFollowUps = new ArrayList<>();

  public OutcomeResponse chainId(@Nullable UUID chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  @Valid 
  @Schema(name = "chainId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chainId")
  public @Nullable UUID getChainId() {
    return chainId;
  }

  public void setChainId(@Nullable UUID chainId) {
    this.chainId = chainId;
  }

  public OutcomeResponse outcome(@Nullable String outcome) {
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

  public OutcomeResponse rewards(@Nullable RewardPayload rewards) {
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
  public @Nullable RewardPayload getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable RewardPayload rewards) {
    this.rewards = rewards;
  }

  public OutcomeResponse reputation(List<@Valid ReputationChange> reputation) {
    this.reputation = reputation;
    return this;
  }

  public OutcomeResponse addReputationItem(ReputationChange reputationItem) {
    if (this.reputation == null) {
      this.reputation = new ArrayList<>();
    }
    this.reputation.add(reputationItem);
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  @Valid 
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public List<@Valid ReputationChange> getReputation() {
    return reputation;
  }

  public void setReputation(List<@Valid ReputationChange> reputation) {
    this.reputation = reputation;
  }

  public OutcomeResponse unlocks(List<String> unlocks) {
    this.unlocks = unlocks;
    return this;
  }

  public OutcomeResponse addUnlocksItem(String unlocksItem) {
    if (this.unlocks == null) {
      this.unlocks = new ArrayList<>();
    }
    this.unlocks.add(unlocksItem);
    return this;
  }

  /**
   * Get unlocks
   * @return unlocks
   */
  
  @Schema(name = "unlocks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocks")
  public List<String> getUnlocks() {
    return unlocks;
  }

  public void setUnlocks(List<String> unlocks) {
    this.unlocks = unlocks;
  }

  public OutcomeResponse scheduledFollowUps(List<UUID> scheduledFollowUps) {
    this.scheduledFollowUps = scheduledFollowUps;
    return this;
  }

  public OutcomeResponse addScheduledFollowUpsItem(UUID scheduledFollowUpsItem) {
    if (this.scheduledFollowUps == null) {
      this.scheduledFollowUps = new ArrayList<>();
    }
    this.scheduledFollowUps.add(scheduledFollowUpsItem);
    return this;
  }

  /**
   * Get scheduledFollowUps
   * @return scheduledFollowUps
   */
  @Valid 
  @Schema(name = "scheduledFollowUps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledFollowUps")
  public List<UUID> getScheduledFollowUps() {
    return scheduledFollowUps;
  }

  public void setScheduledFollowUps(List<UUID> scheduledFollowUps) {
    this.scheduledFollowUps = scheduledFollowUps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OutcomeResponse outcomeResponse = (OutcomeResponse) o;
    return Objects.equals(this.chainId, outcomeResponse.chainId) &&
        Objects.equals(this.outcome, outcomeResponse.outcome) &&
        Objects.equals(this.rewards, outcomeResponse.rewards) &&
        Objects.equals(this.reputation, outcomeResponse.reputation) &&
        Objects.equals(this.unlocks, outcomeResponse.unlocks) &&
        Objects.equals(this.scheduledFollowUps, outcomeResponse.scheduledFollowUps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, outcome, rewards, reputation, unlocks, scheduledFollowUps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OutcomeResponse {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
    sb.append("    unlocks: ").append(toIndentedString(unlocks)).append("\n");
    sb.append("    scheduledFollowUps: ").append(toIndentedString(scheduledFollowUps)).append("\n");
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

