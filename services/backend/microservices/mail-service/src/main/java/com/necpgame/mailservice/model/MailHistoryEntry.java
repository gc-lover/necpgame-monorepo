package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * MailHistoryEntry
 */


public class MailHistoryEntry {

  private @Nullable String event;

  private @Nullable String actorId;

  private @Nullable String actorType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  @Valid
  private Map<String, Object> details = new HashMap<>();

  public MailHistoryEntry event(@Nullable String event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable String getEvent() {
    return event;
  }

  public void setEvent(@Nullable String event) {
    this.event = event;
  }

  public MailHistoryEntry actorId(@Nullable String actorId) {
    this.actorId = actorId;
    return this;
  }

  /**
   * Get actorId
   * @return actorId
   */
  
  @Schema(name = "actorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actorId")
  public @Nullable String getActorId() {
    return actorId;
  }

  public void setActorId(@Nullable String actorId) {
    this.actorId = actorId;
  }

  public MailHistoryEntry actorType(@Nullable String actorType) {
    this.actorType = actorType;
    return this;
  }

  /**
   * Get actorType
   * @return actorType
   */
  
  @Schema(name = "actorType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actorType")
  public @Nullable String getActorType() {
    return actorType;
  }

  public void setActorType(@Nullable String actorType) {
    this.actorType = actorType;
  }

  public MailHistoryEntry timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public MailHistoryEntry details(Map<String, Object> details) {
    this.details = details;
    return this;
  }

  public MailHistoryEntry putDetailsItem(String key, Object detailsItem) {
    if (this.details == null) {
      this.details = new HashMap<>();
    }
    this.details.put(key, detailsItem);
    return this;
  }

  /**
   * Get details
   * @return details
   */
  
  @Schema(name = "details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public Map<String, Object> getDetails() {
    return details;
  }

  public void setDetails(Map<String, Object> details) {
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
    MailHistoryEntry mailHistoryEntry = (MailHistoryEntry) o;
    return Objects.equals(this.event, mailHistoryEntry.event) &&
        Objects.equals(this.actorId, mailHistoryEntry.actorId) &&
        Objects.equals(this.actorType, mailHistoryEntry.actorType) &&
        Objects.equals(this.timestamp, mailHistoryEntry.timestamp) &&
        Objects.equals(this.details, mailHistoryEntry.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event, actorId, actorType, timestamp, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailHistoryEntry {\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
    sb.append("    actorType: ").append(toIndentedString(actorType)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

