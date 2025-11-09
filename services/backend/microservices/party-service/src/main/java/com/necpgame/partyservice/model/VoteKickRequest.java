package com.necpgame.partyservice.model;

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
 * VoteKickRequest
 */


public class VoteKickRequest {

  private String targetMemberId;

  private @Nullable String reason;

  private Integer timeoutSeconds = 60;

  public VoteKickRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoteKickRequest(String targetMemberId) {
    this.targetMemberId = targetMemberId;
  }

  public VoteKickRequest targetMemberId(String targetMemberId) {
    this.targetMemberId = targetMemberId;
    return this;
  }

  /**
   * Get targetMemberId
   * @return targetMemberId
   */
  @NotNull 
  @Schema(name = "targetMemberId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetMemberId")
  public String getTargetMemberId() {
    return targetMemberId;
  }

  public void setTargetMemberId(String targetMemberId) {
    this.targetMemberId = targetMemberId;
  }

  public VoteKickRequest reason(@Nullable String reason) {
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

  public VoteKickRequest timeoutSeconds(Integer timeoutSeconds) {
    this.timeoutSeconds = timeoutSeconds;
    return this;
  }

  /**
   * Get timeoutSeconds
   * @return timeoutSeconds
   */
  
  @Schema(name = "timeoutSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeoutSeconds")
  public Integer getTimeoutSeconds() {
    return timeoutSeconds;
  }

  public void setTimeoutSeconds(Integer timeoutSeconds) {
    this.timeoutSeconds = timeoutSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoteKickRequest voteKickRequest = (VoteKickRequest) o;
    return Objects.equals(this.targetMemberId, voteKickRequest.targetMemberId) &&
        Objects.equals(this.reason, voteKickRequest.reason) &&
        Objects.equals(this.timeoutSeconds, voteKickRequest.timeoutSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetMemberId, reason, timeoutSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoteKickRequest {\n");
    sb.append("    targetMemberId: ").append(toIndentedString(targetMemberId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    timeoutSeconds: ").append(toIndentedString(timeoutSeconds)).append("\n");
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

