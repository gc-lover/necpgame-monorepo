package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TimelineEvent
 */


public class TimelineEvent {

  private @Nullable Integer year;

  private @Nullable String eventId;

  private @Nullable String eventName;

  private @Nullable String eventType;

  private @Nullable Boolean playerCaused;

  @Valid
  private List<String> affectedEntities = new ArrayList<>();

  public TimelineEvent year(@Nullable Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   */
  
  @Schema(name = "year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("year")
  public @Nullable Integer getYear() {
    return year;
  }

  public void setYear(@Nullable Integer year) {
    this.year = year;
  }

  public TimelineEvent eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public TimelineEvent eventName(@Nullable String eventName) {
    this.eventName = eventName;
    return this;
  }

  /**
   * Get eventName
   * @return eventName
   */
  
  @Schema(name = "event_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_name")
  public @Nullable String getEventName() {
    return eventName;
  }

  public void setEventName(@Nullable String eventName) {
    this.eventName = eventName;
  }

  public TimelineEvent eventType(@Nullable String eventType) {
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

  public TimelineEvent playerCaused(@Nullable Boolean playerCaused) {
    this.playerCaused = playerCaused;
    return this;
  }

  /**
   * Get playerCaused
   * @return playerCaused
   */
  
  @Schema(name = "player_caused", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_caused")
  public @Nullable Boolean getPlayerCaused() {
    return playerCaused;
  }

  public void setPlayerCaused(@Nullable Boolean playerCaused) {
    this.playerCaused = playerCaused;
  }

  public TimelineEvent affectedEntities(List<String> affectedEntities) {
    this.affectedEntities = affectedEntities;
    return this;
  }

  public TimelineEvent addAffectedEntitiesItem(String affectedEntitiesItem) {
    if (this.affectedEntities == null) {
      this.affectedEntities = new ArrayList<>();
    }
    this.affectedEntities.add(affectedEntitiesItem);
    return this;
  }

  /**
   * Get affectedEntities
   * @return affectedEntities
   */
  
  @Schema(name = "affected_entities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_entities")
  public List<String> getAffectedEntities() {
    return affectedEntities;
  }

  public void setAffectedEntities(List<String> affectedEntities) {
    this.affectedEntities = affectedEntities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimelineEvent timelineEvent = (TimelineEvent) o;
    return Objects.equals(this.year, timelineEvent.year) &&
        Objects.equals(this.eventId, timelineEvent.eventId) &&
        Objects.equals(this.eventName, timelineEvent.eventName) &&
        Objects.equals(this.eventType, timelineEvent.eventType) &&
        Objects.equals(this.playerCaused, timelineEvent.playerCaused) &&
        Objects.equals(this.affectedEntities, timelineEvent.affectedEntities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, eventId, eventName, eventType, playerCaused, affectedEntities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimelineEvent {\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    playerCaused: ").append(toIndentedString(playerCaused)).append("\n");
    sb.append("    affectedEntities: ").append(toIndentedString(affectedEntities)).append("\n");
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

