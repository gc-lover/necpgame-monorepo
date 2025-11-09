package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ZoneEvacuationPlan
 */


public class ZoneEvacuationPlan {

  private String targetZoneId;

  private Integer batchSize;

  private Integer intervalMs;

  private @Nullable Boolean notifyPlayers;

  private @Nullable Integer timeoutSeconds;

  public ZoneEvacuationPlan() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ZoneEvacuationPlan(String targetZoneId, Integer batchSize, Integer intervalMs) {
    this.targetZoneId = targetZoneId;
    this.batchSize = batchSize;
    this.intervalMs = intervalMs;
  }

  public ZoneEvacuationPlan targetZoneId(String targetZoneId) {
    this.targetZoneId = targetZoneId;
    return this;
  }

  /**
   * Get targetZoneId
   * @return targetZoneId
   */
  @NotNull 
  @Schema(name = "targetZoneId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetZoneId")
  public String getTargetZoneId() {
    return targetZoneId;
  }

  public void setTargetZoneId(String targetZoneId) {
    this.targetZoneId = targetZoneId;
  }

  public ZoneEvacuationPlan batchSize(Integer batchSize) {
    this.batchSize = batchSize;
    return this;
  }

  /**
   * Get batchSize
   * minimum: 5
   * maximum: 100
   * @return batchSize
   */
  @NotNull @Min(value = 5) @Max(value = 100) 
  @Schema(name = "batchSize", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("batchSize")
  public Integer getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(Integer batchSize) {
    this.batchSize = batchSize;
  }

  public ZoneEvacuationPlan intervalMs(Integer intervalMs) {
    this.intervalMs = intervalMs;
    return this;
  }

  /**
   * Get intervalMs
   * minimum: 100
   * @return intervalMs
   */
  @NotNull @Min(value = 100) 
  @Schema(name = "intervalMs", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("intervalMs")
  public Integer getIntervalMs() {
    return intervalMs;
  }

  public void setIntervalMs(Integer intervalMs) {
    this.intervalMs = intervalMs;
  }

  public ZoneEvacuationPlan notifyPlayers(@Nullable Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
    return this;
  }

  /**
   * Get notifyPlayers
   * @return notifyPlayers
   */
  
  @Schema(name = "notifyPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyPlayers")
  public @Nullable Boolean getNotifyPlayers() {
    return notifyPlayers;
  }

  public void setNotifyPlayers(@Nullable Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
  }

  public ZoneEvacuationPlan timeoutSeconds(@Nullable Integer timeoutSeconds) {
    this.timeoutSeconds = timeoutSeconds;
    return this;
  }

  /**
   * Get timeoutSeconds
   * minimum: 30
   * @return timeoutSeconds
   */
  @Min(value = 30) 
  @Schema(name = "timeoutSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeoutSeconds")
  public @Nullable Integer getTimeoutSeconds() {
    return timeoutSeconds;
  }

  public void setTimeoutSeconds(@Nullable Integer timeoutSeconds) {
    this.timeoutSeconds = timeoutSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneEvacuationPlan zoneEvacuationPlan = (ZoneEvacuationPlan) o;
    return Objects.equals(this.targetZoneId, zoneEvacuationPlan.targetZoneId) &&
        Objects.equals(this.batchSize, zoneEvacuationPlan.batchSize) &&
        Objects.equals(this.intervalMs, zoneEvacuationPlan.intervalMs) &&
        Objects.equals(this.notifyPlayers, zoneEvacuationPlan.notifyPlayers) &&
        Objects.equals(this.timeoutSeconds, zoneEvacuationPlan.timeoutSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetZoneId, batchSize, intervalMs, notifyPlayers, timeoutSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneEvacuationPlan {\n");
    sb.append("    targetZoneId: ").append(toIndentedString(targetZoneId)).append("\n");
    sb.append("    batchSize: ").append(toIndentedString(batchSize)).append("\n");
    sb.append("    intervalMs: ").append(toIndentedString(intervalMs)).append("\n");
    sb.append("    notifyPlayers: ").append(toIndentedString(notifyPlayers)).append("\n");
    sb.append("    timeoutSeconds: ").append(toIndentedString(timeoutSeconds)).append("\n");
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

