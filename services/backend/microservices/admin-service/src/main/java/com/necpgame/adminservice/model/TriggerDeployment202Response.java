package com.necpgame.adminservice.model;

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
 * TriggerDeployment202Response
 */

@JsonTypeName("triggerDeployment_202_response")

public class TriggerDeployment202Response {

  private @Nullable String deploymentId;

  private @Nullable String status;

  private @Nullable Integer estimatedTimeMinutes;

  public TriggerDeployment202Response deploymentId(@Nullable String deploymentId) {
    this.deploymentId = deploymentId;
    return this;
  }

  /**
   * Get deploymentId
   * @return deploymentId
   */
  
  @Schema(name = "deployment_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deployment_id")
  public @Nullable String getDeploymentId() {
    return deploymentId;
  }

  public void setDeploymentId(@Nullable String deploymentId) {
    this.deploymentId = deploymentId;
  }

  public TriggerDeployment202Response status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public TriggerDeployment202Response estimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
    return this;
  }

  /**
   * Get estimatedTimeMinutes
   * @return estimatedTimeMinutes
   */
  
  @Schema(name = "estimated_time_minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time_minutes")
  public @Nullable Integer getEstimatedTimeMinutes() {
    return estimatedTimeMinutes;
  }

  public void setEstimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerDeployment202Response triggerDeployment202Response = (TriggerDeployment202Response) o;
    return Objects.equals(this.deploymentId, triggerDeployment202Response.deploymentId) &&
        Objects.equals(this.status, triggerDeployment202Response.status) &&
        Objects.equals(this.estimatedTimeMinutes, triggerDeployment202Response.estimatedTimeMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deploymentId, status, estimatedTimeMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerDeployment202Response {\n");
    sb.append("    deploymentId: ").append(toIndentedString(deploymentId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    estimatedTimeMinutes: ").append(toIndentedString(estimatedTimeMinutes)).append("\n");
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

