package com.necpgame.combatservice.model;

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
 * CombatLogEntry
 */


public class CombatLogEntry {

  private @Nullable String entryId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable String eventType;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  public CombatLogEntry entryId(@Nullable String entryId) {
    this.entryId = entryId;
    return this;
  }

  /**
   * Get entryId
   * @return entryId
   */
  
  @Schema(name = "entryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entryId")
  public @Nullable String getEntryId() {
    return entryId;
  }

  public void setEntryId(@Nullable String entryId) {
    this.entryId = entryId;
  }

  public CombatLogEntry timestamp(@Nullable OffsetDateTime timestamp) {
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

  public CombatLogEntry eventType(@Nullable String eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  
  @Schema(name = "eventType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventType")
  public @Nullable String getEventType() {
    return eventType;
  }

  public void setEventType(@Nullable String eventType) {
    this.eventType = eventType;
  }

  public CombatLogEntry payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public CombatLogEntry putPayloadItem(String key, Object payloadItem) {
    if (this.payload == null) {
      this.payload = new HashMap<>();
    }
    this.payload.put(key, payloadItem);
    return this;
  }

  /**
   * Get payload
   * @return payload
   */
  
  @Schema(name = "payload", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payload")
  public Map<String, Object> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatLogEntry combatLogEntry = (CombatLogEntry) o;
    return Objects.equals(this.entryId, combatLogEntry.entryId) &&
        Objects.equals(this.timestamp, combatLogEntry.timestamp) &&
        Objects.equals(this.eventType, combatLogEntry.eventType) &&
        Objects.equals(this.payload, combatLogEntry.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, timestamp, eventType, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatLogEntry {\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
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

