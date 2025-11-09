package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.MainGameUIDataWorldStateActiveEventsInner;
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
 * MainGameUIDataWorldState
 */

@JsonTypeName("MainGameUIData_world_state")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MainGameUIDataWorldState {

  private @Nullable String timeOfDay;

  private @Nullable String weather;

  @Valid
  private List<@Valid MainGameUIDataWorldStateActiveEventsInner> activeEvents = new ArrayList<>();

  public MainGameUIDataWorldState timeOfDay(@Nullable String timeOfDay) {
    this.timeOfDay = timeOfDay;
    return this;
  }

  /**
   * Get timeOfDay
   * @return timeOfDay
   */
  
  @Schema(name = "time_of_day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_of_day")
  public @Nullable String getTimeOfDay() {
    return timeOfDay;
  }

  public void setTimeOfDay(@Nullable String timeOfDay) {
    this.timeOfDay = timeOfDay;
  }

  public MainGameUIDataWorldState weather(@Nullable String weather) {
    this.weather = weather;
    return this;
  }

  /**
   * Get weather
   * @return weather
   */
  
  @Schema(name = "weather", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weather")
  public @Nullable String getWeather() {
    return weather;
  }

  public void setWeather(@Nullable String weather) {
    this.weather = weather;
  }

  public MainGameUIDataWorldState activeEvents(List<@Valid MainGameUIDataWorldStateActiveEventsInner> activeEvents) {
    this.activeEvents = activeEvents;
    return this;
  }

  public MainGameUIDataWorldState addActiveEventsItem(MainGameUIDataWorldStateActiveEventsInner activeEventsItem) {
    if (this.activeEvents == null) {
      this.activeEvents = new ArrayList<>();
    }
    this.activeEvents.add(activeEventsItem);
    return this;
  }

  /**
   * Текущие события в мире
   * @return activeEvents
   */
  @Valid 
  @Schema(name = "active_events", description = "Текущие события в мире", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_events")
  public List<@Valid MainGameUIDataWorldStateActiveEventsInner> getActiveEvents() {
    return activeEvents;
  }

  public void setActiveEvents(List<@Valid MainGameUIDataWorldStateActiveEventsInner> activeEvents) {
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
    MainGameUIDataWorldState mainGameUIDataWorldState = (MainGameUIDataWorldState) o;
    return Objects.equals(this.timeOfDay, mainGameUIDataWorldState.timeOfDay) &&
        Objects.equals(this.weather, mainGameUIDataWorldState.weather) &&
        Objects.equals(this.activeEvents, mainGameUIDataWorldState.activeEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeOfDay, weather, activeEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MainGameUIDataWorldState {\n");
    sb.append("    timeOfDay: ").append(toIndentedString(timeOfDay)).append("\n");
    sb.append("    weather: ").append(toIndentedString(weather)).append("\n");
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

