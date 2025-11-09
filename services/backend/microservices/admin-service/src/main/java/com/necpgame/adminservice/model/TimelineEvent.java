package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TimelineEvent
 */


public class TimelineEvent {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private @Nullable String actor;

  private String eventType;

  private @Nullable String details;

  public TimelineEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TimelineEvent(OffsetDateTime timestamp, String eventType) {
    this.timestamp = timestamp;
    this.eventType = eventType;
  }

  public TimelineEvent timestamp(OffsetDateTime timestamp) {
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

  public TimelineEvent actor(@Nullable String actor) {
    this.actor = actor;
    return this;
  }

  /**
   * Get actor
   * @return actor
   */
  
  @Schema(name = "actor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actor")
  public @Nullable String getActor() {
    return actor;
  }

  public void setActor(@Nullable String actor) {
    this.actor = actor;
  }

  public TimelineEvent eventType(String eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  @NotNull 
  @Schema(name = "event_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("event_type")
  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public TimelineEvent details(@Nullable String details) {
    this.details = details;
    return this;
  }

  /**
   * Get details
   * @return details
   */
  
  @Schema(name = "details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public @Nullable String getDetails() {
    return details;
  }

  public void setDetails(@Nullable String details) {
    this.details = details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimelineEvent timelineEvent = (TimelineEvent) o;
    return Objects.equals(this.timestamp, timelineEvent.timestamp) &&
        Objects.equals(this.actor, timelineEvent.actor) &&
        Objects.equals(this.eventType, timelineEvent.eventType) &&
        Objects.equals(this.details, timelineEvent.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, actor, eventType, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimelineEvent {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    actor: ").append(toIndentedString(actor)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
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

