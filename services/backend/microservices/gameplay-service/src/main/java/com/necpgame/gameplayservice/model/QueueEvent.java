package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * QueueEvent
 */


public class QueueEvent {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    QUEUE_RANGE_EXPANDED("queue.rangeExpanded"),
    
    QUEUE_PRIORITY_BOOST("queue.priorityBoost"),
    
    QUEUE_MATCH_READY("queue.matchReady");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private UUID ticketId;

  private @Nullable UUID matchId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime emittedAt;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  public QueueEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueueEvent(TypeEnum type, UUID ticketId, OffsetDateTime emittedAt) {
    this.type = type;
    this.ticketId = ticketId;
    this.emittedAt = emittedAt;
  }

  public QueueEvent type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public QueueEvent ticketId(UUID ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  /**
   * Get ticketId
   * @return ticketId
   */
  @NotNull @Valid 
  @Schema(name = "ticketId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ticketId")
  public UUID getTicketId() {
    return ticketId;
  }

  public void setTicketId(UUID ticketId) {
    this.ticketId = ticketId;
  }

  public QueueEvent matchId(@Nullable UUID matchId) {
    this.matchId = matchId;
    return this;
  }

  /**
   * Get matchId
   * @return matchId
   */
  @Valid 
  @Schema(name = "matchId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("matchId")
  public @Nullable UUID getMatchId() {
    return matchId;
  }

  public void setMatchId(@Nullable UUID matchId) {
    this.matchId = matchId;
  }

  public QueueEvent emittedAt(OffsetDateTime emittedAt) {
    this.emittedAt = emittedAt;
    return this;
  }

  /**
   * Get emittedAt
   * @return emittedAt
   */
  @NotNull @Valid 
  @Schema(name = "emittedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("emittedAt")
  public OffsetDateTime getEmittedAt() {
    return emittedAt;
  }

  public void setEmittedAt(OffsetDateTime emittedAt) {
    this.emittedAt = emittedAt;
  }

  public QueueEvent payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public QueueEvent putPayloadItem(String key, Object payloadItem) {
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
    QueueEvent queueEvent = (QueueEvent) o;
    return Objects.equals(this.type, queueEvent.type) &&
        Objects.equals(this.ticketId, queueEvent.ticketId) &&
        Objects.equals(this.matchId, queueEvent.matchId) &&
        Objects.equals(this.emittedAt, queueEvent.emittedAt) &&
        Objects.equals(this.payload, queueEvent.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, ticketId, matchId, emittedAt, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueueEvent {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
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

