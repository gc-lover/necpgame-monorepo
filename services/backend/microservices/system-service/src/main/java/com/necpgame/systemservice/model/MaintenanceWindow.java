package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.systemservice.model.IntegrationHooks;
import com.necpgame.systemservice.model.NotificationPlan;
import com.necpgame.systemservice.model.ShutdownPlan;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * MaintenanceWindow
 */


public class MaintenanceWindow {

  private UUID windowId;

  private String title;

  private @Nullable String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    SCHEDULED("SCHEDULED"),
    
    EMERGENCY("EMERGENCY");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  /**
   * Gets or Sets environment
   */
  public enum EnvironmentEnum {
    PRODUCTION("PRODUCTION"),
    
    STAGING("STAGING"),
    
    TEST("TEST");

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

  @Valid
  private List<String> zones = new ArrayList<>();

  @Valid
  private List<String> services = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endAt;

  private @Nullable Integer expectedDurationMinutes;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PLANNED("PLANNED"),
    
    APPROVED("APPROVED"),
    
    IN_PROGRESS("IN_PROGRESS"),
    
    PAUSED("PAUSED"),
    
    COMPLETED("COMPLETED"),
    
    CANCELLED("CANCELLED"),
    
    ROLLED_BACK("ROLLED_BACK");

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

  private String createdBy;

  private @Nullable String approvedBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  private @Nullable ShutdownPlan shutdownPlan;

  private @Nullable NotificationPlan notificationPlan;

  private @Nullable IntegrationHooks hooks;

  public MaintenanceWindow() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceWindow(UUID windowId, String title, TypeEnum type, EnvironmentEnum environment, OffsetDateTime startAt, StatusEnum status, String createdBy) {
    this.windowId = windowId;
    this.title = title;
    this.type = type;
    this.environment = environment;
    this.startAt = startAt;
    this.status = status;
    this.createdBy = createdBy;
  }

  public MaintenanceWindow windowId(UUID windowId) {
    this.windowId = windowId;
    return this;
  }

