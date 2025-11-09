package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ZoneState
 */


public class ZoneState {

  private @Nullable BigDecimal loadFactor;

  private @Nullable BigDecimal stabilityScore;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastMigratedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime scheduledMaintenanceAt;

  public ZoneState loadFactor(@Nullable BigDecimal loadFactor) {
    this.loadFactor = loadFactor;
    return this;
  }

  /**
   * Get loadFactor
   * minimum: 0
   * maximum: 1
   * @return loadFactor
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "loadFactor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loadFactor")
  public @Nullable BigDecimal getLoadFactor() {
    return loadFactor;
  }

  public void setLoadFactor(@Nullable BigDecimal loadFactor) {
    this.loadFactor = loadFactor;
  }

  public ZoneState stabilityScore(@Nullable BigDecimal stabilityScore) {
    this.stabilityScore = stabilityScore;
    return this;
  }

  /**
   * Get stabilityScore
   * minimum: 0
   * maximum: 1
   * @return stabilityScore
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "stabilityScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stabilityScore")
  public @Nullable BigDecimal getStabilityScore() {
    return stabilityScore;
  }

  public void setStabilityScore(@Nullable BigDecimal stabilityScore) {
    this.stabilityScore = stabilityScore;
  }

  public ZoneState lastMigratedAt(@Nullable OffsetDateTime lastMigratedAt) {
    this.lastMigratedAt = lastMigratedAt;
    return this;
  }

  /**
   * Get lastMigratedAt
   * @return lastMigratedAt
   */
  @Valid 
  @Schema(name = "lastMigratedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastMigratedAt")
  public @Nullable OffsetDateTime getLastMigratedAt() {
    return lastMigratedAt;
  }

  public void setLastMigratedAt(@Nullable OffsetDateTime lastMigratedAt) {
    this.lastMigratedAt = lastMigratedAt;
  }

  public ZoneState scheduledMaintenanceAt(@Nullable OffsetDateTime scheduledMaintenanceAt) {
    this.scheduledMaintenanceAt = scheduledMaintenanceAt;
    return this;
  }

  /**
   * Get scheduledMaintenanceAt
   * @return scheduledMaintenanceAt
   */
  @Valid 
  @Schema(name = "scheduledMaintenanceAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledMaintenanceAt")
  public @Nullable OffsetDateTime getScheduledMaintenanceAt() {
    return scheduledMaintenanceAt;
  }

  public void setScheduledMaintenanceAt(@Nullable OffsetDateTime scheduledMaintenanceAt) {
    this.scheduledMaintenanceAt = scheduledMaintenanceAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneState zoneState = (ZoneState) o;
    return Objects.equals(this.loadFactor, zoneState.loadFactor) &&
        Objects.equals(this.stabilityScore, zoneState.stabilityScore) &&
        Objects.equals(this.lastMigratedAt, zoneState.lastMigratedAt) &&
        Objects.equals(this.scheduledMaintenanceAt, zoneState.scheduledMaintenanceAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(loadFactor, stabilityScore, lastMigratedAt, scheduledMaintenanceAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneState {\n");
    sb.append("    loadFactor: ").append(toIndentedString(loadFactor)).append("\n");
    sb.append("    stabilityScore: ").append(toIndentedString(stabilityScore)).append("\n");
    sb.append("    lastMigratedAt: ").append(toIndentedString(lastMigratedAt)).append("\n");
    sb.append("    scheduledMaintenanceAt: ").append(toIndentedString(scheduledMaintenanceAt)).append("\n");
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

