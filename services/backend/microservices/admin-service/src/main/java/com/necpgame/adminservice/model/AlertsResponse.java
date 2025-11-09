package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.Alert;
import com.necpgame.adminservice.model.TelemetrySnapshot;
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
 * AlertsResponse
 */


public class AlertsResponse {

  @Valid
  private List<@Valid Alert> alerts = new ArrayList<>();

  private @Nullable TelemetrySnapshot telemetry;

  public AlertsResponse alerts(List<@Valid Alert> alerts) {
    this.alerts = alerts;
    return this;
  }

  public AlertsResponse addAlertsItem(Alert alertsItem) {
    if (this.alerts == null) {
      this.alerts = new ArrayList<>();
    }
    this.alerts.add(alertsItem);
    return this;
  }

  /**
   * Get alerts
   * @return alerts
   */
  @Valid 
  @Schema(name = "alerts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alerts")
  public List<@Valid Alert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<@Valid Alert> alerts) {
    this.alerts = alerts;
  }

  public AlertsResponse telemetry(@Nullable TelemetrySnapshot telemetry) {
    this.telemetry = telemetry;
    return this;
  }

  /**
   * Get telemetry
   * @return telemetry
   */
  @Valid 
  @Schema(name = "telemetry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telemetry")
  public @Nullable TelemetrySnapshot getTelemetry() {
    return telemetry;
  }

  public void setTelemetry(@Nullable TelemetrySnapshot telemetry) {
    this.telemetry = telemetry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AlertsResponse alertsResponse = (AlertsResponse) o;
    return Objects.equals(this.alerts, alertsResponse.alerts) &&
        Objects.equals(this.telemetry, alertsResponse.telemetry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alerts, telemetry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlertsResponse {\n");
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
    sb.append("    telemetry: ").append(toIndentedString(telemetry)).append("\n");
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

