package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CalculateProgressionRequestRecentEventsInner
 */

@JsonTypeName("CalculateProgressionRequest_recent_events_inner")

public class CalculateProgressionRequestRecentEventsInner {

  private String eventType;

  private Float impact;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public CalculateProgressionRequestRecentEventsInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculateProgressionRequestRecentEventsInner(String eventType, Float impact) {
    this.eventType = eventType;
    this.impact = impact;
  }

  public CalculateProgressionRequestRecentEventsInner eventType(String eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Тип события
   * @return eventType
   */
  @NotNull 
  @Schema(name = "event_type", description = "Тип события", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("event_type")
  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public CalculateProgressionRequestRecentEventsInner impact(Float impact) {
    this.impact = impact;
    return this;
  }

  /**
   * Влияние события на прогрессию (-100..100)
   * @return impact
   */
  @NotNull 
  @Schema(name = "impact", description = "Влияние события на прогрессию (-100..100)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("impact")
  public Float getImpact() {
    return impact;
  }

  public void setImpact(Float impact) {
    this.impact = impact;
  }

  public CalculateProgressionRequestRecentEventsInner timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Время события
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", description = "Время события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateProgressionRequestRecentEventsInner calculateProgressionRequestRecentEventsInner = (CalculateProgressionRequestRecentEventsInner) o;
    return Objects.equals(this.eventType, calculateProgressionRequestRecentEventsInner.eventType) &&
        Objects.equals(this.impact, calculateProgressionRequestRecentEventsInner.impact) &&
        Objects.equals(this.timestamp, calculateProgressionRequestRecentEventsInner.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, impact, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateProgressionRequestRecentEventsInner {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    impact: ").append(toIndentedString(impact)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

