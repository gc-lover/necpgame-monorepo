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
 * ServiceHealthChecksDependenciesInner
 */

@JsonTypeName("ServiceHealth_checks_dependencies_inner")

public class ServiceHealthChecksDependenciesInner {

  private @Nullable String service;

  private @Nullable String status;

  public ServiceHealthChecksDependenciesInner service(@Nullable String service) {
    this.service = service;
    return this;
  }

  /**
   * Get service
   * @return service
   */
  
  @Schema(name = "service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("service")
  public @Nullable String getService() {
    return service;
  }

  public void setService(@Nullable String service) {
    this.service = service;
  }

  public ServiceHealthChecksDependenciesInner status(@Nullable String status) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceHealthChecksDependenciesInner serviceHealthChecksDependenciesInner = (ServiceHealthChecksDependenciesInner) o;
    return Objects.equals(this.service, serviceHealthChecksDependenciesInner.service) &&
        Objects.equals(this.status, serviceHealthChecksDependenciesInner.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(service, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceHealthChecksDependenciesInner {\n");
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

