package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.LogEntryError;
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
 * LogEntry
 */


public class LogEntry {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  /**
   * Gets or Sets level
   */
  public enum LevelEnum {
    TRACE("TRACE"),
    
    DEBUG("DEBUG"),
    
    INFO("INFO"),
    
    WARN("WARN"),
    
    ERROR("ERROR"),
    
    FATAL("FATAL");

    private final String value;

    LevelEnum(String value) {
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
    public static LevelEnum fromValue(String value) {
      for (LevelEnum b : LevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private LevelEnum level;

  private String service;

  private @Nullable String traceId;

  private @Nullable String playerId;

  private String message;

  private @Nullable LogEntryError error;

  @Valid
  private Map<String, Object> context = new HashMap<>();

  public LogEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LogEntry(LevelEnum level, String service, String message) {
    this.level = level;
    this.service = service;
    this.message = message;
  }

  public LogEntry timestamp(@Nullable OffsetDateTime timestamp) {
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

  public LogEntry level(LevelEnum level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  @NotNull 
  @Schema(name = "level", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public LevelEnum getLevel() {
    return level;
  }

  public void setLevel(LevelEnum level) {
    this.level = level;
  }

  public LogEntry service(String service) {
    this.service = service;
    return this;
  }

  /**
   * Get service
   * @return service
   */
  @NotNull 
  @Schema(name = "service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("service")
  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public LogEntry traceId(@Nullable String traceId) {
    this.traceId = traceId;
    return this;
  }

  /**
   * Get traceId
   * @return traceId
   */
  
  @Schema(name = "trace_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trace_id")
  public @Nullable String getTraceId() {
    return traceId;
  }

  public void setTraceId(@Nullable String traceId) {
    this.traceId = traceId;
  }

  public LogEntry playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public LogEntry message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LogEntry error(@Nullable LogEntryError error) {
    this.error = error;
    return this;
  }

  /**
   * Get error
   * @return error
   */
  @Valid 
  @Schema(name = "error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error")
  public @Nullable LogEntryError getError() {
    return error;
  }

  public void setError(@Nullable LogEntryError error) {
    this.error = error;
  }

  public LogEntry context(Map<String, Object> context) {
    this.context = context;
    return this;
  }

  public LogEntry putContextItem(String key, Object contextItem) {
    if (this.context == null) {
      this.context = new HashMap<>();
    }
    this.context.put(key, contextItem);
    return this;
  }

  /**
   * Get context
   * @return context
   */
  
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public Map<String, Object> getContext() {
    return context;
  }

  public void setContext(Map<String, Object> context) {
    this.context = context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogEntry logEntry = (LogEntry) o;
    return Objects.equals(this.timestamp, logEntry.timestamp) &&
        Objects.equals(this.level, logEntry.level) &&
        Objects.equals(this.service, logEntry.service) &&
        Objects.equals(this.traceId, logEntry.traceId) &&
        Objects.equals(this.playerId, logEntry.playerId) &&
        Objects.equals(this.message, logEntry.message) &&
        Objects.equals(this.error, logEntry.error) &&
        Objects.equals(this.context, logEntry.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, level, service, traceId, playerId, message, error, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogEntry {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
    sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
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

