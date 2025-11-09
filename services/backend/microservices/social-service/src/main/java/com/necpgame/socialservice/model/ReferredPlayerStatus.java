package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RewardGrant;
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
 * ReferredPlayerStatus
 */


public class ReferredPlayerStatus {

  private @Nullable String playerId;

  private @Nullable String referrerId;

  @Valid
  private List<String> milestonesCompleted = new ArrayList<>();

  @Valid
  private List<@Valid RewardGrant> pendingRewards = new ArrayList<>();

  public ReferredPlayerStatus playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public ReferredPlayerStatus referrerId(@Nullable String referrerId) {
    this.referrerId = referrerId;
    return this;
  }

  /**
   * Get referrerId
   * @return referrerId
   */
  
  @Schema(name = "referrerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referrerId")
  public @Nullable String getReferrerId() {
    return referrerId;
  }

  public void setReferrerId(@Nullable String referrerId) {
    this.referrerId = referrerId;
  }

  public ReferredPlayerStatus milestonesCompleted(List<String> milestonesCompleted) {
    this.milestonesCompleted = milestonesCompleted;
    return this;
  }

  public ReferredPlayerStatus addMilestonesCompletedItem(String milestonesCompletedItem) {
    if (this.milestonesCompleted == null) {
      this.milestonesCompleted = new ArrayList<>();
    }
    this.milestonesCompleted.add(milestonesCompletedItem);
    return this;
  }

  /**
   * Get milestonesCompleted
   * @return milestonesCompleted
   */
  
  @Schema(name = "milestonesCompleted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("milestonesCompleted")
  public List<String> getMilestonesCompleted() {
    return milestonesCompleted;
  }

  public void setMilestonesCompleted(List<String> milestonesCompleted) {
    this.milestonesCompleted = milestonesCompleted;
  }

  public ReferredPlayerStatus pendingRewards(List<@Valid RewardGrant> pendingRewards) {
    this.pendingRewards = pendingRewards;
    return this;
  }

  public ReferredPlayerStatus addPendingRewardsItem(RewardGrant pendingRewardsItem) {
    if (this.pendingRewards == null) {
      this.pendingRewards = new ArrayList<>();
    }
    this.pendingRewards.add(pendingRewardsItem);
    return this;
  }

  /**
   * Get pendingRewards
   * @return pendingRewards
   */
  @Valid 
  @Schema(name = "pendingRewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pendingRewards")
  public List<@Valid RewardGrant> getPendingRewards() {
    return pendingRewards;
  }

  public void setPendingRewards(List<@Valid RewardGrant> pendingRewards) {
    this.pendingRewards = pendingRewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReferredPlayerStatus referredPlayerStatus = (ReferredPlayerStatus) o;
    return Objects.equals(this.playerId, referredPlayerStatus.playerId) &&
        Objects.equals(this.referrerId, referredPlayerStatus.referrerId) &&
        Objects.equals(this.milestonesCompleted, referredPlayerStatus.milestonesCompleted) &&
        Objects.equals(this.pendingRewards, referredPlayerStatus.pendingRewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, referrerId, milestonesCompleted, pendingRewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReferredPlayerStatus {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    referrerId: ").append(toIndentedString(referrerId)).append("\n");
    sb.append("    milestonesCompleted: ").append(toIndentedString(milestonesCompleted)).append("\n");
    sb.append("    pendingRewards: ").append(toIndentedString(pendingRewards)).append("\n");
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