  /**
   * Get windowId
   * @return windowId
   */
  @NotNull @Valid 
  @Schema(name = "windowId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("windowId")
  public UUID getWindowId() {
    return windowId;
  }

  public void setWindowId(UUID windowId) {
    this.windowId = windowId;
  }

  public MaintenanceWindow title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public MaintenanceWindow description(@Nullable String description) {
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

  public MaintenanceWindow type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public MaintenanceWindow environment(EnvironmentEnum environment) {
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

  public MaintenanceWindow zones(List<String> zones) {
    this.zones = zones;
    return this;
  }

  public MaintenanceWindow addZonesItem(String zonesItem) {
    if (this.zones == null) {
      this.zones = new ArrayList<>();
    }
    this.zones.add(zonesItem);
    return this;
  }

  /**
   * Get zones
   * @return zones
   */
  
  @Schema(name = "zones", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zones")
  public List<String> getZones() {
    return zones;
  }

  public void setZones(List<String> zones) {
    this.zones = zones;
  }

  public MaintenanceWindow services(List<String> services) {
    this.services = services;
    return this;
  }

  public MaintenanceWindow addServicesItem(String servicesItem) {
    if (this.services == null) {
      this.services = new ArrayList<>();
    }
    this.services.add(servicesItem);
    return this;
  }

  /**
   * Get services
   * @return services
   */
  
  @Schema(name = "services", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("services")
  public List<String> getServices() {
    return services;
  }

  public void setServices(List<String> services) {
    this.services = services;
  }

  public MaintenanceWindow startAt(OffsetDateTime startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * Get startAt
   * @return startAt
   */
  @NotNull @Valid 
  @Schema(name = "startAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startAt")
  public OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public MaintenanceWindow endAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * Get endAt
   * @return endAt
   */
  @Valid 
  @Schema(name = "endAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endAt")
  public @Nullable OffsetDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
  }

  public MaintenanceWindow expectedDurationMinutes(@Nullable Integer expectedDurationMinutes) {
    this.expectedDurationMinutes = expectedDurationMinutes;
    return this;
  }

  /**
   * Get expectedDurationMinutes
   * @return expectedDurationMinutes
   */
  
  @Schema(name = "expectedDurationMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expectedDurationMinutes")
  public @Nullable Integer getExpectedDurationMinutes() {
    return expectedDurationMinutes;
  }

  public void setExpectedDurationMinutes(@Nullable Integer expectedDurationMinutes) {
    this.expectedDurationMinutes = expectedDurationMinutes;
  }

  public MaintenanceWindow status(StatusEnum status) {
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

  public MaintenanceWindow createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  /**
   * Get createdBy
   * @return createdBy
   */
  @NotNull 
  @Schema(name = "createdBy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdBy")
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public MaintenanceWindow approvedBy(@Nullable String approvedBy) {
    this.approvedBy = approvedBy;
    return this;
  }

  /**
   * Get approvedBy
   * @return approvedBy
   */
  
  @Schema(name = "approvedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approvedBy")
  public @Nullable String getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(@Nullable String approvedBy) {
    this.approvedBy = approvedBy;
  }

  public MaintenanceWindow createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public MaintenanceWindow updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public MaintenanceWindow shutdownPlan(@Nullable ShutdownPlan shutdownPlan) {
    this.shutdownPlan = shutdownPlan;
    return this;
  }

  /**
   * Get shutdownPlan
   * @return shutdownPlan
   */
  @Valid 
  @Schema(name = "shutdownPlan", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shutdownPlan")
  public @Nullable ShutdownPlan getShutdownPlan() {
    return shutdownPlan;
  }

  public void setShutdownPlan(@Nullable ShutdownPlan shutdownPlan) {
    this.shutdownPlan = shutdownPlan;
  }

  public MaintenanceWindow notificationPlan(@Nullable NotificationPlan notificationPlan) {
    this.notificationPlan = notificationPlan;
    return this;
  }

  /**
   * Get notificationPlan
   * @return notificationPlan
   */
  @Valid 
  @Schema(name = "notificationPlan", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notificationPlan")
  public @Nullable NotificationPlan getNotificationPlan() {
    return notificationPlan;
  }

  public void setNotificationPlan(@Nullable NotificationPlan notificationPlan) {
    this.notificationPlan = notificationPlan;
  }

  public MaintenanceWindow hooks(@Nullable IntegrationHooks hooks) {
    this.hooks = hooks;
    return this;
  }

  /**
   * Get hooks
   * @return hooks
   */
  @Valid 
  @Schema(name = "hooks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hooks")
  public @Nullable IntegrationHooks getHooks() {
    return hooks;
  }

  public void setHooks(@Nullable IntegrationHooks hooks) {
    this.hooks = hooks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceWindow maintenanceWindow = (MaintenanceWindow) o;
    return Objects.equals(this.windowId, maintenanceWindow.windowId) &&
        Objects.equals(this.title, maintenanceWindow.title) &&
        Objects.equals(this.description, maintenanceWindow.description) &&
        Objects.equals(this.type, maintenanceWindow.type) &&
        Objects.equals(this.environment, maintenanceWindow.environment) &&
        Objects.equals(this.zones, maintenanceWindow.zones) &&
        Objects.equals(this.services, maintenanceWindow.services) &&
        Objects.equals(this.startAt, maintenanceWindow.startAt) &&
        Objects.equals(this.endAt, maintenanceWindow.endAt) &&
        Objects.equals(this.expectedDurationMinutes, maintenanceWindow.expectedDurationMinutes) &&
        Objects.equals(this.status, maintenanceWindow.status) &&
        Objects.equals(this.createdBy, maintenanceWindow.createdBy) &&
        Objects.equals(this.approvedBy, maintenanceWindow.approvedBy) &&
        Objects.equals(this.createdAt, maintenanceWindow.createdAt) &&
        Objects.equals(this.updatedAt, maintenanceWindow.updatedAt) &&
        Objects.equals(this.shutdownPlan, maintenanceWindow.shutdownPlan) &&
        Objects.equals(this.notificationPlan, maintenanceWindow.notificationPlan) &&
        Objects.equals(this.hooks, maintenanceWindow.hooks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(windowId, title, description, type, environment, zones, services, startAt, endAt, expectedDurationMinutes, status, createdBy, approvedBy, createdAt, updatedAt, shutdownPlan, notificationPlan, hooks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceWindow {\n");
    sb.append("    windowId: ").append(toIndentedString(windowId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    environment: ").append(toIndentedString(environment)).append("\n");
    sb.append("    zones: ").append(toIndentedString(zones)).append("\n");
    sb.append("    services: ").append(toIndentedString(services)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    expectedDurationMinutes: ").append(toIndentedString(expectedDurationMinutes)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    shutdownPlan: ").append(toIndentedString(shutdownPlan)).append("\n");
    sb.append("    notificationPlan: ").append(toIndentedString(notificationPlan)).append("\n");
    sb.append("    hooks: ").append(toIndentedString(hooks)).append("\n");
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

