package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ServiceInfo
 */


public class ServiceInfo {

  private @Nullable String serviceId;

  private @Nullable String serviceName;

  private @Nullable String description;

  private @Nullable Integer port;

  private @Nullable Integer endpointsCount;

  @Valid
  private List<String> dependencies = new ArrayList<>();

  private @Nullable String database;

  private @Nullable String cache;

  private @Nullable String messageQueue;

  public ServiceInfo serviceId(@Nullable String serviceId) {
    this.serviceId = serviceId;
    return this;
  }

  /**
   * Get serviceId
   * @return serviceId
   */
  
  @Schema(name = "service_id", example = "auth-service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("service_id")
  public @Nullable String getServiceId() {
    return serviceId;
  }

  public void setServiceId(@Nullable String serviceId) {
    this.serviceId = serviceId;
  }

  public ServiceInfo serviceName(@Nullable String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  /**
   * Get serviceName
   * @return serviceName
   */
  
  @Schema(name = "service_name", example = "Authentication Service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("service_name")
  public @Nullable String getServiceName() {
    return serviceName;
  }

  public void setServiceName(@Nullable String serviceName) {
    this.serviceName = serviceName;
  }

  public ServiceInfo description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public ServiceInfo port(@Nullable Integer port) {
    this.port = port;
    return this;
  }

  /**
   * Get port
   * @return port
   */
  
  @Schema(name = "port", example = "8080", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("port")
  public @Nullable Integer getPort() {
    return port;
  }

  public void setPort(@Nullable Integer port) {
    this.port = port;
  }

  public ServiceInfo endpointsCount(@Nullable Integer endpointsCount) {
    this.endpointsCount = endpointsCount;
    return this;
  }

  /**
   * Get endpointsCount
   * @return endpointsCount
   */
  
  @Schema(name = "endpoints_count", example = "12", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endpoints_count")
  public @Nullable Integer getEndpointsCount() {
    return endpointsCount;
  }

  public void setEndpointsCount(@Nullable Integer endpointsCount) {
    this.endpointsCount = endpointsCount;
  }

  public ServiceInfo dependencies(List<String> dependencies) {
    this.dependencies = dependencies;
    return this;
  }

  public ServiceInfo addDependenciesItem(String dependenciesItem) {
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<>();
    }
    this.dependencies.add(dependenciesItem);
    return this;
  }

  /**
   * Зависимости
   * @return dependencies
   */
  
  @Schema(name = "dependencies", description = "Зависимости", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dependencies")
  public List<String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }

  public ServiceInfo database(@Nullable String database) {
    this.database = database;
    return this;
  }

  /**
   * Get database
   * @return database
   */
  
  @Schema(name = "database", example = "postgresql", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("database")
  public @Nullable String getDatabase() {
    return database;
  }

  public void setDatabase(@Nullable String database) {
    this.database = database;
  }

  public ServiceInfo cache(@Nullable String cache) {
    this.cache = cache;
    return this;
  }

  /**
   * Get cache
   * @return cache
   */
  
  @Schema(name = "cache", example = "redis", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cache")
  public @Nullable String getCache() {
    return cache;
  }

  public void setCache(@Nullable String cache) {
    this.cache = cache;
  }

  public ServiceInfo messageQueue(@Nullable String messageQueue) {
    this.messageQueue = messageQueue;
    return this;
  }

  /**
   * Get messageQueue
   * @return messageQueue
   */
  
  @Schema(name = "message_queue", example = "rabbitmq", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message_queue")
  public @Nullable String getMessageQueue() {
    return messageQueue;
  }

  public void setMessageQueue(@Nullable String messageQueue) {
    this.messageQueue = messageQueue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceInfo serviceInfo = (ServiceInfo) o;
    return Objects.equals(this.serviceId, serviceInfo.serviceId) &&
        Objects.equals(this.serviceName, serviceInfo.serviceName) &&
        Objects.equals(this.description, serviceInfo.description) &&
        Objects.equals(this.port, serviceInfo.port) &&
        Objects.equals(this.endpointsCount, serviceInfo.endpointsCount) &&
        Objects.equals(this.dependencies, serviceInfo.dependencies) &&
        Objects.equals(this.database, serviceInfo.database) &&
        Objects.equals(this.cache, serviceInfo.cache) &&
        Objects.equals(this.messageQueue, serviceInfo.messageQueue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceId, serviceName, description, port, endpointsCount, dependencies, database, cache, messageQueue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceInfo {\n");
    sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
    sb.append("    serviceName: ").append(toIndentedString(serviceName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    port: ").append(toIndentedString(port)).append("\n");
    sb.append("    endpointsCount: ").append(toIndentedString(endpointsCount)).append("\n");
    sb.append("    dependencies: ").append(toIndentedString(dependencies)).append("\n");
    sb.append("    database: ").append(toIndentedString(database)).append("\n");
    sb.append("    cache: ").append(toIndentedString(cache)).append("\n");
    sb.append("    messageQueue: ").append(toIndentedString(messageQueue)).append("\n");
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

