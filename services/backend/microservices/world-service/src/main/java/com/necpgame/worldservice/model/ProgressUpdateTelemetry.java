package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ProgressUpdateTelemetry
 */

@JsonTypeName("ProgressUpdate_telemetry")

public class ProgressUpdateTelemetry {

  /**
   * Gets or Sets event
   */
  public enum EventEnum {
    CONTRACT_VIEWED("contract_viewed"),
    
    CONTRACT_PROGRESSED("contract_progressed"),
    
    CONTRACT_FAILED("contract_failed");

    private final String value;

    EventEnum(String value) {
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
    public static EventEnum fromValue(String value) {
      for (EventEnum b : EventEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable EventEnum event;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public ProgressUpdateTelemetry event(@Nullable EventEnum event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable EventEnum getEvent() {
    return event;
  }

  public void setEvent(@Nullable EventEnum event) {
    this.event = event;
  }

  public ProgressUpdateTelemetry metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public ProgressUpdateTelemetry putMetadataItem(String key, Object metadataItem) {
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
    ProgressUpdateTelemetry progressUpdateTelemetry = (ProgressUpdateTelemetry) o;
    return Objects.equals(this.event, progressUpdateTelemetry.event) &&
        Objects.equals(this.metadata, progressUpdateTelemetry.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressUpdateTelemetry {\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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

