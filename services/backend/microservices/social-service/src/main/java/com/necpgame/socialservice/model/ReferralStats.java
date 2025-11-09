package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReferralStats
 */


public class ReferralStats {

  private @Nullable Integer totalReferrals;

  private @Nullable Integer activeReferrals;

  private @Nullable Integer completedReferrals;

  private @Nullable Integer pendingRewards;

  public ReferralStats totalReferrals(@Nullable Integer totalReferrals) {
    this.totalReferrals = totalReferrals;
    return this;
  }

  /**
   * Get totalReferrals
   * @return totalReferrals
   */
  
  @Schema(name = "totalReferrals", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalReferrals")
  public @Nullable Integer getTotalReferrals() {
    return totalReferrals;
  }

  public void setTotalReferrals(@Nullable Integer totalReferrals) {
    this.totalReferrals = totalReferrals;
  }

  public ReferralStats activeReferrals(@Nullable Integer activeReferrals) {
    this.activeReferrals = activeReferrals;
    return this;
  }

  /**
   * Get activeReferrals
   * @return activeReferrals
   */
  
  @Schema(name = "activeReferrals", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeReferrals")
  public @Nullable Integer getActiveReferrals() {
    return activeReferrals;
  }

  public void setActiveReferrals(@Nullable Integer activeReferrals) {
    this.activeReferrals = activeReferrals;
  }

  public ReferralStats completedReferrals(@Nullable Integer completedReferrals) {
    this.completedReferrals = completedReferrals;
    return this;
  }

  /**
   * Get completedReferrals
   * @return completedReferrals
   */
  
  @Schema(name = "completedReferrals", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completedReferrals")
  public @Nullable Integer getCompletedReferrals() {
    return completedReferrals;
  }

  public void setCompletedReferrals(@Nullable Integer completedReferrals) {
    this.completedReferrals = completedReferrals;
  }

  public ReferralStats pendingRewards(@Nullable Integer pendingRewards) {
    this.pendingRewards = pendingRewards;
    return this;
  }

  /**
   * Get pendingRewards
   * @return pendingRewards
   */
  
  @Schema(name = "pendingRewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pendingRewards")
  public @Nullable Integer getPendingRewards() {
    return pendingRewards;
  }

  public void setPendingRewards(@Nullable Integer pendingRewards) {
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
    ReferralStats referralStats = (ReferralStats) o;
    return Objects.equals(this.totalReferrals, referralStats.totalReferrals) &&
        Objects.equals(this.activeReferrals, referralStats.activeReferrals) &&
        Objects.equals(this.completedReferrals, referralStats.completedReferrals) &&
        Objects.equals(this.pendingRewards, referralStats.pendingRewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalReferrals, activeReferrals, completedReferrals, pendingRewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReferralStats {\n");
    sb.append("    totalReferrals: ").append(toIndentedString(totalReferrals)).append("\n");
    sb.append("    activeReferrals: ").append(toIndentedString(activeReferrals)).append("\n");
    sb.append("    completedReferrals: ").append(toIndentedString(completedReferrals)).append("\n");
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

