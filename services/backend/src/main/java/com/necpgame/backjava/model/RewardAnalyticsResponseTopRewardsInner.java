package com.necpgame.backjava.model;

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
 * RewardAnalyticsResponseTopRewardsInner
 */

@JsonTypeName("RewardAnalyticsResponse_topRewards_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RewardAnalyticsResponseTopRewardsInner {

  private @Nullable String rewardId;

  private @Nullable Integer claimCount;

  public RewardAnalyticsResponseTopRewardsInner rewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
    return this;
  }

  /**
   * Get rewardId
   * @return rewardId
   */
  
  @Schema(name = "rewardId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardId")
  public @Nullable String getRewardId() {
    return rewardId;
  }

  public void setRewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
  }

  public RewardAnalyticsResponseTopRewardsInner claimCount(@Nullable Integer claimCount) {
    this.claimCount = claimCount;
    return this;
  }

  /**
   * Get claimCount
   * @return claimCount
   */
  
  @Schema(name = "claimCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("claimCount")
  public @Nullable Integer getClaimCount() {
    return claimCount;
  }

  public void setClaimCount(@Nullable Integer claimCount) {
    this.claimCount = claimCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardAnalyticsResponseTopRewardsInner rewardAnalyticsResponseTopRewardsInner = (RewardAnalyticsResponseTopRewardsInner) o;
    return Objects.equals(this.rewardId, rewardAnalyticsResponseTopRewardsInner.rewardId) &&
        Objects.equals(this.claimCount, rewardAnalyticsResponseTopRewardsInner.claimCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardId, claimCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardAnalyticsResponseTopRewardsInner {\n");
    sb.append("    rewardId: ").append(toIndentedString(rewardId)).append("\n");
    sb.append("    claimCount: ").append(toIndentedString(claimCount)).append("\n");
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

