package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * LogisticsAlertEvent
 */


public class LogisticsAlertEvent {

  private UUID routeId;

  private String status;

  private Integer threatLevel;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  /**
   * Gets or Sets alertType
   */
  public enum AlertTypeEnum {
    AMBUSH("ambush"),
    
    BLOCKADE("blockade"),
    
    ESCORT_REQUEST("escort_request"),
    
    SUPPLY_DROP("supply_drop");

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

  private @Nullable AlertTypeEnum alertType;

  private @Nullable String message;

  private @Nullable String recommendedAction;

  public LogisticsAlertEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LogisticsAlertEvent(UUID routeId, String status, Integer threatLevel, OffsetDateTime timestamp) {
    this.routeId = routeId;
    this.status = status;
    this.threatLevel = threatLevel;
    this.timestamp = timestamp;
  }

  public LogisticsAlertEvent routeId(UUID routeId) {
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

  public LogisticsAlertEvent status(String status) {
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

  public LogisticsAlertEvent threatLevel(Integer threatLevel) {
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

  public LogisticsAlertEvent timestamp(OffsetDateTime timestamp) {
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

  public LogisticsAlertEvent alertType(@Nullable AlertTypeEnum alertType) {
    this.alertType = alertType;
    return this;
  }

  /**
   * Get alertType
   * @return alertType
   */
  
  @Schema(name = "alertType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alertType")
  public @Nullable AlertTypeEnum getAlertType() {
    return alertType;
  }

  public void setAlertType(@Nullable AlertTypeEnum alertType) {
    this.alertType = alertType;
  }

  public LogisticsAlertEvent message(@Nullable String message) {
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

  public LogisticsAlertEvent recommendedAction(@Nullable String recommendedAction) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogisticsAlertEvent logisticsAlertEvent = (LogisticsAlertEvent) o;
    return Objects.equals(this.routeId, logisticsAlertEvent.routeId) &&
        Objects.equals(this.status, logisticsAlertEvent.status) &&
        Objects.equals(this.threatLevel, logisticsAlertEvent.threatLevel) &&
        Objects.equals(this.timestamp, logisticsAlertEvent.timestamp) &&
        Objects.equals(this.alertType, logisticsAlertEvent.alertType) &&
        Objects.equals(this.message, logisticsAlertEvent.message) &&
        Objects.equals(this.recommendedAction, logisticsAlertEvent.recommendedAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routeId, status, threatLevel, timestamp, alertType, message, recommendedAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogisticsAlertEvent {\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    threatLevel: ").append(toIndentedString(threatLevel)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    alertType: ").append(toIndentedString(alertType)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    recommendedAction: ").append(toIndentedString(recommendedAction)).append("\n");
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

