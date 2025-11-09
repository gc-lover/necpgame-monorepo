package com.necpgame.realtimeservice.model;

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
 * PlayerRelocationRequest
 */


public class PlayerRelocationRequest {

  private String targetZoneId;

  private @Nullable String targetInstanceId;

  private @Nullable String reason;

  private @Nullable Boolean preserveSession;

  public PlayerRelocationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerRelocationRequest(String targetZoneId) {
    this.targetZoneId = targetZoneId;
  }

  public PlayerRelocationRequest targetZoneId(String targetZoneId) {
    this.targetZoneId = targetZoneId;
    return this;
  }

  /**
   * Get targetZoneId
   * @return targetZoneId
   */
  @NotNull 
  @Schema(name = "targetZoneId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetZoneId")
  public String getTargetZoneId() {
    return targetZoneId;
  }

  public void setTargetZoneId(String targetZoneId) {
    this.targetZoneId = targetZoneId;
  }

  public PlayerRelocationRequest targetInstanceId(@Nullable String targetInstanceId) {
    this.targetInstanceId = targetInstanceId;
    return this;
  }

  /**
   * Get targetInstanceId
   * @return targetInstanceId
   */
  
  @Schema(name = "targetInstanceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetInstanceId")
  public @Nullable String getTargetInstanceId() {
    return targetInstanceId;
  }

  public void setTargetInstanceId(@Nullable String targetInstanceId) {
    this.targetInstanceId = targetInstanceId;
  }

  public PlayerRelocationRequest reason(@Nullable String reason) {
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

  public PlayerRelocationRequest preserveSession(@Nullable Boolean preserveSession) {
    this.preserveSession = preserveSession;
    return this;
  }

  /**
   * Get preserveSession
   * @return preserveSession
   */
  
  @Schema(name = "preserveSession", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preserveSession")
  public @Nullable Boolean getPreserveSession() {
    return preserveSession;
  }

  public void setPreserveSession(@Nullable Boolean preserveSession) {
    this.preserveSession = preserveSession;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerRelocationRequest playerRelocationRequest = (PlayerRelocationRequest) o;
    return Objects.equals(this.targetZoneId, playerRelocationRequest.targetZoneId) &&
        Objects.equals(this.targetInstanceId, playerRelocationRequest.targetInstanceId) &&
        Objects.equals(this.reason, playerRelocationRequest.reason) &&
        Objects.equals(this.preserveSession, playerRelocationRequest.preserveSession);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetZoneId, targetInstanceId, reason, preserveSession);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerRelocationRequest {\n");
    sb.append("    targetZoneId: ").append(toIndentedString(targetZoneId)).append("\n");
    sb.append("    targetInstanceId: ").append(toIndentedString(targetInstanceId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    preserveSession: ").append(toIndentedString(preserveSession)).append("\n");
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

