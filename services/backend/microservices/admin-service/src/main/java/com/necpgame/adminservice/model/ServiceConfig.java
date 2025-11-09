package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ServiceConfig
 */


public class ServiceConfig {

  private @Nullable String serviceName;

  private @Nullable String environment;

  @Valid
  private Map<String, Object> _configuration = new HashMap<>();

  private @Nullable String version;

  public ServiceConfig serviceName(@Nullable String serviceName) {
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

  public ServiceConfig environment(@Nullable String environment) {
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

  public ServiceConfig _configuration(Map<String, Object> _configuration) {
    this._configuration = _configuration;
    return this;
  }

  public ServiceConfig putConfigurationItem(String key, Object _configurationItem) {
    if (this._configuration == null) {
      this._configuration = new HashMap<>();
    }
    this._configuration.put(key, _configurationItem);
    return this;
  }

  /**
   * Get _configuration
   * @return _configuration
   */
  
  @Schema(name = "configuration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("configuration")
  public Map<String, Object> getConfiguration() {
    return _configuration;
  }

  public void setConfiguration(Map<String, Object> _configuration) {
    this._configuration = _configuration;
  }

  public ServiceConfig version(@Nullable String version) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceConfig serviceConfig = (ServiceConfig) o;
    return Objects.equals(this.serviceName, serviceConfig.serviceName) &&
        Objects.equals(this.environment, serviceConfig.environment) &&
        Objects.equals(this._configuration, serviceConfig._configuration) &&
        Objects.equals(this.version, serviceConfig.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceName, environment, _configuration, version);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceConfig {\n");
    sb.append("    serviceName: ").append(toIndentedString(serviceName)).append("\n");
    sb.append("    environment: ").append(toIndentedString(environment)).append("\n");
    sb.append("    _configuration: ").append(toIndentedString(_configuration)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

