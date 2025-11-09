package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.GlobalEvent;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * GetActiveGlobalEvents200Response
 */

@JsonTypeName("getActiveGlobalEvents_200_response")

public class GetActiveGlobalEvents200Response {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime gameTime;

  @Valid
  private List<@Valid GlobalEvent> activeEvents = new ArrayList<>();

  public GetActiveGlobalEvents200Response gameTime(@Nullable OffsetDateTime gameTime) {
    this.gameTime = gameTime;
    return this;
  }

  /**
   * Get gameTime
   * @return gameTime
   */
  @Valid 
  @Schema(name = "game_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("game_time")
  public @Nullable OffsetDateTime getGameTime() {
    return gameTime;
  }

  public void setGameTime(@Nullable OffsetDateTime gameTime) {
    this.gameTime = gameTime;
  }

  public GetActiveGlobalEvents200Response activeEvents(List<@Valid GlobalEvent> activeEvents) {
    this.activeEvents = activeEvents;
    return this;
  }

  public GetActiveGlobalEvents200Response addActiveEventsItem(GlobalEvent activeEventsItem) {
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
  public List<@Valid GlobalEvent> getActiveEvents() {
    return activeEvents;
  }

  public void setActiveEvents(List<@Valid GlobalEvent> activeEvents) {
    this.activeEvents = activeEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetActiveGlobalEvents200Response getActiveGlobalEvents200Response = (GetActiveGlobalEvents200Response) o;
    return Objects.equals(this.gameTime, getActiveGlobalEvents200Response.gameTime) &&
        Objects.equals(this.activeEvents, getActiveGlobalEvents200Response.activeEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameTime, activeEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveGlobalEvents200Response {\n");
    sb.append("    gameTime: ").append(toIndentedString(gameTime)).append("\n");
    sb.append("    activeEvents: ").append(toIndentedString(activeEvents)).append("\n");
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

