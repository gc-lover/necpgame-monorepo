package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * TicketEvent
 */


public class TicketEvent {

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    TICKET_CREATED("ticket.created"),
    
    TICKET_ASSIGNED("ticket.assigned"),
    
    TICKET_RESPONSE("ticket.response"),
    
    TICKET_SLA_BREACH("ticket.sla.breach"),
    
    TICKET_RESOLVED("ticket.resolved");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime publishedAt;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  public TicketEvent eventType(@Nullable EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  
  @Schema(name = "eventType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventType")
  public @Nullable EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(@Nullable EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public TicketEvent publishedAt(@Nullable OffsetDateTime publishedAt) {
    this.publishedAt = publishedAt;
    return this;
  }

  /**
   * Get publishedAt
   * @return publishedAt
   */
  @Valid 
  @Schema(name = "publishedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("publishedAt")
  public @Nullable OffsetDateTime getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(@Nullable OffsetDateTime publishedAt) {
    this.publishedAt = publishedAt;
  }

  public TicketEvent payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public TicketEvent putPayloadItem(String key, Object payloadItem) {
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
    TicketEvent ticketEvent = (TicketEvent) o;
    return Objects.equals(this.eventType, ticketEvent.eventType) &&
        Objects.equals(this.publishedAt, ticketEvent.publishedAt) &&
        Objects.equals(this.payload, ticketEvent.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, publishedAt, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TicketEvent {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    publishedAt: ").append(toIndentedString(publishedAt)).append("\n");
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

