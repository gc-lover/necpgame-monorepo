package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * DisconnectEvent
 */


public class DisconnectEvent {

  private String eventId;

  private String sessionId;

  private @Nullable String playerId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  /**
   * Gets or Sets reason
   */
  public enum ReasonEnum {
    NETWORK("network"),
    
    SERVER("server"),
    
    CLIENT("client"),
    
    ANTI_CHEAT("anti_cheat"),
    
    UNKNOWN("unknown");

    private final String value;

    ReasonEnum(String value) {
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
    public static ReasonEnum fromValue(String value) {
      for (ReasonEnum b : ReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ReasonEnum reason;

  private @Nullable Integer durationSeconds;

  private @Nullable Boolean reconnectTokenIssued;

  public DisconnectEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DisconnectEvent(String eventId, String sessionId, OffsetDateTime timestamp) {
    this.eventId = eventId;
    this.sessionId = sessionId;
    this.timestamp = timestamp;
  }

  public DisconnectEvent eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public DisconnectEvent sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @NotNull 
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sessionId")
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public DisconnectEvent playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public DisconnectEvent timestamp(OffsetDateTime timestamp) {
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

  public DisconnectEvent reason(@Nullable ReasonEnum reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable ReasonEnum getReason() {
    return reason;
  }

  public void setReason(@Nullable ReasonEnum reason) {
    this.reason = reason;
  }

  public DisconnectEvent durationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
    return this;
  }

  /**
   * Get durationSeconds
   * minimum: 0
   * @return durationSeconds
   */
  @Min(value = 0) 
  @Schema(name = "durationSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationSeconds")
  public @Nullable Integer getDurationSeconds() {
    return durationSeconds;
  }

  public void setDurationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public DisconnectEvent reconnectTokenIssued(@Nullable Boolean reconnectTokenIssued) {
    this.reconnectTokenIssued = reconnectTokenIssued;
    return this;
  }

  /**
   * Get reconnectTokenIssued
   * @return reconnectTokenIssued
   */
  
  @Schema(name = "reconnectTokenIssued", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reconnectTokenIssued")
  public @Nullable Boolean getReconnectTokenIssued() {
    return reconnectTokenIssued;
  }

  public void setReconnectTokenIssued(@Nullable Boolean reconnectTokenIssued) {
    this.reconnectTokenIssued = reconnectTokenIssued;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DisconnectEvent disconnectEvent = (DisconnectEvent) o;
    return Objects.equals(this.eventId, disconnectEvent.eventId) &&
        Objects.equals(this.sessionId, disconnectEvent.sessionId) &&
        Objects.equals(this.playerId, disconnectEvent.playerId) &&
        Objects.equals(this.timestamp, disconnectEvent.timestamp) &&
        Objects.equals(this.reason, disconnectEvent.reason) &&
        Objects.equals(this.durationSeconds, disconnectEvent.durationSeconds) &&
        Objects.equals(this.reconnectTokenIssued, disconnectEvent.reconnectTokenIssued);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, sessionId, playerId, timestamp, reason, durationSeconds, reconnectTokenIssued);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DisconnectEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    durationSeconds: ").append(toIndentedString(durationSeconds)).append("\n");
    sb.append("    reconnectTokenIssued: ").append(toIndentedString(reconnectTokenIssued)).append("\n");
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

