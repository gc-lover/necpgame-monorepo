package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TelemetrySnapshot
 */


public class TelemetrySnapshot {

  private @Nullable Integer analyticsJobLatencyMs;

  private @Nullable Integer autotuneActionsTotal;

  private @Nullable Integer alertsOpenTotal;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public TelemetrySnapshot analyticsJobLatencyMs(@Nullable Integer analyticsJobLatencyMs) {
    this.analyticsJobLatencyMs = analyticsJobLatencyMs;
    return this;
  }

  /**
   * Get analyticsJobLatencyMs
   * minimum: 0
   * @return analyticsJobLatencyMs
   */
  @Min(value = 0) 
  @Schema(name = "analyticsJobLatencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("analyticsJobLatencyMs")
  public @Nullable Integer getAnalyticsJobLatencyMs() {
    return analyticsJobLatencyMs;
  }

  public void setAnalyticsJobLatencyMs(@Nullable Integer analyticsJobLatencyMs) {
    this.analyticsJobLatencyMs = analyticsJobLatencyMs;
  }

  public TelemetrySnapshot autotuneActionsTotal(@Nullable Integer autotuneActionsTotal) {
    this.autotuneActionsTotal = autotuneActionsTotal;
    return this;
  }

  /**
   * Get autotuneActionsTotal
   * minimum: 0
   * @return autotuneActionsTotal
   */
  @Min(value = 0) 
  @Schema(name = "autotuneActionsTotal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autotuneActionsTotal")
  public @Nullable Integer getAutotuneActionsTotal() {
    return autotuneActionsTotal;
  }

  public void setAutotuneActionsTotal(@Nullable Integer autotuneActionsTotal) {
    this.autotuneActionsTotal = autotuneActionsTotal;
  }

  public TelemetrySnapshot alertsOpenTotal(@Nullable Integer alertsOpenTotal) {
    this.alertsOpenTotal = alertsOpenTotal;
    return this;
  }

  /**
   * Get alertsOpenTotal
   * minimum: 0
   * @return alertsOpenTotal
   */
  @Min(value = 0) 
  @Schema(name = "alertsOpenTotal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alertsOpenTotal")
  public @Nullable Integer getAlertsOpenTotal() {
    return alertsOpenTotal;
  }

  public void setAlertsOpenTotal(@Nullable Integer alertsOpenTotal) {
    this.alertsOpenTotal = alertsOpenTotal;
  }

  public TelemetrySnapshot updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TelemetrySnapshot telemetrySnapshot = (TelemetrySnapshot) o;
    return Objects.equals(this.analyticsJobLatencyMs, telemetrySnapshot.analyticsJobLatencyMs) &&
        Objects.equals(this.autotuneActionsTotal, telemetrySnapshot.autotuneActionsTotal) &&
        Objects.equals(this.alertsOpenTotal, telemetrySnapshot.alertsOpenTotal) &&
        Objects.equals(this.updatedAt, telemetrySnapshot.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(analyticsJobLatencyMs, autotuneActionsTotal, alertsOpenTotal, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TelemetrySnapshot {\n");
    sb.append("    analyticsJobLatencyMs: ").append(toIndentedString(analyticsJobLatencyMs)).append("\n");
    sb.append("    autotuneActionsTotal: ").append(toIndentedString(autotuneActionsTotal)).append("\n");
    sb.append("    alertsOpenTotal: ").append(toIndentedString(alertsOpenTotal)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

