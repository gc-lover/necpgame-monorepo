package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.GlobalEvent;
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
 * GetEventsByEra200Response
 */

@JsonTypeName("getEventsByEra_200_response")

public class GetEventsByEra200Response {

  private @Nullable String era;

  @Valid
  private List<@Valid GlobalEvent> events = new ArrayList<>();

  public GetEventsByEra200Response era(@Nullable String era) {
    this.era = era;
    return this;
  }

  /**
   * Get era
   * @return era
   */
  
  @Schema(name = "era", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("era")
  public @Nullable String getEra() {
    return era;
  }

  public void setEra(@Nullable String era) {
    this.era = era;
  }

  public GetEventsByEra200Response events(List<@Valid GlobalEvent> events) {
    this.events = events;
    return this;
  }

  public GetEventsByEra200Response addEventsItem(GlobalEvent eventsItem) {
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
  public List<@Valid GlobalEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid GlobalEvent> events) {
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
    GetEventsByEra200Response getEventsByEra200Response = (GetEventsByEra200Response) o;
    return Objects.equals(this.era, getEventsByEra200Response.era) &&
        Objects.equals(this.events, getEventsByEra200Response.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(era, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEventsByEra200Response {\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
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

