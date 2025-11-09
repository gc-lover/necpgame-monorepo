package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * PriorityAdjustmentRequest
 */


public class PriorityAdjustmentRequest {

  private Integer priorityDelta;

  private String reason;

  private @Nullable Integer expiresInSeconds;

  private @Nullable UUID actorId;

  public PriorityAdjustmentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PriorityAdjustmentRequest(Integer priorityDelta, String reason) {
    this.priorityDelta = priorityDelta;
    this.reason = reason;
  }

  public PriorityAdjustmentRequest priorityDelta(Integer priorityDelta) {
    this.priorityDelta = priorityDelta;
    return this;
  }

  /**
   * Get priorityDelta
   * minimum: -5
   * maximum: 10
   * @return priorityDelta
   */
  @NotNull @Min(value = -5) @Max(value = 10) 
  @Schema(name = "priorityDelta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("priorityDelta")
  public Integer getPriorityDelta() {
    return priorityDelta;
  }

  public void setPriorityDelta(Integer priorityDelta) {
    this.priorityDelta = priorityDelta;
  }

  public PriorityAdjustmentRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public PriorityAdjustmentRequest expiresInSeconds(@Nullable Integer expiresInSeconds) {
    this.expiresInSeconds = expiresInSeconds;
    return this;
  }

  /**
   * Get expiresInSeconds
   * minimum: 30
   * @return expiresInSeconds
   */
  @Min(value = 30) 
  @Schema(name = "expiresInSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresInSeconds")
  public @Nullable Integer getExpiresInSeconds() {
    return expiresInSeconds;
  }

  public void setExpiresInSeconds(@Nullable Integer expiresInSeconds) {
    this.expiresInSeconds = expiresInSeconds;
  }

  public PriorityAdjustmentRequest actorId(@Nullable UUID actorId) {
    this.actorId = actorId;
    return this;
  }

  /**
   * Get actorId
   * @return actorId
   */
  @Valid 
  @Schema(name = "actorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actorId")
  public @Nullable UUID getActorId() {
    return actorId;
  }

  public void setActorId(@Nullable UUID actorId) {
    this.actorId = actorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriorityAdjustmentRequest priorityAdjustmentRequest = (PriorityAdjustmentRequest) o;
    return Objects.equals(this.priorityDelta, priorityAdjustmentRequest.priorityDelta) &&
        Objects.equals(this.reason, priorityAdjustmentRequest.reason) &&
        Objects.equals(this.expiresInSeconds, priorityAdjustmentRequest.expiresInSeconds) &&
        Objects.equals(this.actorId, priorityAdjustmentRequest.actorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(priorityDelta, reason, expiresInSeconds, actorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriorityAdjustmentRequest {\n");
    sb.append("    priorityDelta: ").append(toIndentedString(priorityDelta)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    expiresInSeconds: ").append(toIndentedString(expiresInSeconds)).append("\n");
    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
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

