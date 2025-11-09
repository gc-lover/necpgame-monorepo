package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.RandomEventDetailed;
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
 * TriggeredEventInstance
 */


public class TriggeredEventInstance {

  private @Nullable UUID instanceId;

  private @Nullable RandomEventDetailed event;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime triggeredAt;

  private @Nullable String location;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public TriggeredEventInstance instanceId(@Nullable UUID instanceId) {
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

  public TriggeredEventInstance event(@Nullable RandomEventDetailed event) {
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
  public @Nullable RandomEventDetailed getEvent() {
    return event;
  }

  public void setEvent(@Nullable RandomEventDetailed event) {
    this.event = event;
  }

  public TriggeredEventInstance triggeredAt(@Nullable OffsetDateTime triggeredAt) {
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

  public TriggeredEventInstance location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public TriggeredEventInstance expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Время, после которого событие исчезает
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", description = "Время, после которого событие исчезает", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggeredEventInstance triggeredEventInstance = (TriggeredEventInstance) o;
    return Objects.equals(this.instanceId, triggeredEventInstance.instanceId) &&
        Objects.equals(this.event, triggeredEventInstance.event) &&
        Objects.equals(this.triggeredAt, triggeredEventInstance.triggeredAt) &&
        Objects.equals(this.location, triggeredEventInstance.location) &&
        Objects.equals(this.expiresAt, triggeredEventInstance.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, event, triggeredAt, location, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggeredEventInstance {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    triggeredAt: ").append(toIndentedString(triggeredAt)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

