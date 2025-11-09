package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.EventMatrixRelationshipsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
 * EventMatrix
 */


public class EventMatrix {

  private @Nullable Integer totalEvents;

  @Valid
  private Map<String, Integer> eventCategories = new HashMap<>();

  @Valid
  private List<@Valid EventMatrixRelationshipsInner> relationships = new ArrayList<>();

  @Valid
  private List<Object> criticalPaths = new ArrayList<>();

  public EventMatrix totalEvents(@Nullable Integer totalEvents) {
    this.totalEvents = totalEvents;
    return this;
  }

  /**
   * Get totalEvents
   * @return totalEvents
   */
  
  @Schema(name = "total_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_events")
  public @Nullable Integer getTotalEvents() {
    return totalEvents;
  }

  public void setTotalEvents(@Nullable Integer totalEvents) {
    this.totalEvents = totalEvents;
  }

  public EventMatrix eventCategories(Map<String, Integer> eventCategories) {
    this.eventCategories = eventCategories;
    return this;
  }

  public EventMatrix putEventCategoriesItem(String key, Integer eventCategoriesItem) {
    if (this.eventCategories == null) {
      this.eventCategories = new HashMap<>();
    }
    this.eventCategories.put(key, eventCategoriesItem);
    return this;
  }

  /**
   * Get eventCategories
   * @return eventCategories
   */
  
  @Schema(name = "event_categories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_categories")
  public Map<String, Integer> getEventCategories() {
    return eventCategories;
  }

  public void setEventCategories(Map<String, Integer> eventCategories) {
    this.eventCategories = eventCategories;
  }

  public EventMatrix relationships(List<@Valid EventMatrixRelationshipsInner> relationships) {
    this.relationships = relationships;
    return this;
  }

  public EventMatrix addRelationshipsItem(EventMatrixRelationshipsInner relationshipsItem) {
    if (this.relationships == null) {
      this.relationships = new ArrayList<>();
    }
    this.relationships.add(relationshipsItem);
    return this;
  }

  /**
   * Связи между событиями
   * @return relationships
   */
  @Valid 
  @Schema(name = "relationships", description = "Связи между событиями", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationships")
  public List<@Valid EventMatrixRelationshipsInner> getRelationships() {
    return relationships;
  }

  public void setRelationships(List<@Valid EventMatrixRelationshipsInner> relationships) {
    this.relationships = relationships;
  }

  public EventMatrix criticalPaths(List<Object> criticalPaths) {
    this.criticalPaths = criticalPaths;
    return this;
  }

  public EventMatrix addCriticalPathsItem(Object criticalPathsItem) {
    if (this.criticalPaths == null) {
      this.criticalPaths = new ArrayList<>();
    }
    this.criticalPaths.add(criticalPathsItem);
    return this;
  }

  /**
   * Критические пути сюжета
   * @return criticalPaths
   */
  
  @Schema(name = "critical_paths", description = "Критические пути сюжета", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_paths")
  public List<Object> getCriticalPaths() {
    return criticalPaths;
  }

  public void setCriticalPaths(List<Object> criticalPaths) {
    this.criticalPaths = criticalPaths;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventMatrix eventMatrix = (EventMatrix) o;
    return Objects.equals(this.totalEvents, eventMatrix.totalEvents) &&
        Objects.equals(this.eventCategories, eventMatrix.eventCategories) &&
        Objects.equals(this.relationships, eventMatrix.relationships) &&
        Objects.equals(this.criticalPaths, eventMatrix.criticalPaths);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalEvents, eventCategories, relationships, criticalPaths);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventMatrix {\n");
    sb.append("    totalEvents: ").append(toIndentedString(totalEvents)).append("\n");
    sb.append("    eventCategories: ").append(toIndentedString(eventCategories)).append("\n");
    sb.append("    relationships: ").append(toIndentedString(relationships)).append("\n");
    sb.append("    criticalPaths: ").append(toIndentedString(criticalPaths)).append("\n");
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

