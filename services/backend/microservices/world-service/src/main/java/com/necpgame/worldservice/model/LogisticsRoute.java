package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.LogisticsRouteCargoManifestInner;
import com.necpgame.worldservice.model.LogisticsRouteTimelineInner;
import com.necpgame.worldservice.model.SquadAssignment;
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
 * LogisticsRoute
 */


public class LogisticsRoute {

  private UUID routeId;

  private UUID originSettlementId;

  private UUID destinationSettlementId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    TRADE("trade"),
    
    TAX("tax"),
    
    REINFORCEMENT("reinforcement"),
    
    AID("aid"),
    
    BLACK_OPS("black_ops");

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

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime scheduledAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eta;

  private Integer securityLevel;

  private Integer threatLevel;

  private @Nullable Integer requestedSecurity;

  @Valid
  private List<@Valid LogisticsRouteCargoManifestInner> cargoManifest = new ArrayList<>();

  @Valid
  private List<@Valid SquadAssignment> activeSquads = new ArrayList<>();

  @Valid
  private List<@Valid LogisticsRouteTimelineInner> timeline = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdated;

  public LogisticsRoute() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LogisticsRoute(UUID routeId, UUID originSettlementId, UUID destinationSettlementId, TypeEnum type, StatusEnum status, OffsetDateTime scheduledAt, Integer securityLevel, Integer threatLevel) {
    this.routeId = routeId;
    this.originSettlementId = originSettlementId;
    this.destinationSettlementId = destinationSettlementId;
    this.type = type;
    this.status = status;
    this.scheduledAt = scheduledAt;
    this.securityLevel = securityLevel;
    this.threatLevel = threatLevel;
  }

