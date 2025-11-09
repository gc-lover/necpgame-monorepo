package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * WaitTimeAnalyticsSamplesInner
 */

@JsonTypeName("WaitTimeAnalytics_samples_inner")

public class WaitTimeAnalyticsSamplesInner {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private Integer waitSeconds;

  private @Nullable Integer priority;

  public WaitTimeAnalyticsSamplesInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WaitTimeAnalyticsSamplesInner(OffsetDateTime timestamp, Integer waitSeconds) {
    this.timestamp = timestamp;
    this.waitSeconds = waitSeconds;
  }

  public WaitTimeAnalyticsSamplesInner timestamp(OffsetDateTime timestamp) {
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

  public WaitTimeAnalyticsSamplesInner waitSeconds(Integer waitSeconds) {
    this.waitSeconds = waitSeconds;
    return this;
  }

  /**
   * Get waitSeconds
   * @return waitSeconds
   */
  @NotNull 
  @Schema(name = "waitSeconds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("waitSeconds")
  public Integer getWaitSeconds() {
    return waitSeconds;
  }

  public void setWaitSeconds(Integer waitSeconds) {
    this.waitSeconds = waitSeconds;
  }

  public WaitTimeAnalyticsSamplesInner priority(@Nullable Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable Integer getPriority() {
    return priority;
  }

  public void setPriority(@Nullable Integer priority) {
    this.priority = priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WaitTimeAnalyticsSamplesInner waitTimeAnalyticsSamplesInner = (WaitTimeAnalyticsSamplesInner) o;
    return Objects.equals(this.timestamp, waitTimeAnalyticsSamplesInner.timestamp) &&
        Objects.equals(this.waitSeconds, waitTimeAnalyticsSamplesInner.waitSeconds) &&
        Objects.equals(this.priority, waitTimeAnalyticsSamplesInner.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, waitSeconds, priority);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WaitTimeAnalyticsSamplesInner {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    waitSeconds: ").append(toIndentedString(waitSeconds)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
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

