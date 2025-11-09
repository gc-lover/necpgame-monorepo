package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetNPCDecisionRequestPlayerActionsInner
 */

@JsonTypeName("getNPCDecision_request_player_actions_inner")

public class GetNPCDecisionRequestPlayerActionsInner {

  private @Nullable String action;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable BigDecimal intensity;

  public GetNPCDecisionRequestPlayerActionsInner action(@Nullable String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable String getAction() {
    return action;
  }

  public void setAction(@Nullable String action) {
    this.action = action;
  }

  public GetNPCDecisionRequestPlayerActionsInner timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public GetNPCDecisionRequestPlayerActionsInner intensity(@Nullable BigDecimal intensity) {
    this.intensity = intensity;
    return this;
  }

  /**
   * Get intensity
   * @return intensity
   */
  @Valid 
  @Schema(name = "intensity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("intensity")
  public @Nullable BigDecimal getIntensity() {
    return intensity;
  }

  public void setIntensity(@Nullable BigDecimal intensity) {
    this.intensity = intensity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetNPCDecisionRequestPlayerActionsInner getNPCDecisionRequestPlayerActionsInner = (GetNPCDecisionRequestPlayerActionsInner) o;
    return Objects.equals(this.action, getNPCDecisionRequestPlayerActionsInner.action) &&
        Objects.equals(this.timestamp, getNPCDecisionRequestPlayerActionsInner.timestamp) &&
        Objects.equals(this.intensity, getNPCDecisionRequestPlayerActionsInner.intensity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, timestamp, intensity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetNPCDecisionRequestPlayerActionsInner {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    intensity: ").append(toIndentedString(intensity)).append("\n");
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

