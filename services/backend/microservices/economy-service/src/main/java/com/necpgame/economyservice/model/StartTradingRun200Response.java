package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * StartTradingRun200Response
 */

@JsonTypeName("startTradingRun_200_response")

public class StartTradingRun200Response {

  private @Nullable String runId;

  private @Nullable String routeId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedArrival;

  private @Nullable BigDecimal estimatedProfit;

  @Valid
  private List<String> riskEvents = new ArrayList<>();

  public StartTradingRun200Response runId(@Nullable String runId) {
    this.runId = runId;
    return this;
  }

  /**
   * Get runId
   * @return runId
   */
  
  @Schema(name = "run_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("run_id")
  public @Nullable String getRunId() {
    return runId;
  }

  public void setRunId(@Nullable String runId) {
    this.runId = runId;
  }

  public StartTradingRun200Response routeId(@Nullable String routeId) {
    this.routeId = routeId;
    return this;
  }

  /**
   * Get routeId
   * @return routeId
   */
  
  @Schema(name = "route_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("route_id")
  public @Nullable String getRouteId() {
    return routeId;
  }

  public void setRouteId(@Nullable String routeId) {
    this.routeId = routeId;
  }

  public StartTradingRun200Response estimatedArrival(@Nullable OffsetDateTime estimatedArrival) {
    this.estimatedArrival = estimatedArrival;
    return this;
  }

  /**
   * Get estimatedArrival
   * @return estimatedArrival
   */
  @Valid 
  @Schema(name = "estimated_arrival", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_arrival")
  public @Nullable OffsetDateTime getEstimatedArrival() {
    return estimatedArrival;
  }

  public void setEstimatedArrival(@Nullable OffsetDateTime estimatedArrival) {
    this.estimatedArrival = estimatedArrival;
  }

  public StartTradingRun200Response estimatedProfit(@Nullable BigDecimal estimatedProfit) {
    this.estimatedProfit = estimatedProfit;
    return this;
  }

  /**
   * Get estimatedProfit
   * @return estimatedProfit
   */
  @Valid 
  @Schema(name = "estimated_profit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_profit")
  public @Nullable BigDecimal getEstimatedProfit() {
    return estimatedProfit;
  }

  public void setEstimatedProfit(@Nullable BigDecimal estimatedProfit) {
    this.estimatedProfit = estimatedProfit;
  }

  public StartTradingRun200Response riskEvents(List<String> riskEvents) {
    this.riskEvents = riskEvents;
    return this;
  }

  public StartTradingRun200Response addRiskEventsItem(String riskEventsItem) {
    if (this.riskEvents == null) {
      this.riskEvents = new ArrayList<>();
    }
    this.riskEvents.add(riskEventsItem);
    return this;
  }

  /**
   * Get riskEvents
   * @return riskEvents
   */
  
  @Schema(name = "risk_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_events")
  public List<String> getRiskEvents() {
    return riskEvents;
  }

  public void setRiskEvents(List<String> riskEvents) {
    this.riskEvents = riskEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartTradingRun200Response startTradingRun200Response = (StartTradingRun200Response) o;
    return Objects.equals(this.runId, startTradingRun200Response.runId) &&
        Objects.equals(this.routeId, startTradingRun200Response.routeId) &&
        Objects.equals(this.estimatedArrival, startTradingRun200Response.estimatedArrival) &&
        Objects.equals(this.estimatedProfit, startTradingRun200Response.estimatedProfit) &&
        Objects.equals(this.riskEvents, startTradingRun200Response.riskEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(runId, routeId, estimatedArrival, estimatedProfit, riskEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartTradingRun200Response {\n");
    sb.append("    runId: ").append(toIndentedString(runId)).append("\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    estimatedArrival: ").append(toIndentedString(estimatedArrival)).append("\n");
    sb.append("    estimatedProfit: ").append(toIndentedString(estimatedProfit)).append("\n");
    sb.append("    riskEvents: ").append(toIndentedString(riskEvents)).append("\n");
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

