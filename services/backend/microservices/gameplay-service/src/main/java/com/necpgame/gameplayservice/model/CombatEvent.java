package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CombatEvent
 */


public class CombatEvent {

  private @Nullable Integer eventId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    DAMAGE("DAMAGE"),
    
    HEAL("HEAL"),
    
    KILL("KILL"),
    
    ABILITY_USED("ABILITY_USED"),
    
    STATUS_EFFECT("STATUS_EFFECT"),
    
    TURN_START("TURN_START"),
    
    TURN_END("TURN_END");

    private final String value;

    EventTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static EventTypeEnum fromValue(String value) {
      for (EventTypeEnum b : EventTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable EventTypeEnum eventType;

  private JsonNullable<String> actorId = JsonNullable.<String>undefined();

  private JsonNullable<String> targetId = JsonNullable.<String>undefined();

  @Valid
  private Map<String, Object> data = new HashMap<>();

  public CombatEvent eventId(@Nullable Integer eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable Integer getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable Integer eventId) {
    this.eventId = eventId;
  }

  public CombatEvent timestamp(@Nullable OffsetDateTime timestamp) {
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

  public CombatEvent eventType(@Nullable EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  
  @Schema(name = "event_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_type")
  public @Nullable EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(@Nullable EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public CombatEvent actorId(String actorId) {
    this.actorId = JsonNullable.of(actorId);
    return this;
  }

  /**
   * Get actorId
   * @return actorId
   */
  
  @Schema(name = "actor_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actor_id")
  public JsonNullable<String> getActorId() {
    return actorId;
  }

  public void setActorId(JsonNullable<String> actorId) {
    this.actorId = actorId;
  }

  public CombatEvent targetId(String targetId) {
    this.targetId = JsonNullable.of(targetId);
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_id")
  public JsonNullable<String> getTargetId() {
    return targetId;
  }

  public void setTargetId(JsonNullable<String> targetId) {
    this.targetId = targetId;
  }

  public CombatEvent data(Map<String, Object> data) {
    this.data = data;
    return this;
  }

  public CombatEvent putDataItem(String key, Object dataItem) {
    if (this.data == null) {
      this.data = new HashMap<>();
    }
    this.data.put(key, dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  
  @Schema(name = "data", example = "{\"damage\":150,\"is_critical\":true,\"weapon\":\"Mantis Blades\"}", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data")
  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatEvent combatEvent = (CombatEvent) o;
    return Objects.equals(this.eventId, combatEvent.eventId) &&
        Objects.equals(this.timestamp, combatEvent.timestamp) &&
        Objects.equals(this.eventType, combatEvent.eventType) &&
        equalsNullable(this.actorId, combatEvent.actorId) &&
        equalsNullable(this.targetId, combatEvent.targetId) &&
        Objects.equals(this.data, combatEvent.data);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, timestamp, eventType, hashCodeNullable(actorId), hashCodeNullable(targetId), data);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

