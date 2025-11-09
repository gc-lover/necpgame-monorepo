package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.systemservice.model.ShutdownPlanDrainStepsInner;
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
 * ShutdownPlan
 */


public class ShutdownPlan {

  private Integer gracePeriodMinutes = 15;

  @Valid
  private List<@Valid ShutdownPlanDrainStepsInner> drainSteps = new ArrayList<>();

  private Boolean queueLock = true;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime forceKickAt;

  @Valid
  private List<String> healthChecks = new ArrayList<>();

  public ShutdownPlan gracePeriodMinutes(Integer gracePeriodMinutes) {
    this.gracePeriodMinutes = gracePeriodMinutes;
    return this;
  }

  /**
   * Get gracePeriodMinutes
   * @return gracePeriodMinutes
   */
  
  @Schema(name = "gracePeriodMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gracePeriodMinutes")
  public Integer getGracePeriodMinutes() {
    return gracePeriodMinutes;
  }

  public void setGracePeriodMinutes(Integer gracePeriodMinutes) {
    this.gracePeriodMinutes = gracePeriodMinutes;
  }

  public ShutdownPlan drainSteps(List<@Valid ShutdownPlanDrainStepsInner> drainSteps) {
    this.drainSteps = drainSteps;
    return this;
  }

  public ShutdownPlan addDrainStepsItem(ShutdownPlanDrainStepsInner drainStepsItem) {
    if (this.drainSteps == null) {
      this.drainSteps = new ArrayList<>();
    }
    this.drainSteps.add(drainStepsItem);
    return this;
  }

  /**
   * Get drainSteps
   * @return drainSteps
   */
  @Valid 
  @Schema(name = "drainSteps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("drainSteps")
  public List<@Valid ShutdownPlanDrainStepsInner> getDrainSteps() {
    return drainSteps;
  }

  public void setDrainSteps(List<@Valid ShutdownPlanDrainStepsInner> drainSteps) {
    this.drainSteps = drainSteps;
  }

  public ShutdownPlan queueLock(Boolean queueLock) {
    this.queueLock = queueLock;
    return this;
  }

  /**
   * Get queueLock
   * @return queueLock
   */
  
  @Schema(name = "queueLock", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueLock")
  public Boolean getQueueLock() {
    return queueLock;
  }

  public void setQueueLock(Boolean queueLock) {
    this.queueLock = queueLock;
  }

  public ShutdownPlan forceKickAt(@Nullable OffsetDateTime forceKickAt) {
    this.forceKickAt = forceKickAt;
    return this;
  }

  /**
   * Get forceKickAt
   * @return forceKickAt
   */
  @Valid 
  @Schema(name = "forceKickAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("forceKickAt")
  public @Nullable OffsetDateTime getForceKickAt() {
    return forceKickAt;
  }

  public void setForceKickAt(@Nullable OffsetDateTime forceKickAt) {
    this.forceKickAt = forceKickAt;
  }

  public ShutdownPlan healthChecks(List<String> healthChecks) {
    this.healthChecks = healthChecks;
    return this;
  }

  public ShutdownPlan addHealthChecksItem(String healthChecksItem) {
    if (this.healthChecks == null) {
      this.healthChecks = new ArrayList<>();
    }
    this.healthChecks.add(healthChecksItem);
    return this;
  }

  /**
   * Get healthChecks
   * @return healthChecks
   */
  
  @Schema(name = "healthChecks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("healthChecks")
  public List<String> getHealthChecks() {
    return healthChecks;
  }

  public void setHealthChecks(List<String> healthChecks) {
    this.healthChecks = healthChecks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShutdownPlan shutdownPlan = (ShutdownPlan) o;
    return Objects.equals(this.gracePeriodMinutes, shutdownPlan.gracePeriodMinutes) &&
        Objects.equals(this.drainSteps, shutdownPlan.drainSteps) &&
        Objects.equals(this.queueLock, shutdownPlan.queueLock) &&
        Objects.equals(this.forceKickAt, shutdownPlan.forceKickAt) &&
        Objects.equals(this.healthChecks, shutdownPlan.healthChecks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gracePeriodMinutes, drainSteps, queueLock, forceKickAt, healthChecks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShutdownPlan {\n");
    sb.append("    gracePeriodMinutes: ").append(toIndentedString(gracePeriodMinutes)).append("\n");
    sb.append("    drainSteps: ").append(toIndentedString(drainSteps)).append("\n");
    sb.append("    queueLock: ").append(toIndentedString(queueLock)).append("\n");
    sb.append("    forceKickAt: ").append(toIndentedString(forceKickAt)).append("\n");
    sb.append("    healthChecks: ").append(toIndentedString(healthChecks)).append("\n");
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

