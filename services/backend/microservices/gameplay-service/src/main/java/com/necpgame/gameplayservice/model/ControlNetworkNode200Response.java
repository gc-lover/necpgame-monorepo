package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ControlNetworkNode200Response
 */

@JsonTypeName("controlNetworkNode_200_response")

public class ControlNetworkNode200Response {

  private @Nullable Boolean success;

  private @Nullable String nodeId;

  private @Nullable String action;

  private @Nullable BigDecimal duration;

  public ControlNetworkNode200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public ControlNetworkNode200Response nodeId(@Nullable String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  /**
   * Get nodeId
   * @return nodeId
   */
  
  @Schema(name = "node_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("node_id")
  public @Nullable String getNodeId() {
    return nodeId;
  }

  public void setNodeId(@Nullable String nodeId) {
    this.nodeId = nodeId;
  }

  public ControlNetworkNode200Response action(@Nullable String action) {
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

  public ControlNetworkNode200Response duration(@Nullable BigDecimal duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Get duration
   * @return duration
   */
  @Valid 
  @Schema(name = "duration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable BigDecimal getDuration() {
    return duration;
  }

  public void setDuration(@Nullable BigDecimal duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ControlNetworkNode200Response controlNetworkNode200Response = (ControlNetworkNode200Response) o;
    return Objects.equals(this.success, controlNetworkNode200Response.success) &&
        Objects.equals(this.nodeId, controlNetworkNode200Response.nodeId) &&
        Objects.equals(this.action, controlNetworkNode200Response.action) &&
        Objects.equals(this.duration, controlNetworkNode200Response.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, nodeId, action, duration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ControlNetworkNode200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

