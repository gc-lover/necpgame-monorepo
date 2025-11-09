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
 * HealthStatusResponseSummary
 */

@JsonTypeName("HealthStatusResponse_summary")

public class HealthStatusResponseSummary {

  private @Nullable Integer totalServices;

  private @Nullable Integer healthy;

  private @Nullable Integer degraded;

  private @Nullable Integer unhealthy;

  public HealthStatusResponseSummary totalServices(@Nullable Integer totalServices) {
    this.totalServices = totalServices;
    return this;
  }

  /**
   * Get totalServices
   * @return totalServices
   */
  
  @Schema(name = "total_services", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_services")
  public @Nullable Integer getTotalServices() {
    return totalServices;
  }

  public void setTotalServices(@Nullable Integer totalServices) {
    this.totalServices = totalServices;
  }

  public HealthStatusResponseSummary healthy(@Nullable Integer healthy) {
    this.healthy = healthy;
    return this;
  }

  /**
   * Get healthy
   * @return healthy
   */
  
  @Schema(name = "healthy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("healthy")
  public @Nullable Integer getHealthy() {
    return healthy;
  }

  public void setHealthy(@Nullable Integer healthy) {
    this.healthy = healthy;
  }

  public HealthStatusResponseSummary degraded(@Nullable Integer degraded) {
    this.degraded = degraded;
    return this;
  }

  /**
   * Get degraded
   * @return degraded
   */
  
  @Schema(name = "degraded", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("degraded")
  public @Nullable Integer getDegraded() {
    return degraded;
  }

  public void setDegraded(@Nullable Integer degraded) {
    this.degraded = degraded;
  }

  public HealthStatusResponseSummary unhealthy(@Nullable Integer unhealthy) {
    this.unhealthy = unhealthy;
    return this;
  }

  /**
   * Get unhealthy
   * @return unhealthy
   */
  
  @Schema(name = "unhealthy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unhealthy")
  public @Nullable Integer getUnhealthy() {
    return unhealthy;
  }

  public void setUnhealthy(@Nullable Integer unhealthy) {
    this.unhealthy = unhealthy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HealthStatusResponseSummary healthStatusResponseSummary = (HealthStatusResponseSummary) o;
    return Objects.equals(this.totalServices, healthStatusResponseSummary.totalServices) &&
        Objects.equals(this.healthy, healthStatusResponseSummary.healthy) &&
        Objects.equals(this.degraded, healthStatusResponseSummary.degraded) &&
        Objects.equals(this.unhealthy, healthStatusResponseSummary.unhealthy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalServices, healthy, degraded, unhealthy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HealthStatusResponseSummary {\n");
    sb.append("    totalServices: ").append(toIndentedString(totalServices)).append("\n");
    sb.append("    healthy: ").append(toIndentedString(healthy)).append("\n");
    sb.append("    degraded: ").append(toIndentedString(degraded)).append("\n");
    sb.append("    unhealthy: ").append(toIndentedString(unhealthy)).append("\n");
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

