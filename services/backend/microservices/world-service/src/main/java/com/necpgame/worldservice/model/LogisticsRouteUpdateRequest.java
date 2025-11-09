package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.LogisticsRouteUpdateRequestRouteSegmentsInner;
import com.necpgame.worldservice.model.SquadAssignment;
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
 * LogisticsRouteUpdateRequest
 */


public class LogisticsRouteUpdateRequest {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    SCHEDULED("scheduled"),
    
    ACTIVE("active"),
    
    UNDER_ATTACK("under_attack"),
    
    COMPLETED("completed"),
    
    FAILED("failed"),
    
    PAUSED("paused");

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

  private @Nullable Integer threatLevel;

  @Valid
  private List<@Valid SquadAssignment> assignedSquads = new ArrayList<>();

  @Valid
  private List<@Valid LogisticsRouteUpdateRequestRouteSegmentsInner> routeSegments = new ArrayList<>();

  private @Nullable String incidentReport;

  @Valid
  private List<String> notificationScope = new ArrayList<>();

  public LogisticsRouteUpdateRequest status(@Nullable StatusEnum status) {
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

  public LogisticsRouteUpdateRequest threatLevel(@Nullable Integer threatLevel) {
    this.threatLevel = threatLevel;
    return this;
  }

  /**
   * Get threatLevel
   * minimum: 0
   * maximum: 5
   * @return threatLevel
   */
  @Min(value = 0) @Max(value = 5) 
  @Schema(name = "threatLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threatLevel")
  public @Nullable Integer getThreatLevel() {
    return threatLevel;
  }

  public void setThreatLevel(@Nullable Integer threatLevel) {
    this.threatLevel = threatLevel;
  }

  public LogisticsRouteUpdateRequest assignedSquads(List<@Valid SquadAssignment> assignedSquads) {
    this.assignedSquads = assignedSquads;
    return this;
  }

  public LogisticsRouteUpdateRequest addAssignedSquadsItem(SquadAssignment assignedSquadsItem) {
    if (this.assignedSquads == null) {
      this.assignedSquads = new ArrayList<>();
    }
    this.assignedSquads.add(assignedSquadsItem);
    return this;
  }

  /**
   * Get assignedSquads
   * @return assignedSquads
   */
  @Valid 
  @Schema(name = "assignedSquads", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assignedSquads")
  public List<@Valid SquadAssignment> getAssignedSquads() {
    return assignedSquads;
  }

  public void setAssignedSquads(List<@Valid SquadAssignment> assignedSquads) {
    this.assignedSquads = assignedSquads;
  }

  public LogisticsRouteUpdateRequest routeSegments(List<@Valid LogisticsRouteUpdateRequestRouteSegmentsInner> routeSegments) {
    this.routeSegments = routeSegments;
    return this;
  }

  public LogisticsRouteUpdateRequest addRouteSegmentsItem(LogisticsRouteUpdateRequestRouteSegmentsInner routeSegmentsItem) {
    if (this.routeSegments == null) {
      this.routeSegments = new ArrayList<>();
    }
    this.routeSegments.add(routeSegmentsItem);
    return this;
  }

  /**
   * Get routeSegments
   * @return routeSegments
   */
  @Valid 
  @Schema(name = "routeSegments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("routeSegments")
  public List<@Valid LogisticsRouteUpdateRequestRouteSegmentsInner> getRouteSegments() {
    return routeSegments;
  }

  public void setRouteSegments(List<@Valid LogisticsRouteUpdateRequestRouteSegmentsInner> routeSegments) {
    this.routeSegments = routeSegments;
  }

  public LogisticsRouteUpdateRequest incidentReport(@Nullable String incidentReport) {
    this.incidentReport = incidentReport;
    return this;
  }

  /**
   * Get incidentReport
   * @return incidentReport
   */
  
  @Schema(name = "incidentReport", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incidentReport")
  public @Nullable String getIncidentReport() {
    return incidentReport;
  }

  public void setIncidentReport(@Nullable String incidentReport) {
    this.incidentReport = incidentReport;
  }

  public LogisticsRouteUpdateRequest notificationScope(List<String> notificationScope) {
    this.notificationScope = notificationScope;
    return this;
  }

  public LogisticsRouteUpdateRequest addNotificationScopeItem(String notificationScopeItem) {
    if (this.notificationScope == null) {
      this.notificationScope = new ArrayList<>();
    }
    this.notificationScope.add(notificationScopeItem);
    return this;
  }

  /**
   * Get notificationScope
   * @return notificationScope
   */
  
  @Schema(name = "notificationScope", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notificationScope")
  public List<String> getNotificationScope() {
    return notificationScope;
  }

  public void setNotificationScope(List<String> notificationScope) {
    this.notificationScope = notificationScope;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogisticsRouteUpdateRequest logisticsRouteUpdateRequest = (LogisticsRouteUpdateRequest) o;
    return Objects.equals(this.status, logisticsRouteUpdateRequest.status) &&
        Objects.equals(this.threatLevel, logisticsRouteUpdateRequest.threatLevel) &&
        Objects.equals(this.assignedSquads, logisticsRouteUpdateRequest.assignedSquads) &&
        Objects.equals(this.routeSegments, logisticsRouteUpdateRequest.routeSegments) &&
        Objects.equals(this.incidentReport, logisticsRouteUpdateRequest.incidentReport) &&
        Objects.equals(this.notificationScope, logisticsRouteUpdateRequest.notificationScope);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, threatLevel, assignedSquads, routeSegments, incidentReport, notificationScope);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogisticsRouteUpdateRequest {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    threatLevel: ").append(toIndentedString(threatLevel)).append("\n");
    sb.append("    assignedSquads: ").append(toIndentedString(assignedSquads)).append("\n");
    sb.append("    routeSegments: ").append(toIndentedString(routeSegments)).append("\n");
    sb.append("    incidentReport: ").append(toIndentedString(incidentReport)).append("\n");
    sb.append("    notificationScope: ").append(toIndentedString(notificationScope)).append("\n");
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

