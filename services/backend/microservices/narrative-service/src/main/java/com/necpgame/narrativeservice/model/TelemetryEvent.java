package com.necpgame.narrativeservice.model;

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
 * TelemetryEvent
 */


public class TelemetryEvent {

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    NODE_ENTER("node_enter"),
    
    OPTION_SELECT("option_select"),
    
    SKILL_CHECK("skill_check"),
    
    TUTORIAL_VIEW("tutorial_view");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private @Nullable String nodeId;

  private @Nullable String optionId;

  private @Nullable String outcome;

  private @Nullable Integer latencyMs;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public TelemetryEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TelemetryEvent(EventTypeEnum eventType, OffsetDateTime timestamp) {
    this.eventType = eventType;
    this.timestamp = timestamp;
  }

  public TelemetryEvent eventType(EventTypeEnum eventType) {
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

  public TelemetryEvent timestamp(OffsetDateTime timestamp) {
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

  public TelemetryEvent nodeId(@Nullable String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  /**
   * Get nodeId
   * @return nodeId
   */
  
  @Schema(name = "nodeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nodeId")
  public @Nullable String getNodeId() {
    return nodeId;
  }

  public void setNodeId(@Nullable String nodeId) {
    this.nodeId = nodeId;
  }

  public TelemetryEvent optionId(@Nullable String optionId) {
    this.optionId = optionId;
    return this;
  }

  /**
   * Get optionId
   * @return optionId
   */
  
  @Schema(name = "optionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("optionId")
  public @Nullable String getOptionId() {
    return optionId;
  }

  public void setOptionId(@Nullable String optionId) {
    this.optionId = optionId;
  }

  public TelemetryEvent outcome(@Nullable String outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable String getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable String outcome) {
    this.outcome = outcome;
  }

  public TelemetryEvent latencyMs(@Nullable Integer latencyMs) {
    this.latencyMs = latencyMs;
    return this;
  }

  /**
   * Get latencyMs
   * @return latencyMs
   */
  
  @Schema(name = "latencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyMs")
  public @Nullable Integer getLatencyMs() {
    return latencyMs;
  }

  public void setLatencyMs(@Nullable Integer latencyMs) {
    this.latencyMs = latencyMs;
  }

  public TelemetryEvent metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public TelemetryEvent putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TelemetryEvent telemetryEvent = (TelemetryEvent) o;
    return Objects.equals(this.eventType, telemetryEvent.eventType) &&
        Objects.equals(this.timestamp, telemetryEvent.timestamp) &&
        Objects.equals(this.nodeId, telemetryEvent.nodeId) &&
        Objects.equals(this.optionId, telemetryEvent.optionId) &&
        Objects.equals(this.outcome, telemetryEvent.outcome) &&
        Objects.equals(this.latencyMs, telemetryEvent.latencyMs) &&
        Objects.equals(this.metadata, telemetryEvent.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, timestamp, nodeId, optionId, outcome, latencyMs, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TelemetryEvent {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    optionId: ").append(toIndentedString(optionId)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    latencyMs: ").append(toIndentedString(latencyMs)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

