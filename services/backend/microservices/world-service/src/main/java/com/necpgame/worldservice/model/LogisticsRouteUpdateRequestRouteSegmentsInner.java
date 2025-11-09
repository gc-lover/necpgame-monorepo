package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * LogisticsRouteUpdateRequestRouteSegmentsInner
 */

@JsonTypeName("LogisticsRouteUpdateRequest_routeSegments_inner")

public class LogisticsRouteUpdateRequestRouteSegmentsInner {

  private @Nullable String waypoint;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eta;

  private @Nullable BigDecimal riskIndex;

  public LogisticsRouteUpdateRequestRouteSegmentsInner waypoint(@Nullable String waypoint) {
    this.waypoint = waypoint;
    return this;
  }

  /**
   * Get waypoint
   * @return waypoint
   */
  
  @Schema(name = "waypoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("waypoint")
  public @Nullable String getWaypoint() {
    return waypoint;
  }

  public void setWaypoint(@Nullable String waypoint) {
    this.waypoint = waypoint;
  }

  public LogisticsRouteUpdateRequestRouteSegmentsInner eta(@Nullable OffsetDateTime eta) {
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

  public LogisticsRouteUpdateRequestRouteSegmentsInner riskIndex(@Nullable BigDecimal riskIndex) {
    this.riskIndex = riskIndex;
    return this;
  }

  /**
   * Get riskIndex
   * @return riskIndex
   */
  @Valid 
  @Schema(name = "riskIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("riskIndex")
  public @Nullable BigDecimal getRiskIndex() {
    return riskIndex;
  }

  public void setRiskIndex(@Nullable BigDecimal riskIndex) {
    this.riskIndex = riskIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogisticsRouteUpdateRequestRouteSegmentsInner logisticsRouteUpdateRequestRouteSegmentsInner = (LogisticsRouteUpdateRequestRouteSegmentsInner) o;
    return Objects.equals(this.waypoint, logisticsRouteUpdateRequestRouteSegmentsInner.waypoint) &&
        Objects.equals(this.eta, logisticsRouteUpdateRequestRouteSegmentsInner.eta) &&
        Objects.equals(this.riskIndex, logisticsRouteUpdateRequestRouteSegmentsInner.riskIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(waypoint, eta, riskIndex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogisticsRouteUpdateRequestRouteSegmentsInner {\n");
    sb.append("    waypoint: ").append(toIndentedString(waypoint)).append("\n");
    sb.append("    eta: ").append(toIndentedString(eta)).append("\n");
    sb.append("    riskIndex: ").append(toIndentedString(riskIndex)).append("\n");
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

