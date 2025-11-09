package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.AutonomousSquadCurrentWaypoint;
import java.time.OffsetDateTime;
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
 * AutonomousSquadUpdateRequest
 */


public class AutonomousSquadUpdateRequest {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    FORMING("forming"),
    
    EN_ROUTE("en_route"),
    
    ENGAGED("engaged"),
    
    RETREATING("retreating"),
    
    DESTROYED("destroyed");

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

  private @Nullable AutonomousSquadCurrentWaypoint currentWaypoint;

  private @Nullable String engagementReport;

  private @Nullable UUID routeId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eta;

  public AutonomousSquadUpdateRequest status(@Nullable StatusEnum status) {
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

  public AutonomousSquadUpdateRequest threatLevel(@Nullable Integer threatLevel) {
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

  public AutonomousSquadUpdateRequest currentWaypoint(@Nullable AutonomousSquadCurrentWaypoint currentWaypoint) {
    this.currentWaypoint = currentWaypoint;
    return this;
  }

  /**
   * Get currentWaypoint
   * @return currentWaypoint
   */
  @Valid 
  @Schema(name = "currentWaypoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentWaypoint")
  public @Nullable AutonomousSquadCurrentWaypoint getCurrentWaypoint() {
    return currentWaypoint;
  }

  public void setCurrentWaypoint(@Nullable AutonomousSquadCurrentWaypoint currentWaypoint) {
    this.currentWaypoint = currentWaypoint;
  }

  public AutonomousSquadUpdateRequest engagementReport(@Nullable String engagementReport) {
    this.engagementReport = engagementReport;
    return this;
  }

  /**
   * Get engagementReport
   * @return engagementReport
   */
  
  @Schema(name = "engagementReport", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("engagementReport")
  public @Nullable String getEngagementReport() {
    return engagementReport;
  }

  public void setEngagementReport(@Nullable String engagementReport) {
    this.engagementReport = engagementReport;
  }

  public AutonomousSquadUpdateRequest routeId(@Nullable UUID routeId) {
    this.routeId = routeId;
    return this;
  }

  /**
   * Get routeId
   * @return routeId
   */
  @Valid 
  @Schema(name = "routeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("routeId")
  public @Nullable UUID getRouteId() {
    return routeId;
  }

  public void setRouteId(@Nullable UUID routeId) {
    this.routeId = routeId;
  }

  public AutonomousSquadUpdateRequest eta(@Nullable OffsetDateTime eta) {
    this.eta = eta;
    return this;
  }

  /**
   * Get eta
   * @return eta
   */
  @Valid 
  @Schema(name = "eta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eta")
  public @Nullable OffsetDateTime getEta() {
    return eta;
  }

  public void setEta(@Nullable OffsetDateTime eta) {
    this.eta = eta;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutonomousSquadUpdateRequest autonomousSquadUpdateRequest = (AutonomousSquadUpdateRequest) o;
    return Objects.equals(this.status, autonomousSquadUpdateRequest.status) &&
        Objects.equals(this.threatLevel, autonomousSquadUpdateRequest.threatLevel) &&
        Objects.equals(this.currentWaypoint, autonomousSquadUpdateRequest.currentWaypoint) &&
        Objects.equals(this.engagementReport, autonomousSquadUpdateRequest.engagementReport) &&
        Objects.equals(this.routeId, autonomousSquadUpdateRequest.routeId) &&
        Objects.equals(this.eta, autonomousSquadUpdateRequest.eta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, threatLevel, currentWaypoint, engagementReport, routeId, eta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutonomousSquadUpdateRequest {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    threatLevel: ").append(toIndentedString(threatLevel)).append("\n");
    sb.append("    currentWaypoint: ").append(toIndentedString(currentWaypoint)).append("\n");
    sb.append("    engagementReport: ").append(toIndentedString(engagementReport)).append("\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    eta: ").append(toIndentedString(eta)).append("\n");
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

