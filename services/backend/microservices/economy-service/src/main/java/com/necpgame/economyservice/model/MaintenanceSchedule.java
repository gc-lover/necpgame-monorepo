package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * MaintenanceSchedule
 */


public class MaintenanceSchedule {

  private @Nullable String scheduleId;

  private @Nullable String window;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime nextRun;

  private @Nullable String assignedTeam;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    SCHEDULED("scheduled"),
    
    IN_PROGRESS("in_progress"),
    
    COMPLETED("completed"),
    
    CANCELLED("cancelled");

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

  public MaintenanceSchedule scheduleId(@Nullable String scheduleId) {
    this.scheduleId = scheduleId;
    return this;
  }

  /**
   * Get scheduleId
   * @return scheduleId
   */
  
  @Schema(name = "scheduleId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduleId")
  public @Nullable String getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(@Nullable String scheduleId) {
    this.scheduleId = scheduleId;
  }

  public MaintenanceSchedule window(@Nullable String window) {
    this.window = window;
    return this;
  }

  /**
   * Get window
   * @return window
   */
  
  @Schema(name = "window", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("window")
  public @Nullable String getWindow() {
    return window;
  }

  public void setWindow(@Nullable String window) {
    this.window = window;
  }

  public MaintenanceSchedule nextRun(@Nullable OffsetDateTime nextRun) {
    this.nextRun = nextRun;
    return this;
  }

  /**
   * Get nextRun
   * @return nextRun
   */
  @Valid 
  @Schema(name = "nextRun", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextRun")
  public @Nullable OffsetDateTime getNextRun() {
    return nextRun;
  }

  public void setNextRun(@Nullable OffsetDateTime nextRun) {
    this.nextRun = nextRun;
  }

  public MaintenanceSchedule assignedTeam(@Nullable String assignedTeam) {
    this.assignedTeam = assignedTeam;
    return this;
  }

  /**
   * Get assignedTeam
   * @return assignedTeam
   */
  
  @Schema(name = "assignedTeam", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assignedTeam")
  public @Nullable String getAssignedTeam() {
    return assignedTeam;
  }

  public void setAssignedTeam(@Nullable String assignedTeam) {
    this.assignedTeam = assignedTeam;
  }

  public MaintenanceSchedule status(@Nullable StatusEnum status) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceSchedule maintenanceSchedule = (MaintenanceSchedule) o;
    return Objects.equals(this.scheduleId, maintenanceSchedule.scheduleId) &&
        Objects.equals(this.window, maintenanceSchedule.window) &&
        Objects.equals(this.nextRun, maintenanceSchedule.nextRun) &&
        Objects.equals(this.assignedTeam, maintenanceSchedule.assignedTeam) &&
        Objects.equals(this.status, maintenanceSchedule.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduleId, window, nextRun, assignedTeam, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceSchedule {\n");
    sb.append("    scheduleId: ").append(toIndentedString(scheduleId)).append("\n");
    sb.append("    window: ").append(toIndentedString(window)).append("\n");
    sb.append("    nextRun: ").append(toIndentedString(nextRun)).append("\n");
    sb.append("    assignedTeam: ").append(toIndentedString(assignedTeam)).append("\n");
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

