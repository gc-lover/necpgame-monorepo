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
 * RewardGrantRequest
 */


public class RewardGrantRequest {

  private String milestoneId;

  private @Nullable Integer overrideAmount;

  private @Nullable String reason;

  public RewardGrantRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RewardGrantRequest(String milestoneId) {
    this.milestoneId = milestoneId;
  }

  public RewardGrantRequest milestoneId(String milestoneId) {
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

  public RewardGrantRequest overrideAmount(@Nullable Integer overrideAmount) {
    this.overrideAmount = overrideAmount;
    return this;
  }

  /**
   * Get overrideAmount
   * @return overrideAmount
   */
  
  @Schema(name = "overrideAmount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overrideAmount")
  public @Nullable Integer getOverrideAmount() {
    return overrideAmount;
  }

  public void setOverrideAmount(@Nullable Integer overrideAmount) {
    this.overrideAmount = overrideAmount;
  }

  public RewardGrantRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardGrantRequest rewardGrantRequest = (RewardGrantRequest) o;
    return Objects.equals(this.milestoneId, rewardGrantRequest.milestoneId) &&
        Objects.equals(this.overrideAmount, rewardGrantRequest.overrideAmount) &&
        Objects.equals(this.reason, rewardGrantRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(milestoneId, overrideAmount, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardGrantRequest {\n");
    sb.append("    milestoneId: ").append(toIndentedString(milestoneId)).append("\n");
    sb.append("    overrideAmount: ").append(toIndentedString(overrideAmount)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

