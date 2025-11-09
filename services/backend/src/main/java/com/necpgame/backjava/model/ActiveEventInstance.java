package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.RandomEvent;
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
 * ActiveEventInstance
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ActiveEventInstance {

  private @Nullable UUID instanceId;

  private @Nullable RandomEvent event;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime triggeredAt;

  private @Nullable Integer timeRemainingSeconds;

  public ActiveEventInstance instanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  @Valid 
  @Schema(name = "instance_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instance_id")
  public @Nullable UUID getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
  }

  public ActiveEventInstance event(@Nullable RandomEvent event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  @Valid 
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable RandomEvent getEvent() {
    return event;
  }

  public void setEvent(@Nullable RandomEvent event) {
    this.event = event;
  }

  public ActiveEventInstance triggeredAt(@Nullable OffsetDateTime triggeredAt) {
    this.triggeredAt = triggeredAt;
    return this;
  }

  /**
   * Get triggeredAt
   * @return triggeredAt
   */
  @Valid 
  @Schema(name = "triggered_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggered_at")
  public @Nullable OffsetDateTime getTriggeredAt() {
    return triggeredAt;
  }

  public void setTriggeredAt(@Nullable OffsetDateTime triggeredAt) {
    this.triggeredAt = triggeredAt;
  }

  public ActiveEventInstance timeRemainingSeconds(@Nullable Integer timeRemainingSeconds) {
    this.timeRemainingSeconds = timeRemainingSeconds;
    return this;
  }

  /**
   * Get timeRemainingSeconds
   * @return timeRemainingSeconds
   */
  
  @Schema(name = "time_remaining_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_remaining_seconds")
  public @Nullable Integer getTimeRemainingSeconds() {
    return timeRemainingSeconds;
  }

  public void setTimeRemainingSeconds(@Nullable Integer timeRemainingSeconds) {
    this.timeRemainingSeconds = timeRemainingSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActiveEventInstance activeEventInstance = (ActiveEventInstance) o;
    return Objects.equals(this.instanceId, activeEventInstance.instanceId) &&
        Objects.equals(this.event, activeEventInstance.event) &&
        Objects.equals(this.triggeredAt, activeEventInstance.triggeredAt) &&
        Objects.equals(this.timeRemainingSeconds, activeEventInstance.timeRemainingSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, event, triggeredAt, timeRemainingSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActiveEventInstance {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    triggeredAt: ").append(toIndentedString(triggeredAt)).append("\n");
    sb.append("    timeRemainingSeconds: ").append(toIndentedString(timeRemainingSeconds)).append("\n");
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

