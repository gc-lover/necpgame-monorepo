package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HeartbeatMetrics
 */


public class HeartbeatMetrics {

  private @Nullable Integer totalHeartbeats;

  private @Nullable Integer missedHeartbeats;

  private @Nullable BigDecimal averageLatencyMs;

  private @Nullable BigDecimal slaCompliancePercent;

  public HeartbeatMetrics totalHeartbeats(@Nullable Integer totalHeartbeats) {
    this.totalHeartbeats = totalHeartbeats;
    return this;
  }

  /**
   * Get totalHeartbeats
   * @return totalHeartbeats
   */
  
  @Schema(name = "totalHeartbeats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalHeartbeats")
  public @Nullable Integer getTotalHeartbeats() {
    return totalHeartbeats;
  }

  public void setTotalHeartbeats(@Nullable Integer totalHeartbeats) {
    this.totalHeartbeats = totalHeartbeats;
  }

  public HeartbeatMetrics missedHeartbeats(@Nullable Integer missedHeartbeats) {
    this.missedHeartbeats = missedHeartbeats;
    return this;
  }

  /**
   * Get missedHeartbeats
   * @return missedHeartbeats
   */
  
  @Schema(name = "missedHeartbeats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missedHeartbeats")
  public @Nullable Integer getMissedHeartbeats() {
    return missedHeartbeats;
  }

  public void setMissedHeartbeats(@Nullable Integer missedHeartbeats) {
    this.missedHeartbeats = missedHeartbeats;
  }

  public HeartbeatMetrics averageLatencyMs(@Nullable BigDecimal averageLatencyMs) {
    this.averageLatencyMs = averageLatencyMs;
    return this;
  }

  /**
   * Get averageLatencyMs
   * @return averageLatencyMs
   */
  @Valid 
  @Schema(name = "averageLatencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageLatencyMs")
  public @Nullable BigDecimal getAverageLatencyMs() {
    return averageLatencyMs;
  }

  public void setAverageLatencyMs(@Nullable BigDecimal averageLatencyMs) {
    this.averageLatencyMs = averageLatencyMs;
  }

  public HeartbeatMetrics slaCompliancePercent(@Nullable BigDecimal slaCompliancePercent) {
    this.slaCompliancePercent = slaCompliancePercent;
    return this;
  }

  /**
   * Get slaCompliancePercent
   * @return slaCompliancePercent
   */
  @Valid 
  @Schema(name = "slaCompliancePercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slaCompliancePercent")
  public @Nullable BigDecimal getSlaCompliancePercent() {
    return slaCompliancePercent;
  }

  public void setSlaCompliancePercent(@Nullable BigDecimal slaCompliancePercent) {
    this.slaCompliancePercent = slaCompliancePercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HeartbeatMetrics heartbeatMetrics = (HeartbeatMetrics) o;
    return Objects.equals(this.totalHeartbeats, heartbeatMetrics.totalHeartbeats) &&
        Objects.equals(this.missedHeartbeats, heartbeatMetrics.missedHeartbeats) &&
        Objects.equals(this.averageLatencyMs, heartbeatMetrics.averageLatencyMs) &&
        Objects.equals(this.slaCompliancePercent, heartbeatMetrics.slaCompliancePercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalHeartbeats, missedHeartbeats, averageLatencyMs, slaCompliancePercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HeartbeatMetrics {\n");
    sb.append("    totalHeartbeats: ").append(toIndentedString(totalHeartbeats)).append("\n");
    sb.append("    missedHeartbeats: ").append(toIndentedString(missedHeartbeats)).append("\n");
    sb.append("    averageLatencyMs: ").append(toIndentedString(averageLatencyMs)).append("\n");
    sb.append("    slaCompliancePercent: ").append(toIndentedString(slaCompliancePercent)).append("\n");
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

