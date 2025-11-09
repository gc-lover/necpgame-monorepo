package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CityTimelineEntry
 */


public class CityTimelineEntry {

  private @Nullable Integer year;

  @Valid
  private List<String> events = new ArrayList<>();

  private JsonNullable<Integer> population = JsonNullable.<Integer>undefined();

  private JsonNullable<String> controllingFaction = JsonNullable.<String>undefined();

  public CityTimelineEntry year(@Nullable Integer year) {
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

  public CityTimelineEntry events(List<String> events) {
    this.events = events;
    return this;
  }

  public CityTimelineEntry addEventsItem(String eventsItem) {
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
  
  @Schema(name = "events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<String> getEvents() {
    return events;
  }

  public void setEvents(List<String> events) {
    this.events = events;
  }

  public CityTimelineEntry population(Integer population) {
    this.population = JsonNullable.of(population);
    return this;
  }

  /**
   * Get population
   * @return population
   */
  
  @Schema(name = "population", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("population")
  public JsonNullable<Integer> getPopulation() {
    return population;
  }

  public void setPopulation(JsonNullable<Integer> population) {
    this.population = population;
  }

  public CityTimelineEntry controllingFaction(String controllingFaction) {
    this.controllingFaction = JsonNullable.of(controllingFaction);
    return this;
  }

  /**
   * Get controllingFaction
   * @return controllingFaction
   */
  
  @Schema(name = "controlling_faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlling_faction")
  public JsonNullable<String> getControllingFaction() {
    return controllingFaction;
  }

  public void setControllingFaction(JsonNullable<String> controllingFaction) {
    this.controllingFaction = controllingFaction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CityTimelineEntry cityTimelineEntry = (CityTimelineEntry) o;
    return Objects.equals(this.year, cityTimelineEntry.year) &&
        Objects.equals(this.events, cityTimelineEntry.events) &&
        equalsNullable(this.population, cityTimelineEntry.population) &&
        equalsNullable(this.controllingFaction, cityTimelineEntry.controllingFaction);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, events, hashCodeNullable(population), hashCodeNullable(controllingFaction));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CityTimelineEntry {\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
    sb.append("    population: ").append(toIndentedString(population)).append("\n");
    sb.append("    controllingFaction: ").append(toIndentedString(controllingFaction)).append("\n");
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

