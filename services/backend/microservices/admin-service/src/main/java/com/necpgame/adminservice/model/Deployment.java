package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * Deployment
 */


public class Deployment {

  private @Nullable String deploymentId;

  private @Nullable String version;

  private @Nullable String environment;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    SUCCESS("success"),
    
    FAILED("failed"),
    
    IN_PROGRESS("in_progress"),
    
    ROLLED_BACK("rolled_back");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime completedAt;

  private @Nullable String deployedBy;

  private @Nullable String deploymentStrategy;

  @Valid
  private List<String> servicesDeployed = new ArrayList<>();

  public Deployment deploymentId(@Nullable String deploymentId) {
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

  public Deployment version(@Nullable String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  
  @Schema(name = "version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable String getVersion() {
    return version;
  }

  public void setVersion(@Nullable String version) {
    this.version = version;
  }

  public Deployment environment(@Nullable String environment) {
    this.environment = environment;
    return this;
  }

  /**
   * Get environment
   * @return environment
   */
  
  @Schema(name = "environment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("environment")
  public @Nullable String getEnvironment() {
    return environment;
  }

  public void setEnvironment(@Nullable String environment) {
    this.environment = environment;
  }

  public Deployment status(@Nullable StatusEnum status) {
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

  public Deployment startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started_at")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public Deployment completedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
    return this;
  }

  /**
   * Get completedAt
   * @return completedAt
   */
  @Valid 
  @Schema(name = "completed_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed_at")
  public @Nullable OffsetDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
  }

  public Deployment deployedBy(@Nullable String deployedBy) {
    this.deployedBy = deployedBy;
    return this;
  }

  /**
   * Get deployedBy
   * @return deployedBy
   */
  
  @Schema(name = "deployed_by", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deployed_by")
  public @Nullable String getDeployedBy() {
    return deployedBy;
  }

  public void setDeployedBy(@Nullable String deployedBy) {
    this.deployedBy = deployedBy;
  }

  public Deployment deploymentStrategy(@Nullable String deploymentStrategy) {
    this.deploymentStrategy = deploymentStrategy;
    return this;
  }

  /**
   * Get deploymentStrategy
   * @return deploymentStrategy
   */
  
  @Schema(name = "deployment_strategy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deployment_strategy")
  public @Nullable String getDeploymentStrategy() {
    return deploymentStrategy;
  }

  public void setDeploymentStrategy(@Nullable String deploymentStrategy) {
    this.deploymentStrategy = deploymentStrategy;
  }

  public Deployment servicesDeployed(List<String> servicesDeployed) {
    this.servicesDeployed = servicesDeployed;
    return this;
  }

  public Deployment addServicesDeployedItem(String servicesDeployedItem) {
    if (this.servicesDeployed == null) {
      this.servicesDeployed = new ArrayList<>();
    }
    this.servicesDeployed.add(servicesDeployedItem);
    return this;
  }

  /**
   * Get servicesDeployed
   * @return servicesDeployed
   */
  
  @Schema(name = "services_deployed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("services_deployed")
  public List<String> getServicesDeployed() {
    return servicesDeployed;
  }

  public void setServicesDeployed(List<String> servicesDeployed) {
    this.servicesDeployed = servicesDeployed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Deployment deployment = (Deployment) o;
    return Objects.equals(this.deploymentId, deployment.deploymentId) &&
        Objects.equals(this.version, deployment.version) &&
        Objects.equals(this.environment, deployment.environment) &&
        Objects.equals(this.status, deployment.status) &&
        Objects.equals(this.startedAt, deployment.startedAt) &&
        Objects.equals(this.completedAt, deployment.completedAt) &&
        Objects.equals(this.deployedBy, deployment.deployedBy) &&
        Objects.equals(this.deploymentStrategy, deployment.deploymentStrategy) &&
        Objects.equals(this.servicesDeployed, deployment.servicesDeployed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deploymentId, version, environment, status, startedAt, completedAt, deployedBy, deploymentStrategy, servicesDeployed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Deployment {\n");
    sb.append("    deploymentId: ").append(toIndentedString(deploymentId)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    environment: ").append(toIndentedString(environment)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    completedAt: ").append(toIndentedString(completedAt)).append("\n");
    sb.append("    deployedBy: ").append(toIndentedString(deployedBy)).append("\n");
    sb.append("    deploymentStrategy: ").append(toIndentedString(deploymentStrategy)).append("\n");
    sb.append("    servicesDeployed: ").append(toIndentedString(servicesDeployed)).append("\n");
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

