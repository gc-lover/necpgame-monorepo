package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.TimelineEventDetailed;
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
 * GetDetailedTimeline200Response
 */

@JsonTypeName("getDetailedTimeline_200_response")

public class GetDetailedTimeline200Response {

  private @Nullable String period;

  @Valid
  private List<@Valid TimelineEventDetailed> events = new ArrayList<>();

  public GetDetailedTimeline200Response period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public GetDetailedTimeline200Response events(List<@Valid TimelineEventDetailed> events) {
    this.events = events;
    return this;
  }

  public GetDetailedTimeline200Response addEventsItem(TimelineEventDetailed eventsItem) {
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
  @Valid 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<@Valid TimelineEventDetailed> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid TimelineEventDetailed> events) {
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
    GetDetailedTimeline200Response getDetailedTimeline200Response = (GetDetailedTimeline200Response) o;
    return Objects.equals(this.period, getDetailedTimeline200Response.period) &&
        Objects.equals(this.events, getDetailedTimeline200Response.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(period, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetDetailedTimeline200Response {\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
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

