package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.ComponentAuditMetrics;
import com.necpgame.adminservice.model.Issue;
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
 * ComponentAudit
 */


public class ComponentAudit {

  private String componentId;

  private String name;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    EXCELLENT("excellent"),
    
    GOOD("good"),
    
    FAIR("fair"),
    
    POOR("poor"),
    
    CRITICAL("critical");

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

  private StatusEnum status;

  /**
   * Gets or Sets implementationStatus
   */
  public enum ImplementationStatusEnum {
    COMPLETED("completed"),
    
    IN_PROGRESS("in_progress"),
    
    NOT_STARTED("not_started");

    private final String value;

    ImplementationStatusEnum(String value) {
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
    public static ImplementationStatusEnum fromValue(String value) {
      for (ImplementationStatusEnum b : ImplementationStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ImplementationStatusEnum implementationStatus;

  @Valid
  private List<@Valid Issue> issues = new ArrayList<>();

  private @Nullable ComponentAuditMetrics metrics;

  @Valid
  private List<String> dependencies = new ArrayList<>();

  private @Nullable Integer apiEndpoints;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdated;

  public ComponentAudit() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ComponentAudit(String componentId, String name, StatusEnum status) {
    this.componentId = componentId;
    this.name = name;
    this.status = status;
  }

  public ComponentAudit componentId(String componentId) {
    this.componentId = componentId;
    return this;
  }

  /**
   * Get componentId
   * @return componentId
   */
  @NotNull 
  @Schema(name = "component_id", example = "auth", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("component_id")
  public String getComponentId() {
    return componentId;
  }

  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  public ComponentAudit name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Authentication & Authorization", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ComponentAudit status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ComponentAudit implementationStatus(@Nullable ImplementationStatusEnum implementationStatus) {
    this.implementationStatus = implementationStatus;
    return this;
  }

  /**
   * Get implementationStatus
   * @return implementationStatus
   */
  
  @Schema(name = "implementation_status", example = "completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implementation_status")
  public @Nullable ImplementationStatusEnum getImplementationStatus() {
    return implementationStatus;
  }

  public void setImplementationStatus(@Nullable ImplementationStatusEnum implementationStatus) {
    this.implementationStatus = implementationStatus;
  }

  public ComponentAudit issues(List<@Valid Issue> issues) {
    this.issues = issues;
    return this;
  }

  public ComponentAudit addIssuesItem(Issue issuesItem) {
    if (this.issues == null) {
      this.issues = new ArrayList<>();
    }
    this.issues.add(issuesItem);
    return this;
  }

  /**
   * Get issues
   * @return issues
   */
  @Valid 
  @Schema(name = "issues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issues")
  public List<@Valid Issue> getIssues() {
    return issues;
  }

  public void setIssues(List<@Valid Issue> issues) {
    this.issues = issues;
  }

  public ComponentAudit metrics(@Nullable ComponentAuditMetrics metrics) {
    this.metrics = metrics;
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  @Valid 
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public @Nullable ComponentAuditMetrics getMetrics() {
    return metrics;
  }

  public void setMetrics(@Nullable ComponentAuditMetrics metrics) {
    this.metrics = metrics;
  }

  public ComponentAudit dependencies(List<String> dependencies) {
    this.dependencies = dependencies;
    return this;
  }

  public ComponentAudit addDependenciesItem(String dependenciesItem) {
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<>();
    }
    this.dependencies.add(dependenciesItem);
    return this;
  }

  /**
   * Зависимости от других компонентов
   * @return dependencies
   */
  
  @Schema(name = "dependencies", description = "Зависимости от других компонентов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dependencies")
  public List<String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }

  public ComponentAudit apiEndpoints(@Nullable Integer apiEndpoints) {
    this.apiEndpoints = apiEndpoints;
    return this;
  }

  /**
   * Количество API endpoints
   * @return apiEndpoints
   */
  
  @Schema(name = "api_endpoints", example = "12", description = "Количество API endpoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("api_endpoints")
  public @Nullable Integer getApiEndpoints() {
    return apiEndpoints;
  }

  public void setApiEndpoints(@Nullable Integer apiEndpoints) {
    this.apiEndpoints = apiEndpoints;
  }

  public ComponentAudit lastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  /**
   * Get lastUpdated
   * @return lastUpdated
   */
  @Valid 
  @Schema(name = "last_updated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_updated")
  public @Nullable OffsetDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComponentAudit componentAudit = (ComponentAudit) o;
    return Objects.equals(this.componentId, componentAudit.componentId) &&
        Objects.equals(this.name, componentAudit.name) &&
        Objects.equals(this.status, componentAudit.status) &&
        Objects.equals(this.implementationStatus, componentAudit.implementationStatus) &&
        Objects.equals(this.issues, componentAudit.issues) &&
        Objects.equals(this.metrics, componentAudit.metrics) &&
        Objects.equals(this.dependencies, componentAudit.dependencies) &&
        Objects.equals(this.apiEndpoints, componentAudit.apiEndpoints) &&
        Objects.equals(this.lastUpdated, componentAudit.lastUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentId, name, status, implementationStatus, issues, metrics, dependencies, apiEndpoints, lastUpdated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComponentAudit {\n");
    sb.append("    componentId: ").append(toIndentedString(componentId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    implementationStatus: ").append(toIndentedString(implementationStatus)).append("\n");
    sb.append("    issues: ").append(toIndentedString(issues)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    dependencies: ").append(toIndentedString(dependencies)).append("\n");
    sb.append("    apiEndpoints: ").append(toIndentedString(apiEndpoints)).append("\n");
    sb.append("    lastUpdated: ").append(toIndentedString(lastUpdated)).append("\n");
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

