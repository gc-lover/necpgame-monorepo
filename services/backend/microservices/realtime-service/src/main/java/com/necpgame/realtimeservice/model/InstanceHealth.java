package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * InstanceHealth
 */


public class InstanceHealth {

  private @Nullable BigDecimal stabilityScore;

  @Valid
  private List<String> slaBreaches = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastHeartbeatAt;

  public InstanceHealth stabilityScore(@Nullable BigDecimal stabilityScore) {
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

  public InstanceHealth slaBreaches(List<String> slaBreaches) {
    this.slaBreaches = slaBreaches;
    return this;
  }

  public InstanceHealth addSlaBreachesItem(String slaBreachesItem) {
    if (this.slaBreaches == null) {
      this.slaBreaches = new ArrayList<>();
    }
    this.slaBreaches.add(slaBreachesItem);
    return this;
  }

  /**
   * Get slaBreaches
   * @return slaBreaches
   */
  
  @Schema(name = "slaBreaches", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slaBreaches")
  public List<String> getSlaBreaches() {
    return slaBreaches;
  }

  public void setSlaBreaches(List<String> slaBreaches) {
    this.slaBreaches = slaBreaches;
  }

  public InstanceHealth lastHeartbeatAt(@Nullable OffsetDateTime lastHeartbeatAt) {
    this.lastHeartbeatAt = lastHeartbeatAt;
    return this;
  }

  /**
   * Get lastHeartbeatAt
   * @return lastHeartbeatAt
   */
  @Valid 
  @Schema(name = "lastHeartbeatAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastHeartbeatAt")
  public @Nullable OffsetDateTime getLastHeartbeatAt() {
    return lastHeartbeatAt;
  }

  public void setLastHeartbeatAt(@Nullable OffsetDateTime lastHeartbeatAt) {
    this.lastHeartbeatAt = lastHeartbeatAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InstanceHealth instanceHealth = (InstanceHealth) o;
    return Objects.equals(this.stabilityScore, instanceHealth.stabilityScore) &&
        Objects.equals(this.slaBreaches, instanceHealth.slaBreaches) &&
        Objects.equals(this.lastHeartbeatAt, instanceHealth.lastHeartbeatAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stabilityScore, slaBreaches, lastHeartbeatAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InstanceHealth {\n");
    sb.append("    stabilityScore: ").append(toIndentedString(stabilityScore)).append("\n");
    sb.append("    slaBreaches: ").append(toIndentedString(slaBreaches)).append("\n");
    sb.append("    lastHeartbeatAt: ").append(toIndentedString(lastHeartbeatAt)).append("\n");
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

