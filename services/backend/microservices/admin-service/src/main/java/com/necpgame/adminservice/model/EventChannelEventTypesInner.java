package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EventChannelEventTypesInner
 */

@JsonTypeName("EventChannel_event_types_inner")

public class EventChannelEventTypesInner {

  private @Nullable String eventType;

  private @Nullable Object schema;

  public EventChannelEventTypesInner eventType(@Nullable String eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  
  @Schema(name = "event_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_type")
  public @Nullable String getEventType() {
    return eventType;
  }

  public void setEventType(@Nullable String eventType) {
    this.eventType = eventType;
  }

  public EventChannelEventTypesInner schema(@Nullable Object schema) {
    this.schema = schema;
    return this;
  }

  /**
   * Get schema
   * @return schema
   */
  
  @Schema(name = "schema", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("schema")
  public @Nullable Object getSchema() {
    return schema;
  }

  public void setSchema(@Nullable Object schema) {
    this.schema = schema;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventChannelEventTypesInner eventChannelEventTypesInner = (EventChannelEventTypesInner) o;
    return Objects.equals(this.eventType, eventChannelEventTypesInner.eventType) &&
        Objects.equals(this.schema, eventChannelEventTypesInner.schema);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, schema);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventChannelEventTypesInner {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
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

