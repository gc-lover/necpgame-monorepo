package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.AlertSeverity;
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
 * Alert
 */


public class Alert {

  private String alertId;

  private AlertSeverity severity;

  private String message;

  private @Nullable String metricId;

  private @Nullable String factionId;

  private @Nullable String recommendedAction;

  @Valid
  private List<String> impactedSystems = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime detectedAt;

  private Boolean acknowledged = false;

  private Boolean sandbox = false;

  public Alert() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Alert(String alertId, AlertSeverity severity, String message, OffsetDateTime detectedAt) {
    this.alertId = alertId;
    this.severity = severity;
    this.message = message;
    this.detectedAt = detectedAt;
  }

  public Alert alertId(String alertId) {
    this.alertId = alertId;
    return this;
  }

  /**
   * Get alertId
   * @return alertId
   */
  @NotNull 
  @Schema(name = "alertId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alertId")
  public String getAlertId() {
    return alertId;
  }

  public void setAlertId(String alertId) {
    this.alertId = alertId;
  }

  public Alert severity(AlertSeverity severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull @Valid 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public AlertSeverity getSeverity() {
    return severity;
  }

  public void setSeverity(AlertSeverity severity) {
    this.severity = severity;
  }

  public Alert message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Alert metricId(@Nullable String metricId) {
    this.metricId = metricId;
    return this;
  }

  /**
   * Get metricId
   * @return metricId
   */
  
  @Schema(name = "metricId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metricId")
  public @Nullable String getMetricId() {
    return metricId;
  }

  public void setMetricId(@Nullable String metricId) {
    this.metricId = metricId;
  }

  public Alert factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "factionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionId")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public Alert recommendedAction(@Nullable String recommendedAction) {
    this.recommendedAction = recommendedAction;
    return this;
  }

  /**
   * Get recommendedAction
   * @return recommendedAction
   */
  
  @Schema(name = "recommendedAction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedAction")
  public @Nullable String getRecommendedAction() {
    return recommendedAction;
  }

  public void setRecommendedAction(@Nullable String recommendedAction) {
    this.recommendedAction = recommendedAction;
  }

  public Alert impactedSystems(List<String> impactedSystems) {
    this.impactedSystems = impactedSystems;
    return this;
  }

  public Alert addImpactedSystemsItem(String impactedSystemsItem) {
    if (this.impactedSystems == null) {
      this.impactedSystems = new ArrayList<>();
    }
    this.impactedSystems.add(impactedSystemsItem);
    return this;
  }

  /**
   * Get impactedSystems
   * @return impactedSystems
   */
  
  @Schema(name = "impactedSystems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impactedSystems")
  public List<String> getImpactedSystems() {
    return impactedSystems;
  }

  public void setImpactedSystems(List<String> impactedSystems) {
    this.impactedSystems = impactedSystems;
  }

  public Alert detectedAt(OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
    return this;
  }

  /**
   * Get detectedAt
   * @return detectedAt
   */
  @NotNull @Valid 
  @Schema(name = "detectedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("detectedAt")
  public OffsetDateTime getDetectedAt() {
    return detectedAt;
  }

  public void setDetectedAt(OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
  }

  public Alert acknowledged(Boolean acknowledged) {
    this.acknowledged = acknowledged;
    return this;
  }

  /**
   * Get acknowledged
   * @return acknowledged
   */
  
  @Schema(name = "acknowledged", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("acknowledged")
  public Boolean getAcknowledged() {
    return acknowledged;
  }

  public void setAcknowledged(Boolean acknowledged) {
    this.acknowledged = acknowledged;
  }

  public Alert sandbox(Boolean sandbox) {
    this.sandbox = sandbox;
    return this;
  }

  /**
   * Get sandbox
   * @return sandbox
   */
  
  @Schema(name = "sandbox", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sandbox")
  public Boolean getSandbox() {
    return sandbox;
  }

  public void setSandbox(Boolean sandbox) {
    this.sandbox = sandbox;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Alert alert = (Alert) o;
    return Objects.equals(this.alertId, alert.alertId) &&
        Objects.equals(this.severity, alert.severity) &&
        Objects.equals(this.message, alert.message) &&
        Objects.equals(this.metricId, alert.metricId) &&
        Objects.equals(this.factionId, alert.factionId) &&
        Objects.equals(this.recommendedAction, alert.recommendedAction) &&
        Objects.equals(this.impactedSystems, alert.impactedSystems) &&
        Objects.equals(this.detectedAt, alert.detectedAt) &&
        Objects.equals(this.acknowledged, alert.acknowledged) &&
        Objects.equals(this.sandbox, alert.sandbox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alertId, severity, message, metricId, factionId, recommendedAction, impactedSystems, detectedAt, acknowledged, sandbox);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Alert {\n");
    sb.append("    alertId: ").append(toIndentedString(alertId)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    metricId: ").append(toIndentedString(metricId)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    recommendedAction: ").append(toIndentedString(recommendedAction)).append("\n");
    sb.append("    impactedSystems: ").append(toIndentedString(impactedSystems)).append("\n");
    sb.append("    detectedAt: ").append(toIndentedString(detectedAt)).append("\n");
    sb.append("    acknowledged: ").append(toIndentedString(acknowledged)).append("\n");
    sb.append("    sandbox: ").append(toIndentedString(sandbox)).append("\n");
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

