package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GetEventsImpact200ResponseEventsInner;
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
 * GetEventsImpact200Response
 */

@JsonTypeName("getEventsImpact_200_response")

public class GetEventsImpact200Response {

  @Valid
  private List<@Valid GetEventsImpact200ResponseEventsInner> events = new ArrayList<>();

  public GetEventsImpact200Response events(List<@Valid GetEventsImpact200ResponseEventsInner> events) {
    this.events = events;
    return this;
  }

  public GetEventsImpact200Response addEventsItem(GetEventsImpact200ResponseEventsInner eventsItem) {
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
  public List<@Valid GetEventsImpact200ResponseEventsInner> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid GetEventsImpact200ResponseEventsInner> events) {
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
    GetEventsImpact200Response getEventsImpact200Response = (GetEventsImpact200Response) o;
    return Objects.equals(this.events, getEventsImpact200Response.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEventsImpact200Response {\n");
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