  public LogisticsRoute routeId(UUID routeId) {
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

  public LogisticsRoute originSettlementId(UUID originSettlementId) {
    this.originSettlementId = originSettlementId;
    return this;
  }

  /**
   * Get originSettlementId
   * @return originSettlementId
   */
  @NotNull @Valid 
  @Schema(name = "originSettlementId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("originSettlementId")
  public UUID getOriginSettlementId() {
    return originSettlementId;
  }

  public void setOriginSettlementId(UUID originSettlementId) {
    this.originSettlementId = originSettlementId;
  }

  public LogisticsRoute destinationSettlementId(UUID destinationSettlementId) {
    this.destinationSettlementId = destinationSettlementId;
    return this;
  }

  /**
   * Get destinationSettlementId
   * @return destinationSettlementId
   */
  @NotNull @Valid 
  @Schema(name = "destinationSettlementId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("destinationSettlementId")
  public UUID getDestinationSettlementId() {
    return destinationSettlementId;
  }

  public void setDestinationSettlementId(UUID destinationSettlementId) {
    this.destinationSettlementId = destinationSettlementId;
  }

  public LogisticsRoute type(TypeEnum type) {
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

  public LogisticsRoute status(StatusEnum status) {
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

  public LogisticsRoute scheduledAt(OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
    return this;
  }

  /**
   * Get scheduledAt
   * @return scheduledAt
   */
  @NotNull @Valid 
  @Schema(name = "scheduledAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scheduledAt")
  public OffsetDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  public LogisticsRoute eta(@Nullable OffsetDateTime eta) {
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

  public LogisticsRoute securityLevel(Integer securityLevel) {
    this.securityLevel = securityLevel;
    return this;
  }

  /**
   * Get securityLevel
   * minimum: 1
   * maximum: 5
   * @return securityLevel
   */
  @NotNull @Min(value = 1) @Max(value = 5) 
  @Schema(name = "securityLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("securityLevel")
  public Integer getSecurityLevel() {
    return securityLevel;
  }

  public void setSecurityLevel(Integer securityLevel) {
    this.securityLevel = securityLevel;
  }

  public LogisticsRoute threatLevel(Integer threatLevel) {
    this.threatLevel = threatLevel;
    return this;
  }

  /**
   * Get threatLevel
   * minimum: 0
   * maximum: 5
   * @return threatLevel
   */
  @NotNull @Min(value = 0) @Max(value = 5) 
  @Schema(name = "threatLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("threatLevel")
  public Integer getThreatLevel() {
    return threatLevel;
  }

  public void setThreatLevel(Integer threatLevel) {
    this.threatLevel = threatLevel;
  }

  public LogisticsRoute requestedSecurity(@Nullable Integer requestedSecurity) {
    this.requestedSecurity = requestedSecurity;
    return this;
  }

  /**
   * Get requestedSecurity
   * minimum: 1
   * maximum: 5
   * @return requestedSecurity
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "requestedSecurity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestedSecurity")
  public @Nullable Integer getRequestedSecurity() {
    return requestedSecurity;
  }

  public void setRequestedSecurity(@Nullable Integer requestedSecurity) {
    this.requestedSecurity = requestedSecurity;
  }

  public LogisticsRoute cargoManifest(List<@Valid LogisticsRouteCargoManifestInner> cargoManifest) {
    this.cargoManifest = cargoManifest;
    return this;
  }

  public LogisticsRoute addCargoManifestItem(LogisticsRouteCargoManifestInner cargoManifestItem) {
    if (this.cargoManifest == null) {
      this.cargoManifest = new ArrayList<>();
    }
    this.cargoManifest.add(cargoManifestItem);
    return this;
  }

  /**
   * Get cargoManifest
   * @return cargoManifest
   */
  @Valid 
  @Schema(name = "cargoManifest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cargoManifest")
  public List<@Valid LogisticsRouteCargoManifestInner> getCargoManifest() {
    return cargoManifest;
  }

  public void setCargoManifest(List<@Valid LogisticsRouteCargoManifestInner> cargoManifest) {
    this.cargoManifest = cargoManifest;
  }

  public LogisticsRoute activeSquads(List<@Valid SquadAssignment> activeSquads) {
    this.activeSquads = activeSquads;
    return this;
  }

  public LogisticsRoute addActiveSquadsItem(SquadAssignment activeSquadsItem) {
    if (this.activeSquads == null) {
      this.activeSquads = new ArrayList<>();
    }
    this.activeSquads.add(activeSquadsItem);
    return this;
  }

  /**
   * Get activeSquads
   * @return activeSquads
   */
  @Valid 
  @Schema(name = "activeSquads", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeSquads")
  public List<@Valid SquadAssignment> getActiveSquads() {
    return activeSquads;
  }

  public void setActiveSquads(List<@Valid SquadAssignment> activeSquads) {
    this.activeSquads = activeSquads;
  }

  public LogisticsRoute timeline(List<@Valid LogisticsRouteTimelineInner> timeline) {
    this.timeline = timeline;
    return this;
  }

  public LogisticsRoute addTimelineItem(LogisticsRouteTimelineInner timelineItem) {
    if (this.timeline == null) {
      this.timeline = new ArrayList<>();
    }
    this.timeline.add(timelineItem);
    return this;
  }

  /**
   * Get timeline
   * @return timeline
   */
  @Valid 
  @Schema(name = "timeline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeline")
  public List<@Valid LogisticsRouteTimelineInner> getTimeline() {
    return timeline;
  }

  public void setTimeline(List<@Valid LogisticsRouteTimelineInner> timeline) {
    this.timeline = timeline;
  }

  public LogisticsRoute lastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  /**
   * Get lastUpdated
   * @return lastUpdated
   */
  @Valid 
  @Schema(name = "lastUpdated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdated")
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
    LogisticsRoute logisticsRoute = (LogisticsRoute) o;
    return Objects.equals(this.routeId, logisticsRoute.routeId) &&
        Objects.equals(this.originSettlementId, logisticsRoute.originSettlementId) &&
        Objects.equals(this.destinationSettlementId, logisticsRoute.destinationSettlementId) &&
        Objects.equals(this.type, logisticsRoute.type) &&
        Objects.equals(this.status, logisticsRoute.status) &&
        Objects.equals(this.scheduledAt, logisticsRoute.scheduledAt) &&
        Objects.equals(this.eta, logisticsRoute.eta) &&
        Objects.equals(this.securityLevel, logisticsRoute.securityLevel) &&
        Objects.equals(this.threatLevel, logisticsRoute.threatLevel) &&
        Objects.equals(this.requestedSecurity, logisticsRoute.requestedSecurity) &&
        Objects.equals(this.cargoManifest, logisticsRoute.cargoManifest) &&
        Objects.equals(this.activeSquads, logisticsRoute.activeSquads) &&
        Objects.equals(this.timeline, logisticsRoute.timeline) &&
        Objects.equals(this.lastUpdated, logisticsRoute.lastUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routeId, originSettlementId, destinationSettlementId, type, status, scheduledAt, eta, securityLevel, threatLevel, requestedSecurity, cargoManifest, activeSquads, timeline, lastUpdated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogisticsRoute {\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    originSettlementId: ").append(toIndentedString(originSettlementId)).append("\n");
    sb.append("    destinationSettlementId: ").append(toIndentedString(destinationSettlementId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    scheduledAt: ").append(toIndentedString(scheduledAt)).append("\n");
    sb.append("    eta: ").append(toIndentedString(eta)).append("\n");
    sb.append("    securityLevel: ").append(toIndentedString(securityLevel)).append("\n");
    sb.append("    threatLevel: ").append(toIndentedString(threatLevel)).append("\n");
    sb.append("    requestedSecurity: ").append(toIndentedString(requestedSecurity)).append("\n");
    sb.append("    cargoManifest: ").append(toIndentedString(cargoManifest)).append("\n");
    sb.append("    activeSquads: ").append(toIndentedString(activeSquads)).append("\n");
    sb.append("    timeline: ").append(toIndentedString(timeline)).append("\n");
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

