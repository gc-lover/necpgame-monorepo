package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.TimelineEvent;
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
 * IncidentTimelineResponse
 */


public class IncidentTimelineResponse {

  private String incidentId;

  @Valid
  private List<@Valid TimelineEvent> events = new ArrayList<>();

  public IncidentTimelineResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IncidentTimelineResponse(String incidentId, List<@Valid TimelineEvent> events) {
    this.incidentId = incidentId;
    this.events = events;
  }

  public IncidentTimelineResponse incidentId(String incidentId) {
    this.incidentId = incidentId;
    return this;
  }

  /**
   * Get incidentId
   * @return incidentId
   */
  @NotNull 
  @Schema(name = "incident_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("incident_id")
  public String getIncidentId() {
    return incidentId;
  }

  public void setIncidentId(String incidentId) {
    this.incidentId = incidentId;
  }

  public IncidentTimelineResponse events(List<@Valid TimelineEvent> events) {
    this.events = events;
    return this;
  }

  public IncidentTimelineResponse addEventsItem(TimelineEvent eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Get events
   * @return events
   */
  @NotNull @Valid 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("events")
  public List<@Valid TimelineEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid TimelineEvent> events) {
    this.events = events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncidentTimelineResponse incidentTimelineResponse = (IncidentTimelineResponse) o;
    return Objects.equals(this.incidentId, incidentTimelineResponse.incidentId) &&
        Objects.equals(this.events, incidentTimelineResponse.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incidentId, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncidentTimelineResponse {\n");
    sb.append("    incidentId: ").append(toIndentedString(incidentId)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

