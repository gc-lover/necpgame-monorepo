package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TriggerDeploymentRequest
 */

@JsonTypeName("triggerDeployment_request")

public class TriggerDeploymentRequest {

  private String version;

  /**
   * Gets or Sets environment
   */
  public enum EnvironmentEnum {
    PRODUCTION("production"),
    
    STAGING("staging");

    private final String value;

    EnvironmentEnum(String value) {
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
    public static EnvironmentEnum fromValue(String value) {
      for (EnvironmentEnum b : EnvironmentEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private EnvironmentEnum environment;

  /**
   * Gets or Sets deploymentStrategy
   */
  public enum DeploymentStrategyEnum {
    ROLLING("rolling"),
    
    BLUE_GREEN("blue_green"),
    
    CANARY("canary");

    private final String value;

    DeploymentStrategyEnum(String value) {
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
    public static DeploymentStrategyEnum fromValue(String value) {
      for (DeploymentStrategyEnum b : DeploymentStrategyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DeploymentStrategyEnum deploymentStrategy = DeploymentStrategyEnum.ROLLING;

  public TriggerDeploymentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TriggerDeploymentRequest(String version, EnvironmentEnum environment) {
    this.version = version;
    this.environment = environment;
  }

  public TriggerDeploymentRequest version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  @NotNull 
  @Schema(name = "version", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public TriggerDeploymentRequest environment(EnvironmentEnum environment) {
    this.environment = environment;
    return this;
  }

  /**
   * Get environment
   * @return environment
   */
  @NotNull 
  @Schema(name = "environment", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("environment")
  public EnvironmentEnum getEnvironment() {
    return environment;
  }

  public void setEnvironment(EnvironmentEnum environment) {
    this.environment = environment;
  }

  public TriggerDeploymentRequest deploymentStrategy(DeploymentStrategyEnum deploymentStrategy) {
    this.deploymentStrategy = deploymentStrategy;
    return this;
  }

  /**
   * Get deploymentStrategy
   * @return deploymentStrategy
   */
  
  @Schema(name = "deployment_strategy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deployment_strategy")
  public DeploymentStrategyEnum getDeploymentStrategy() {
    return deploymentStrategy;
  }

  public void setDeploymentStrategy(DeploymentStrategyEnum deploymentStrategy) {
    this.deploymentStrategy = deploymentStrategy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerDeploymentRequest triggerDeploymentRequest = (TriggerDeploymentRequest) o;
    return Objects.equals(this.version, triggerDeploymentRequest.version) &&
        Objects.equals(this.environment, triggerDeploymentRequest.environment) &&
        Objects.equals(this.deploymentStrategy, triggerDeploymentRequest.deploymentStrategy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, environment, deploymentStrategy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerDeploymentRequest {\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    environment: ").append(toIndentedString(environment)).append("\n");
    sb.append("    deploymentStrategy: ").append(toIndentedString(deploymentStrategy)).append("\n");
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

