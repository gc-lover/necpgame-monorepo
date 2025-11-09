package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.WarReward;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RewardDistribution
 */


public class RewardDistribution {

  private @Nullable String clanId;

  private @Nullable WarReward reward;

  private @Nullable Integer sharePercent;

  public RewardDistribution clanId(@Nullable String clanId) {
    this.clanId = clanId;
    return this;
  }

  /**
   * Get clanId
   * @return clanId
   */
  
  @Schema(name = "clanId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clanId")
  public @Nullable String getClanId() {
    return clanId;
  }

  public void setClanId(@Nullable String clanId) {
    this.clanId = clanId;
  }

  public RewardDistribution reward(@Nullable WarReward reward) {
    this.reward = reward;
    return this;
  }

  /**
   * Get reward
   * @return reward
   */
  @Valid 
  @Schema(name = "reward", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reward")
  public @Nullable WarReward getReward() {
    return reward;
  }

  public void setReward(@Nullable WarReward reward) {
    this.reward = reward;
  }

  public RewardDistribution sharePercent(@Nullable Integer sharePercent) {
    this.sharePercent = sharePercent;
    return this;
  }

  /**
   * Get sharePercent
   * @return sharePercent
   */
  
  @Schema(name = "sharePercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sharePercent")
  public @Nullable Integer getSharePercent() {
    return sharePercent;
  }

  public void setSharePercent(@Nullable Integer sharePercent) {
    this.sharePercent = sharePercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardDistribution rewardDistribution = (RewardDistribution) o;
    return Objects.equals(this.clanId, rewardDistribution.clanId) &&
        Objects.equals(this.reward, rewardDistribution.reward) &&
        Objects.equals(this.sharePercent, rewardDistribution.sharePercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clanId, reward, sharePercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardDistribution {\n");
    sb.append("    clanId: ").append(toIndentedString(clanId)).append("\n");
    sb.append("    reward: ").append(toIndentedString(reward)).append("\n");
    sb.append("    sharePercent: ").append(toIndentedString(sharePercent)).append("\n");
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

