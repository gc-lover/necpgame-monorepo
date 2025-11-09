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
 * GetGlobalEvents200Response
 */

@JsonTypeName("getGlobalEvents_200_response")

public class GetGlobalEvents200Response {

  @Valid
  private List<@Valid GlobalEvent> events = new ArrayList<>();

  private @Nullable Integer total;

  public GetGlobalEvents200Response events(List<@Valid GlobalEvent> events) {
    this.events = events;
    return this;
  }

  public GetGlobalEvents200Response addEventsItem(GlobalEvent eventsItem) {
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

  public GetGlobalEvents200Response total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetGlobalEvents200Response getGlobalEvents200Response = (GetGlobalEvents200Response) o;
    return Objects.equals(this.events, getGlobalEvents200Response.events) &&
        Objects.equals(this.total, getGlobalEvents200Response.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(events, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetGlobalEvents200Response {\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

