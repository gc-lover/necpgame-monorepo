package com.necpgame.sessionservice.model;

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
 * ForceReconnectRequest
 */


public class ForceReconnectRequest {

  private String targetRegion;

  private @Nullable String reason;

  private Boolean notifyPlayer = true;

  public ForceReconnectRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ForceReconnectRequest(String targetRegion) {
    this.targetRegion = targetRegion;
  }

  public ForceReconnectRequest targetRegion(String targetRegion) {
    this.targetRegion = targetRegion;
    return this;
  }

  /**
   * Get targetRegion
   * @return targetRegion
   */
  @NotNull 
  @Schema(name = "targetRegion", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetRegion")
  public String getTargetRegion() {
    return targetRegion;
  }

  public void setTargetRegion(String targetRegion) {
    this.targetRegion = targetRegion;
  }

  public ForceReconnectRequest reason(@Nullable String reason) {
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

  public ForceReconnectRequest notifyPlayer(Boolean notifyPlayer) {
    this.notifyPlayer = notifyPlayer;
    return this;
  }

  /**
   * Get notifyPlayer
   * @return notifyPlayer
   */
  
  @Schema(name = "notifyPlayer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyPlayer")
  public Boolean getNotifyPlayer() {
    return notifyPlayer;
  }

  public void setNotifyPlayer(Boolean notifyPlayer) {
    this.notifyPlayer = notifyPlayer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ForceReconnectRequest forceReconnectRequest = (ForceReconnectRequest) o;
    return Objects.equals(this.targetRegion, forceReconnectRequest.targetRegion) &&
        Objects.equals(this.reason, forceReconnectRequest.reason) &&
        Objects.equals(this.notifyPlayer, forceReconnectRequest.notifyPlayer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetRegion, reason, notifyPlayer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ForceReconnectRequest {\n");
    sb.append("    targetRegion: ").append(toIndentedString(targetRegion)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    notifyPlayer: ").append(toIndentedString(notifyPlayer)).append("\n");
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

