package com.necpgame.gameplayservice.model;

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
 * CompanionEvent
 */


public class CompanionEvent {

  private @Nullable String eventType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime emittedAt;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  public CompanionEvent eventType(@Nullable String eventType) {
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

  public CompanionEvent emittedAt(@Nullable OffsetDateTime emittedAt) {
    this.emittedAt = emittedAt;
    return this;
  }

  /**
   * Get emittedAt
   * @return emittedAt
   */
  @Valid 
  @Schema(name = "emittedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emittedAt")
  public @Nullable OffsetDateTime getEmittedAt() {
    return emittedAt;
  }

  public void setEmittedAt(@Nullable OffsetDateTime emittedAt) {
    this.emittedAt = emittedAt;
  }

  public CompanionEvent payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public CompanionEvent putPayloadItem(String key, Object payloadItem) {
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
    CompanionEvent companionEvent = (CompanionEvent) o;
    return Objects.equals(this.eventType, companionEvent.eventType) &&
        Objects.equals(this.emittedAt, companionEvent.emittedAt) &&
        Objects.equals(this.payload, companionEvent.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, emittedAt, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionEvent {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    emittedAt: ").append(toIndentedString(emittedAt)).append("\n");
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

