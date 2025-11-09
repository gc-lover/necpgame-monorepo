package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.systemservice.model.IntegrationHooks;
import com.necpgame.systemservice.model.NotificationPlan;
import com.necpgame.systemservice.model.ShutdownPlan;
import java.time.OffsetDateTime;
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
 * MaintenanceWindowUpdateRequest
 */


public class MaintenanceWindowUpdateRequest {

  private @Nullable String title;

  private @Nullable String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endAt;

  private @Nullable Integer expectedDurationMinutes;

  private @Nullable NotificationPlan notificationPlan;

  private @Nullable ShutdownPlan shutdownPlan;

  private @Nullable IntegrationHooks hooks;

  private @Nullable String notes;

  public MaintenanceWindowUpdateRequest title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public MaintenanceWindowUpdateRequest description(@Nullable String description) {
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

  public MaintenanceWindowUpdateRequest startAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * Get startAt
   * @return startAt
   */
  @Valid 
  @Schema(name = "startAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startAt")
  public @Nullable OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public MaintenanceWindowUpdateRequest endAt(@Nullable OffsetDateTime endAt) {
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

  public MaintenanceWindowUpdateRequest expectedDurationMinutes(@Nullable Integer expectedDurationMinutes) {
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

  public MaintenanceWindowUpdateRequest notificationPlan(@Nullable NotificationPlan notificationPlan) {
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

  public MaintenanceWindowUpdateRequest shutdownPlan(@Nullable ShutdownPlan shutdownPlan) {
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

  public MaintenanceWindowUpdateRequest hooks(@Nullable IntegrationHooks hooks) {
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

  public MaintenanceWindowUpdateRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceWindowUpdateRequest maintenanceWindowUpdateRequest = (MaintenanceWindowUpdateRequest) o;
    return Objects.equals(this.title, maintenanceWindowUpdateRequest.title) &&
        Objects.equals(this.description, maintenanceWindowUpdateRequest.description) &&
        Objects.equals(this.startAt, maintenanceWindowUpdateRequest.startAt) &&
        Objects.equals(this.endAt, maintenanceWindowUpdateRequest.endAt) &&
        Objects.equals(this.expectedDurationMinutes, maintenanceWindowUpdateRequest.expectedDurationMinutes) &&
        Objects.equals(this.notificationPlan, maintenanceWindowUpdateRequest.notificationPlan) &&
        Objects.equals(this.shutdownPlan, maintenanceWindowUpdateRequest.shutdownPlan) &&
        Objects.equals(this.hooks, maintenanceWindowUpdateRequest.hooks) &&
        Objects.equals(this.notes, maintenanceWindowUpdateRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, startAt, endAt, expectedDurationMinutes, notificationPlan, shutdownPlan, hooks, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceWindowUpdateRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    expectedDurationMinutes: ").append(toIndentedString(expectedDurationMinutes)).append("\n");
    sb.append("    notificationPlan: ").append(toIndentedString(notificationPlan)).append("\n");
    sb.append("    shutdownPlan: ").append(toIndentedString(shutdownPlan)).append("\n");
    sb.append("    hooks: ").append(toIndentedString(hooks)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

