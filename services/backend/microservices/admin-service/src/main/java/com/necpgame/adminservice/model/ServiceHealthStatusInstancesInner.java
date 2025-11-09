package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ServiceHealthStatusInstancesInner
 */

@JsonTypeName("ServiceHealthStatus_instances_inner")

public class ServiceHealthStatusInstancesInner {

  private @Nullable String instanceId;

  private @Nullable String status;

  private @Nullable Integer responseTimeMs;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastCheck;

  public ServiceHealthStatusInstancesInner instanceId(@Nullable String instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  
  @Schema(name = "instance_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instance_id")
  public @Nullable String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(@Nullable String instanceId) {
    this.instanceId = instanceId;
  }

  public ServiceHealthStatusInstancesInner status(@Nullable String status) {
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

  public ServiceHealthStatusInstancesInner responseTimeMs(@Nullable Integer responseTimeMs) {
    this.responseTimeMs = responseTimeMs;
    return this;
  }

  /**
   * Get responseTimeMs
   * @return responseTimeMs
   */
  
  @Schema(name = "response_time_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("response_time_ms")
  public @Nullable Integer getResponseTimeMs() {
    return responseTimeMs;
  }

  public void setResponseTimeMs(@Nullable Integer responseTimeMs) {
    this.responseTimeMs = responseTimeMs;
  }

  public ServiceHealthStatusInstancesInner lastCheck(@Nullable OffsetDateTime lastCheck) {
    this.lastCheck = lastCheck;
    return this;
  }

  /**
   * Get lastCheck
   * @return lastCheck
   */
  @Valid 
  @Schema(name = "last_check", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_check")
  public @Nullable OffsetDateTime getLastCheck() {
    return lastCheck;
  }

  public void setLastCheck(@Nullable OffsetDateTime lastCheck) {
    this.lastCheck = lastCheck;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceHealthStatusInstancesInner serviceHealthStatusInstancesInner = (ServiceHealthStatusInstancesInner) o;
    return Objects.equals(this.instanceId, serviceHealthStatusInstancesInner.instanceId) &&
        Objects.equals(this.status, serviceHealthStatusInstancesInner.status) &&
        Objects.equals(this.responseTimeMs, serviceHealthStatusInstancesInner.responseTimeMs) &&
        Objects.equals(this.lastCheck, serviceHealthStatusInstancesInner.lastCheck);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, status, responseTimeMs, lastCheck);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceHealthStatusInstancesInner {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    responseTimeMs: ").append(toIndentedString(responseTimeMs)).append("\n");
    sb.append("    lastCheck: ").append(toIndentedString(lastCheck)).append("\n");
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

