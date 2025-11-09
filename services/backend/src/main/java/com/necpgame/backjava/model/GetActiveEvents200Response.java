package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.ActiveEventInstance;
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
 * GetActiveEvents200Response
 */

@JsonTypeName("getActiveEvents_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetActiveEvents200Response {

  @Valid
  private List<@Valid ActiveEventInstance> activeEvents = new ArrayList<>();

  private @Nullable Integer maxActiveEvents;

  public GetActiveEvents200Response activeEvents(List<@Valid ActiveEventInstance> activeEvents) {
    this.activeEvents = activeEvents;
    return this;
  }

  public GetActiveEvents200Response addActiveEventsItem(ActiveEventInstance activeEventsItem) {
    if (this.activeEvents == null) {
      this.activeEvents = new ArrayList<>();
    }
    this.activeEvents.add(activeEventsItem);
    return this;
  }

  /**
   * Get activeEvents
   * @return activeEvents
   */
  @Valid 
  @Schema(name = "active_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_events")
  public List<@Valid ActiveEventInstance> getActiveEvents() {
    return activeEvents;
  }

  public void setActiveEvents(List<@Valid ActiveEventInstance> activeEvents) {
    this.activeEvents = activeEvents;
  }

  public GetActiveEvents200Response maxActiveEvents(@Nullable Integer maxActiveEvents) {
    this.maxActiveEvents = maxActiveEvents;
    return this;
  }

  /**
   * Get maxActiveEvents
   * @return maxActiveEvents
   */
  
  @Schema(name = "max_active_events", example = "5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_active_events")
  public @Nullable Integer getMaxActiveEvents() {
    return maxActiveEvents;
  }

  public void setMaxActiveEvents(@Nullable Integer maxActiveEvents) {
    this.maxActiveEvents = maxActiveEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetActiveEvents200Response getActiveEvents200Response = (GetActiveEvents200Response) o;
    return Objects.equals(this.activeEvents, getActiveEvents200Response.activeEvents) &&
        Objects.equals(this.maxActiveEvents, getActiveEvents200Response.maxActiveEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeEvents, maxActiveEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveEvents200Response {\n");
    sb.append("    activeEvents: ").append(toIndentedString(activeEvents)).append("\n");
    sb.append("    maxActiveEvents: ").append(toIndentedString(maxActiveEvents)).append("\n");
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

