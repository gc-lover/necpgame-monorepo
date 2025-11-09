package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.ServiceHealthStatusInstancesInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ServiceHealthStatus
 */


public class ServiceHealthStatus {

  private @Nullable String serviceName;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    UP("up"),
    
    DEGRADED("degraded"),
    
    DOWN("down");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @Valid
  private List<@Valid ServiceHealthStatusInstancesInner> instances = new ArrayList<>();

  public ServiceHealthStatus serviceName(@Nullable String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  /**
   * Get serviceName
   * @return serviceName
   */
  
  @Schema(name = "service_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("service_name")
  public @Nullable String getServiceName() {
    return serviceName;
  }

  public void setServiceName(@Nullable String serviceName) {
    this.serviceName = serviceName;
  }

  public ServiceHealthStatus status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ServiceHealthStatus instances(List<@Valid ServiceHealthStatusInstancesInner> instances) {
    this.instances = instances;
    return this;
  }

  public ServiceHealthStatus addInstancesItem(ServiceHealthStatusInstancesInner instancesItem) {
    if (this.instances == null) {
      this.instances = new ArrayList<>();
    }
    this.instances.add(instancesItem);
    return this;
  }

  /**
   * Get instances
   * @return instances
   */
  @Valid 
  @Schema(name = "instances", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instances")
  public List<@Valid ServiceHealthStatusInstancesInner> getInstances() {
    return instances;
  }

  public void setInstances(List<@Valid ServiceHealthStatusInstancesInner> instances) {
    this.instances = instances;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceHealthStatus serviceHealthStatus = (ServiceHealthStatus) o;
    return Objects.equals(this.serviceName, serviceHealthStatus.serviceName) &&
        Objects.equals(this.status, serviceHealthStatus.status) &&
        Objects.equals(this.instances, serviceHealthStatus.instances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceName, status, instances);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceHealthStatus {\n");
    sb.append("    serviceName: ").append(toIndentedString(serviceName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    instances: ").append(toIndentedString(instances)).append("\n");
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

