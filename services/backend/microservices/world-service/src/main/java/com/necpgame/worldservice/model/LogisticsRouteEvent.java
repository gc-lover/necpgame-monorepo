package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * LogisticsRouteEvent
 */


public class LogisticsRouteEvent {

  private UUID routeId;

  private String status;

  private Integer threatLevel;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private @Nullable String message;

  @Valid
  private List<String> affectedCargo = new ArrayList<>();

  public LogisticsRouteEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LogisticsRouteEvent(UUID routeId, String status, Integer threatLevel, OffsetDateTime timestamp) {
    this.routeId = routeId;
    this.status = status;
    this.threatLevel = threatLevel;
    this.timestamp = timestamp;
  }

  public LogisticsRouteEvent routeId(UUID routeId) {
    this.routeId = routeId;
    return this;
  }

  /**
   * Get routeId
   * @return routeId
   */
  @NotNull @Valid 
  @Schema(name = "routeId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("routeId")
  public UUID getRouteId() {
    return routeId;
  }

  public void setRouteId(UUID routeId) {
    this.routeId = routeId;
  }

  public LogisticsRouteEvent status(String status) {
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

  public LogisticsRouteEvent threatLevel(Integer threatLevel) {
    this.threatLevel = threatLevel;
    return this;
  }

  /**
   * Get threatLevel
   * @return threatLevel
   */
  @NotNull 
  @Schema(name = "threatLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("threatLevel")
  public Integer getThreatLevel() {
    return threatLevel;
  }

  public void setThreatLevel(Integer threatLevel) {
    this.threatLevel = threatLevel;
  }

  public LogisticsRouteEvent timestamp(OffsetDateTime timestamp) {
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

  public LogisticsRouteEvent message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public LogisticsRouteEvent affectedCargo(List<String> affectedCargo) {
    this.affectedCargo = affectedCargo;
    return this;
  }

  public LogisticsRouteEvent addAffectedCargoItem(String affectedCargoItem) {
    if (this.affectedCargo == null) {
      this.affectedCargo = new ArrayList<>();
    }
    this.affectedCargo.add(affectedCargoItem);
    return this;
  }

  /**
   * Get affectedCargo
   * @return affectedCargo
   */
  
  @Schema(name = "affectedCargo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affectedCargo")
  public List<String> getAffectedCargo() {
    return affectedCargo;
  }

  public void setAffectedCargo(List<String> affectedCargo) {
    this.affectedCargo = affectedCargo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogisticsRouteEvent logisticsRouteEvent = (LogisticsRouteEvent) o;
    return Objects.equals(this.routeId, logisticsRouteEvent.routeId) &&
        Objects.equals(this.status, logisticsRouteEvent.status) &&
        Objects.equals(this.threatLevel, logisticsRouteEvent.threatLevel) &&
        Objects.equals(this.timestamp, logisticsRouteEvent.timestamp) &&
        Objects.equals(this.message, logisticsRouteEvent.message) &&
        Objects.equals(this.affectedCargo, logisticsRouteEvent.affectedCargo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routeId, status, threatLevel, timestamp, message, affectedCargo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogisticsRouteEvent {\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    threatLevel: ").append(toIndentedString(threatLevel)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    affectedCargo: ").append(toIndentedString(affectedCargo)).append("\n");
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

