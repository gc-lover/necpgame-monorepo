package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * AutonomousSquadEvent
 */


public class AutonomousSquadEvent {

  private UUID squadId;

  private String mission;

  private String status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private @Nullable UUID routeId;

  private @Nullable Integer threatLevel;

  private @Nullable String currentWaypoint;

  public AutonomousSquadEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AutonomousSquadEvent(UUID squadId, String mission, String status, OffsetDateTime timestamp) {
    this.squadId = squadId;
    this.mission = mission;
    this.status = status;
    this.timestamp = timestamp;
  }

  public AutonomousSquadEvent squadId(UUID squadId) {
    this.squadId = squadId;
    return this;
  }

  /**
   * Get squadId
   * @return squadId
   */
  @NotNull @Valid 
  @Schema(name = "squadId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("squadId")
  public UUID getSquadId() {
    return squadId;
  }

  public void setSquadId(UUID squadId) {
    this.squadId = squadId;
  }

  public AutonomousSquadEvent mission(String mission) {
    this.mission = mission;
    return this;
  }

  /**
   * Get mission
   * @return mission
   */
  @NotNull 
  @Schema(name = "mission", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mission")
  public String getMission() {
    return mission;
  }

  public void setMission(String mission) {
    this.mission = mission;
  }

  public AutonomousSquadEvent status(String status) {
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
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public AutonomousSquadEvent timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public AutonomousSquadEvent routeId(@Nullable UUID routeId) {
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

  public AutonomousSquadEvent threatLevel(@Nullable Integer threatLevel) {
    this.threatLevel = threatLevel;
    return this;
  }

  /**
   * Get threatLevel
   * @return threatLevel
   */
  
  @Schema(name = "threatLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threatLevel")
  public @Nullable Integer getThreatLevel() {
    return threatLevel;
  }

  public void setThreatLevel(@Nullable Integer threatLevel) {
    this.threatLevel = threatLevel;
  }

  public AutonomousSquadEvent currentWaypoint(@Nullable String currentWaypoint) {
    this.currentWaypoint = currentWaypoint;
    return this;
  }

  /**
   * Get currentWaypoint
   * @return currentWaypoint
   */
  
  @Schema(name = "currentWaypoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentWaypoint")
  public @Nullable String getCurrentWaypoint() {
    return currentWaypoint;
  }

  public void setCurrentWaypoint(@Nullable String currentWaypoint) {
    this.currentWaypoint = currentWaypoint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutonomousSquadEvent autonomousSquadEvent = (AutonomousSquadEvent) o;
    return Objects.equals(this.squadId, autonomousSquadEvent.squadId) &&
        Objects.equals(this.mission, autonomousSquadEvent.mission) &&
        Objects.equals(this.status, autonomousSquadEvent.status) &&
        Objects.equals(this.timestamp, autonomousSquadEvent.timestamp) &&
        Objects.equals(this.routeId, autonomousSquadEvent.routeId) &&
        Objects.equals(this.threatLevel, autonomousSquadEvent.threatLevel) &&
        Objects.equals(this.currentWaypoint, autonomousSquadEvent.currentWaypoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(squadId, mission, status, timestamp, routeId, threatLevel, currentWaypoint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutonomousSquadEvent {\n");
    sb.append("    squadId: ").append(toIndentedString(squadId)).append("\n");
    sb.append("    mission: ").append(toIndentedString(mission)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    threatLevel: ").append(toIndentedString(threatLevel)).append("\n");
    sb.append("    currentWaypoint: ").append(toIndentedString(currentWaypoint)).append("\n");
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

