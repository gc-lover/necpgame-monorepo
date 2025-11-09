package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootEventNotificationRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootEventNotificationRequest {

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    RARE_DROP("RARE_DROP"),
    
    WORLD_ANNOUNCEMENT("WORLD_ANNOUNCEMENT"),
    
    RAID_SUMMARY("RAID_SUMMARY");

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

  private EventTypeEnum eventType;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  public LootEventNotificationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootEventNotificationRequest(EventTypeEnum eventType, Map<String, Object> payload) {
    this.eventType = eventType;
    this.payload = payload;
  }

  public LootEventNotificationRequest eventType(EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  @NotNull 
  @Schema(name = "eventType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventType")
  public EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public LootEventNotificationRequest payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public LootEventNotificationRequest putPayloadItem(String key, Object payloadItem) {
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
  @NotNull 
  @Schema(name = "payload", requiredMode = Schema.RequiredMode.REQUIRED)
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
    LootEventNotificationRequest lootEventNotificationRequest = (LootEventNotificationRequest) o;
    return Objects.equals(this.eventType, lootEventNotificationRequest.eventType) &&
        Objects.equals(this.payload, lootEventNotificationRequest.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootEventNotificationRequest {\n");
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

