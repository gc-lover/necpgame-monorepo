package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RealtimeAlertRequest
 */


public class RealtimeAlertRequest {

  /**
   * Gets or Sets alertType
   */
  public enum AlertTypeEnum {
    TICK_OVER_50_MS("TICK_OVER_50MS"),
    
    ZONE_OVER_CAPACITY("ZONE_OVER_CAPACITY"),
    
    INSTANCE_UNREACHABLE("INSTANCE_UNREACHABLE"),
    
    REDIS_DEGRADED("REDIS_DEGRADED");

    private final String value;

    AlertTypeEnum(String value) {
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
    public static AlertTypeEnum fromValue(String value) {
      for (AlertTypeEnum b : AlertTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AlertTypeEnum alertType;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    WARNING("warning"),
    
    CRITICAL("critical");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SeverityEnum severity;

  private @Nullable String sourceInstanceId;

  private @Nullable String zoneId;

  @Valid
  private Map<String, Object> metrics = new HashMap<>();

  @Valid
  private List<String> actions = new ArrayList<>();

  public RealtimeAlertRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RealtimeAlertRequest(AlertTypeEnum alertType, SeverityEnum severity) {
    this.alertType = alertType;
    this.severity = severity;
  }

  public RealtimeAlertRequest alertType(AlertTypeEnum alertType) {
    this.alertType = alertType;
    return this;
  }

  /**
   * Get alertType
   * @return alertType
   */
  @NotNull 
  @Schema(name = "alertType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alertType")
  public AlertTypeEnum getAlertType() {
    return alertType;
  }

  public void setAlertType(AlertTypeEnum alertType) {
    this.alertType = alertType;
  }

  public RealtimeAlertRequest severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public RealtimeAlertRequest sourceInstanceId(@Nullable String sourceInstanceId) {
    this.sourceInstanceId = sourceInstanceId;
    return this;
  }

  /**
   * Get sourceInstanceId
   * @return sourceInstanceId
   */
  
  @Schema(name = "sourceInstanceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceInstanceId")
  public @Nullable String getSourceInstanceId() {
    return sourceInstanceId;
  }

  public void setSourceInstanceId(@Nullable String sourceInstanceId) {
    this.sourceInstanceId = sourceInstanceId;
  }

  public RealtimeAlertRequest zoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  
  @Schema(name = "zoneId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zoneId")
  public @Nullable String getZoneId() {
    return zoneId;
  }

  public void setZoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
  }

  public RealtimeAlertRequest metrics(Map<String, Object> metrics) {
    this.metrics = metrics;
    return this;
  }

  public RealtimeAlertRequest putMetricsItem(String key, Object metricsItem) {
    if (this.metrics == null) {
      this.metrics = new HashMap<>();
    }
    this.metrics.put(key, metricsItem);
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public Map<String, Object> getMetrics() {
    return metrics;
  }

  public void setMetrics(Map<String, Object> metrics) {
    this.metrics = metrics;
  }

  public RealtimeAlertRequest actions(List<String> actions) {
    this.actions = actions;
    return this;
  }

  public RealtimeAlertRequest addActionsItem(String actionsItem) {
    if (this.actions == null) {
      this.actions = new ArrayList<>();
    }
    this.actions.add(actionsItem);
    return this;
  }

  /**
   * Get actions
   * @return actions
   */
  
  @Schema(name = "actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actions")
  public List<String> getActions() {
    return actions;
  }

  public void setActions(List<String> actions) {
    this.actions = actions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RealtimeAlertRequest realtimeAlertRequest = (RealtimeAlertRequest) o;
    return Objects.equals(this.alertType, realtimeAlertRequest.alertType) &&
        Objects.equals(this.severity, realtimeAlertRequest.severity) &&
        Objects.equals(this.sourceInstanceId, realtimeAlertRequest.sourceInstanceId) &&
        Objects.equals(this.zoneId, realtimeAlertRequest.zoneId) &&
        Objects.equals(this.metrics, realtimeAlertRequest.metrics) &&
        Objects.equals(this.actions, realtimeAlertRequest.actions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alertType, severity, sourceInstanceId, zoneId, metrics, actions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RealtimeAlertRequest {\n");
    sb.append("    alertType: ").append(toIndentedString(alertType)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    sourceInstanceId: ").append(toIndentedString(sourceInstanceId)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
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

