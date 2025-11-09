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
 * MaintenanceWindowCreateRequest
 */


public class MaintenanceWindowCreateRequest {

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

  private @Nullable ShutdownPlan shutdownPlan;

  private NotificationPlan notificationPlan;

  private @Nullable IntegrationHooks hooks;

  @Valid
  private List<String> approvalsRequired = new ArrayList<>();

  public MaintenanceWindowCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceWindowCreateRequest(String title, TypeEnum type, EnvironmentEnum environment, OffsetDateTime startAt, NotificationPlan notificationPlan) {
    this.title = title;
    this.type = type;
    this.environment = environment;
    this.startAt = startAt;
    this.notificationPlan = notificationPlan;
  }

  public MaintenanceWindowCreateRequest title(String title) {
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

  public MaintenanceWindowCreateRequest description(@Nullable String description) {
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

  public MaintenanceWindowCreateRequest type(TypeEnum type) {
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

  public MaintenanceWindowCreateRequest environment(EnvironmentEnum environment) {
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

  public MaintenanceWindowCreateRequest zones(List<String> zones) {
    this.zones = zones;
    return this;
  }

  public MaintenanceWindowCreateRequest addZonesItem(String zonesItem) {
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

  public MaintenanceWindowCreateRequest services(List<String> services) {
    this.services = services;
    return this;
  }

  public MaintenanceWindowCreateRequest addServicesItem(String servicesItem) {
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

  public MaintenanceWindowCreateRequest startAt(OffsetDateTime startAt) {
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

  public MaintenanceWindowCreateRequest endAt(@Nullable OffsetDateTime endAt) {
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

  public MaintenanceWindowCreateRequest expectedDurationMinutes(@Nullable Integer expectedDurationMinutes) {
    this.expectedDurationMinutes = expectedDurationMinutes;
    return this;
  }

  /**
   * Get expectedDurationMinutes
   * minimum: 5
   * @return expectedDurationMinutes
   */
  @Min(value = 5) 
  @Schema(name = "expectedDurationMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expectedDurationMinutes")
  public @Nullable Integer getExpectedDurationMinutes() {
    return expectedDurationMinutes;
  }

  public void setExpectedDurationMinutes(@Nullable Integer expectedDurationMinutes) {
    this.expectedDurationMinutes = expectedDurationMinutes;
  }

  public MaintenanceWindowCreateRequest shutdownPlan(@Nullable ShutdownPlan shutdownPlan) {
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

  public MaintenanceWindowCreateRequest notificationPlan(NotificationPlan notificationPlan) {
    this.notificationPlan = notificationPlan;
    return this;
  }

  /**
   * Get notificationPlan
   * @return notificationPlan
   */
  @NotNull @Valid 
  @Schema(name = "notificationPlan", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("notificationPlan")
  public NotificationPlan getNotificationPlan() {
    return notificationPlan;
  }

  public void setNotificationPlan(NotificationPlan notificationPlan) {
    this.notificationPlan = notificationPlan;
  }

  public MaintenanceWindowCreateRequest hooks(@Nullable IntegrationHooks hooks) {
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

  public MaintenanceWindowCreateRequest approvalsRequired(List<String> approvalsRequired) {
    this.approvalsRequired = approvalsRequired;
    return this;
  }

  public MaintenanceWindowCreateRequest addApprovalsRequiredItem(String approvalsRequiredItem) {
    if (this.approvalsRequired == null) {
      this.approvalsRequired = new ArrayList<>();
    }
    this.approvalsRequired.add(approvalsRequiredItem);
    return this;
  }

  /**
   * Get approvalsRequired
   * @return approvalsRequired
   */
  
  @Schema(name = "approvalsRequired", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approvalsRequired")
  public List<String> getApprovalsRequired() {
    return approvalsRequired;
  }

  public void setApprovalsRequired(List<String> approvalsRequired) {
    this.approvalsRequired = approvalsRequired;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceWindowCreateRequest maintenanceWindowCreateRequest = (MaintenanceWindowCreateRequest) o;
    return Objects.equals(this.title, maintenanceWindowCreateRequest.title) &&
        Objects.equals(this.description, maintenanceWindowCreateRequest.description) &&
        Objects.equals(this.type, maintenanceWindowCreateRequest.type) &&
        Objects.equals(this.environment, maintenanceWindowCreateRequest.environment) &&
        Objects.equals(this.zones, maintenanceWindowCreateRequest.zones) &&
        Objects.equals(this.services, maintenanceWindowCreateRequest.services) &&
        Objects.equals(this.startAt, maintenanceWindowCreateRequest.startAt) &&
        Objects.equals(this.endAt, maintenanceWindowCreateRequest.endAt) &&
        Objects.equals(this.expectedDurationMinutes, maintenanceWindowCreateRequest.expectedDurationMinutes) &&
        Objects.equals(this.shutdownPlan, maintenanceWindowCreateRequest.shutdownPlan) &&
        Objects.equals(this.notificationPlan, maintenanceWindowCreateRequest.notificationPlan) &&
        Objects.equals(this.hooks, maintenanceWindowCreateRequest.hooks) &&
        Objects.equals(this.approvalsRequired, maintenanceWindowCreateRequest.approvalsRequired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, type, environment, zones, services, startAt, endAt, expectedDurationMinutes, shutdownPlan, notificationPlan, hooks, approvalsRequired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceWindowCreateRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    environment: ").append(toIndentedString(environment)).append("\n");
    sb.append("    zones: ").append(toIndentedString(zones)).append("\n");
    sb.append("    services: ").append(toIndentedString(services)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    expectedDurationMinutes: ").append(toIndentedString(expectedDurationMinutes)).append("\n");
    sb.append("    shutdownPlan: ").append(toIndentedString(shutdownPlan)).append("\n");
    sb.append("    notificationPlan: ").append(toIndentedString(notificationPlan)).append("\n");
    sb.append("    hooks: ").append(toIndentedString(hooks)).append("\n");
    sb.append("    approvalsRequired: ").append(toIndentedString(approvalsRequired)).append("\n");
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

